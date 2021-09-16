package br.com.zup.library.usuario;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private TipoUsuario tipoUsuario;

    public Usuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * @deprecated (construtor padrão obrigatório por conta do Hibernate)
     */
    @Deprecated(since = "1.0")
    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
}
