package br.com.zup.library.usuario;

import java.time.LocalDate;

public class NovoUsuarioResponse {

    private Long id;
    private String tipoUsuario;
    private final LocalDate dataCriacao = LocalDate.now();

    public NovoUsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.tipoUsuario = String.valueOf(usuario.getTipoUsuario());
    }

    public NovoUsuarioResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }
}
