package br.com.zup.library.utils;

import br.com.zup.library.livro.NovoLivroRequest;

import java.math.BigDecimal;
import java.util.UUID;

public class TestFactory {

    public static final String TITULO = "Titulo livro";
    public static final String ISBN = UUID.randomUUID().toString();
    public static final BigDecimal PRECO = BigDecimal.valueOf(100);

    public static NovoLivroRequest criaNovoLivroRequest() {
        return new NovoLivroRequest(TITULO, PRECO, ISBN);
    }
}
