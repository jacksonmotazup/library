package br.com.zup.library.devolucao;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class NovaDevolucaoRequest {

    @NotNull
    @Positive
    private final Long usuarioId;
    @NotNull
    @Positive
    private final Long emprestimoId;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getEmprestimoId() {
        return emprestimoId;
    }

    public NovaDevolucaoRequest(Long usuarioId, Long emprestimoId) {
        this.usuarioId = usuarioId;
        this.emprestimoId = emprestimoId;
    }
}
