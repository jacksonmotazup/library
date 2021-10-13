package br.com.zup.library.emprestimo;

import br.com.zup.library.compartilhado.exception.EmprestimoExpiradoException;
import br.com.zup.library.compartilhado.exception.ExemplarIndisponivelException;
import br.com.zup.library.compartilhado.exception.LimiteEmprestimoExcedidoException;
import br.com.zup.library.exemplar.Exemplar;
import br.com.zup.library.exemplar.ExemplarRepository;
import br.com.zup.library.livro.Livro;
import br.com.zup.library.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static br.com.zup.library.exemplar.TipoCirculacao.LIVRE;
import static br.com.zup.library.usuario.TipoUsuario.PADRAO;

@Service
public class EmprestimoService {

    public final int QTD_MAX_EMPRESTIMO_USUARIO_PADRAO = 5;
    private final EmprestimoRepository emprestimoRepository;
    private final ExemplarRepository exemplarRepository;

    @Autowired
    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             ExemplarRepository exemplarRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.exemplarRepository = exemplarRepository;
    }

    @Transactional
    public Emprestimo realizaEmprestimo(SolicitacaoEmprestimo solicitacao) {
        this.verificaEmprestimoExpirado(solicitacao);

        var usuario = solicitacao.getUsuario();
        var livro = solicitacao.getLivro();

        this.validaNumeroMaximoEmprestimos(usuario);

        var exemplar = buscaExemplar(livro, usuario);
        var novoEmprestimo = exemplar.reservaEmprestimo(solicitacao.getPrazoDevolucao(), usuario);

        return emprestimoRepository.save(novoEmprestimo);
    }

    private void verificaEmprestimoExpirado(SolicitacaoEmprestimo solicitacao) {
        if (emprestimoRepository.existeEmprestimoExpirado(solicitacao.getUsuario())) {
            throw new EmprestimoExpiradoException("Existe empréstimo expirado");
        }
    }

    private void validaNumeroMaximoEmprestimos(Usuario usuario) {
        if (PADRAO.equals(usuario.getTipoUsuario()) && emprestimoRepository.countByUsuario(usuario)
                >= QTD_MAX_EMPRESTIMO_USUARIO_PADRAO) {
            throw new LimiteEmprestimoExcedidoException("Usuário padrão só pode ter 5 empréstimos simultâneos");
        }
    }

    private Exemplar buscaExemplar(Livro livro, Usuario usuario) {
        if (PADRAO.equals(usuario.getTipoUsuario())) {
            return exemplarRepository.findFirstByLivroAndTipoCirculacaoAndDisponivelTrue(livro, LIVRE)
                    .orElseThrow(() -> new ExemplarIndisponivelException("Não existe exemplar disponível"));

        }
        return exemplarRepository.findFirstByLivroAndDisponivelTrueOrderByTipoCirculacaoDesc(livro)
                .orElseThrow(() -> new ExemplarIndisponivelException("Não existe exemplar disponível"));

    }

}
