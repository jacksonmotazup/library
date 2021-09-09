package br.com.zup.library.livro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false, unique = true)
    private String isbn;

    public Livro(String titulo, BigDecimal preco, String isbn) {
        this.titulo = titulo;
        this.preco = preco;
        this.isbn = isbn;
    }

    /**
     * @deprecated (construtor padrão obrigatório por conta do Hibernate)
     */
    @Deprecated(since = "1.0")
    public Livro() {
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }
}
