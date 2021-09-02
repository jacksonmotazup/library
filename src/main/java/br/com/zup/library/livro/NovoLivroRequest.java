package br.com.zup.library.livro;

import br.com.zup.library.compartilhado.validacao.CampoUnico;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class NovoLivroRequest {

    @NotBlank
    private final String titulo;
    @NotNull
    private final BigDecimal preco;
    @NotBlank
    @CampoUnico(campo = "isbn", classe = Livro.class, message = "ISBN ja cadastrado.")
    private final String isbn;

    public NovoLivroRequest(String titulo, BigDecimal preco, String isbn) {
        this.titulo = titulo;
        this.preco = preco;
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public String getIsbn() {
        return isbn;
    }

    public Livro toModel() {
        return new Livro(titulo, preco, isbn);
    }
}
