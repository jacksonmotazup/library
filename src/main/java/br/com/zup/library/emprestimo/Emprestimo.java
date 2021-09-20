package br.com.zup.library.emprestimo;

import br.com.zup.library.exemplar.Exemplar;
import br.com.zup.library.usuario.Usuario;

import javax.persistence.*;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer prazoDevolucao;
    @ManyToOne
    private Exemplar exemplar;
    @ManyToOne
    private Usuario usuario;

    public Emprestimo(Integer prazoDevolucao, Exemplar exemplar, Usuario usuario) {
        this.prazoDevolucao = prazoDevolucao;
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

    public Integer getPrazoDevolucao() {
        return prazoDevolucao;
    }
}
