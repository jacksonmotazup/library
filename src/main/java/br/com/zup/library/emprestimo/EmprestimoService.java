package br.com.zup.library.emprestimo;

import br.com.zup.library.exemplar.Exemplar;
import br.com.zup.library.exemplar.ExemplarRepository;
import br.com.zup.library.livro.Livro;
import br.com.zup.library.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import static br.com.zup.library.exemplar.TipoCirculacao.LIVRE;
import static br.com.zup.library.usuario.TipoUsuario.PADRAO;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final ExemplarRepository exemplarRepository;

    @Autowired
    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             ExemplarRepository exemplarRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.exemplarRepository = exemplarRepository;
    }

    @Transactional
    public NovoEmprestimoResponse realizaEmprestimo(SolicitacaoEmprestimo request) {
        var usuario = request.getUsuario();
        var livro = request.getLivro();

        var exemplar = buscaExemplar(livro, usuario);

        validaNumeroMaximoEmprestimos(usuario);

        var novoEmprestimo = toModel(request, exemplar, usuario);

        emprestimoRepository.save(novoEmprestimo);
        exemplarRepository.save(exemplar.alteraDisponibilidade());

        return new NovoEmprestimoResponse(novoEmprestimo);
    }

    private void validaNumeroMaximoEmprestimos(Usuario usuario) {
        if (PADRAO.equals(usuario.getTipoUsuario()) && emprestimoRepository.findAllByUsuario(usuario).size() >= 5) {
            throw new ResponseStatusException(BAD_REQUEST, "Usuário padrão só pode ter 5 empréstimos simultâneos");
        }
    }

    private Emprestimo toModel(SolicitacaoEmprestimo request, Exemplar exemplar, Usuario usuario) {
        return new Emprestimo(request.getPrazoDevolucao(), exemplar, usuario);
    }

    private Exemplar buscaExemplar(Livro livro, Usuario usuario) {
        Exemplar exemplar;
        if (PADRAO.equals(usuario.getTipoUsuario())) {
            exemplar = exemplarRepository.findFirstByLivroAndTipoCirculacaoAndDisponivelTrue(livro, LIVRE)
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Não existe exemplar disponível"));
        } else exemplar = exemplarRepository.findFirstByLivroAndDisponivelTrueOrderByTipoCirculacaoDesc(livro)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Não existe exemplar disponível"));

        return exemplar;
    }

}
