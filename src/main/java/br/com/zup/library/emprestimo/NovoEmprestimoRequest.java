package br.com.zup.library.emprestimo;

import br.com.zup.library.livro.LivroRepository;
import br.com.zup.library.usuario.TipoUsuario;
import br.com.zup.library.usuario.UsuarioRepository;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.Objects;

import static br.com.zup.library.usuario.TipoUsuario.PADRAO;
import static br.com.zup.library.usuario.TipoUsuario.PESQUISADOR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;


public class NovoEmprestimoRequest {

    public final Integer PRAZO_MAXIMO = 60;

    @NotNull
    private final Long idLivro;
    @NotNull
    private final Long idUsuario;
    @Range(min = 1, max = 60)
    private final Integer prazoDevolucao;

    public NovoEmprestimoRequest(Long idLivro, Long idUsuario, Integer prazoDevolucao) {
        this.idLivro = idLivro;
        this.idUsuario = idUsuario;
        this.prazoDevolucao = prazoDevolucao;
    }

    public Long getIdLivro() {
        return idLivro;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Integer getPrazoDevolucao() {
        return prazoDevolucao;
    }

    public SolicitacaoEmprestimo toModel(UsuarioRepository usuarioRepository, LivroRepository livroRepository) {
        var usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado"));

        var livro = livroRepository.findById(idLivro)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Livro não encontrado"));

        var prazoDevolucaoFinal = retornaPrazoDevolucao(usuario.getTipoUsuario());

        return new SolicitacaoEmprestimo(usuario, livro, prazoDevolucaoFinal);
    }

    private Integer retornaPrazoDevolucao(TipoUsuario tipoUsuario) {
        if (PADRAO.equals(tipoUsuario) && Objects.isNull(prazoDevolucao)) {
            throw new ResponseStatusException(BAD_REQUEST, "Prazo deve ser preenchido");
        } else if (PESQUISADOR.equals(tipoUsuario) && Objects.isNull(prazoDevolucao)) {
            return PRAZO_MAXIMO;
        } else return prazoDevolucao;
    }
}
