package br.com.zup.library.emprestimo;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;


public class NovoEmprestimoRequest {

    @NotNull
    private Long idLivro;
    @NotNull
    private Long idUsuario;
    @Range(min = 1, max = 60)
    private Integer prazoDevolucao;

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
