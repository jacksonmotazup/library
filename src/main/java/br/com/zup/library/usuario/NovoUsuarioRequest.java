package br.com.zup.library.usuario;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class NovoUsuarioRequest {

    @NotBlank
    @Pattern(regexp = "(PADRAO|PESQUISADOR)", message = "Tipo deve ser PADRAO ou PESQUISADOR")
    private String tipo;

    public String getTipo() {
        return tipo;
    }

    public Usuario toModel() {
        return new Usuario(TipoUsuario.valueOf(tipo));
    }
}
