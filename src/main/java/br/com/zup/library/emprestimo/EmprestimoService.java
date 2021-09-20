package br.com.zup.library.emprestimo;

import br.com.zup.library.exemplar.Exemplar;
import br.com.zup.library.exemplar.ExemplarRepository;
import br.com.zup.library.livro.LivroRepository;
import br.com.zup.library.usuario.Usuario;
import br.com.zup.library.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.function.Supplier;

import static br.com.zup.library.exemplar.TipoCirculacao.LIVRE;
import static br.com.zup.library.usuario.TipoUsuario.PADRAO;
import static br.com.zup.library.usuario.TipoUsuario.PESQUISADOR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExemplarRepository exemplarRepository;

    @Autowired
    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             LivroRepository livroRepository,
                             UsuarioRepository usuarioRepository,
                             ExemplarRepository exemplarRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.exemplarRepository = exemplarRepository;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public NovoEmprestimoResponse cadastra(NovoEmprestimoRequest request) {
        var usuario = buscaUsuario(request);

        var exemplar = buscaExemplar(request, usuario);

        validaEmprestimo(usuario);

        var novoEmprestimo = toModel(request, exemplar, usuario);

        emprestimoRepository.save(novoEmprestimo);
        exemplarRepository.save(exemplar.alteraDisponibilidade());

        return new NovoEmprestimoResponse(novoEmprestimo);
    }

    private void validaEmprestimo(Usuario usuario) {
        if (PADRAO.equals(usuario.getTipoUsuario()) && emprestimoRepository.findAllByUsuario(usuario).size() > 5) {
            throw new ResponseStatusException(BAD_REQUEST, "Usuário padrão só pode ter 5 empréstimos simultâneos");
        }
    }

    private Emprestimo toModel(NovoEmprestimoRequest request, Exemplar exemplar, Usuario usuario) {
        Integer prazoDevolucao = null;

        if (PADRAO.equals(usuario.getTipoUsuario()) && Objects.isNull(request.getPrazoDevolucao())) {
            throw new ResponseStatusException(BAD_REQUEST, "Prazo deve ser preenchido");
        } else if (PADRAO.equals(usuario.getTipoUsuario()) && !Objects.isNull(request.getPrazoDevolucao())) {
            prazoDevolucao = request.getPrazoDevolucao();
        }

        if (PESQUISADOR.equals(usuario.getTipoUsuario()) && Objects.isNull(request.getPrazoDevolucao())) {
            prazoDevolucao = 60;
        } else if (PESQUISADOR.equals(usuario.getTipoUsuario()) && !Objects.isNull(request.getPrazoDevolucao())) {
            prazoDevolucao = request.getPrazoDevolucao();
        }

        return new Emprestimo(prazoDevolucao, exemplar, usuario);
    }

    private Usuario buscaUsuario(NovoEmprestimoRequest request) {
        return usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(throwNotFound("Usuário não encontrado"));

    }

    private Exemplar buscaExemplar(NovoEmprestimoRequest request, Usuario usuario) {
        var livro = livroRepository.findById(request.getIdLivro())
                .orElseThrow(throwNotFound("Livro não encontrado"));

        Exemplar exemplar;
        if (PADRAO.equals(usuario.getTipoUsuario())) {
            exemplar = exemplarRepository.findFirstByLivroAndTipoCirculacaoAndDisponivelTrue(livro, LIVRE)
                    .orElseThrow(throwNotFound("Não existe exemplar disponível"));
        } else exemplar = exemplarRepository.findFirstByLivroAndDisponivelTrueOrderByTipoCirculacaoDesc(livro)
                .orElseThrow(throwNotFound("Não existe exemplar disponível"));

        return exemplar;
    }

    private Supplier<ResponseStatusException> throwNotFound(String mensagem) {
        return () -> new ResponseStatusException(NOT_FOUND, mensagem);
    }
}
