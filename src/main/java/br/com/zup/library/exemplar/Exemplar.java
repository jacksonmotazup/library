package br.com.zup.library.exemplar;

import br.com.zup.library.livro.Livro;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Exemplar {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Enumerated(STRING)
    private TipoCirculacao tipoCirculacao;
    @ManyToOne
    private Livro livro;
    private boolean disponivel = true;

    public Exemplar(TipoCirculacao tipoCirculacao, Livro livro) {
        this.tipoCirculacao = tipoCirculacao;
        this.livro = livro;
    }

    /**
     * @deprecated (construtor padrão obrigatório por conta do Hibernate)
     */
    @Deprecated(since = "1.0")
    public Exemplar() {
    }

    public Long getId() {
        return id;
    }

    public TipoCirculacao getCirculacao() {
        return tipoCirculacao;
    }

    public Livro getLivro() {
        return livro;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public Exemplar alteraDisponibilidade() {
        this.disponivel = !this.disponivel;
        return this;
    }
}
