package br.com.zup.library.emprestimo;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;


public class NovoEmprestimoRequest {

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
}
