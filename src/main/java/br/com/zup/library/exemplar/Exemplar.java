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
    private Circulacao circulacao;
    @ManyToOne
    private Livro livro;

    public Exemplar(Circulacao circulacao, Livro livro) {
        this.circulacao = circulacao;
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

    public Circulacao getCirculacao() {
        return circulacao;
    }

    public Livro getLivro() {
        return livro;
    }
}
