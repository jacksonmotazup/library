package br.com.zup.library.emprestimo;

import br.com.zup.library.exemplar.Exemplar;
import br.com.zup.library.usuario.Usuario;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer prazoDevolucaoDias;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Exemplar exemplar;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @Column(nullable = false, updatable = false)
    private final LocalDate dataCriacao = LocalDate.now();

    public Emprestimo(Integer prazoDevolucaoDias, Exemplar exemplar, Usuario usuario) {
        this.prazoDevolucaoDias = prazoDevolucaoDias;
        this.exemplar = exemplar;
        this.usuario = usuario;
    }

    /**
     * @deprecated (construtor padrão obrigatório por conta do Hibernate)
     */
    @Deprecated(since = "1.0")
    public Emprestimo() {
    }

    public Long getId() {
        return id;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public Integer getPrazoDevolucaoDias() {
        return prazoDevolucaoDias;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }
}
