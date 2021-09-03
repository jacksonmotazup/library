package br.com.zup.library.utils;

import br.com.zup.library.livro.NovoLivroRequest;

import java.math.BigDecimal;
import java.util.UUID;

public class TestFactory {

    public static final String TITULO_EM_BRANCO = "";
    public static final String TITULO = "Titulo livro";
    public static final String ISBN = UUID.randomUUID().toString();
    public static final String ISBN_EM_BRANCO = "";
    public static final BigDecimal PRECO = BigDecimal.valueOf(100);
    public static final BigDecimal PRECO_NULO = null;

    public static NovoLivroRequest criaNovoLivroRequest() {
        return new NovoLivroRequest(TITULO, PRECO, ISBN);
    }

    public static NovoLivroRequest criaNovoLivroRequestEmBranco() {
        return new NovoLivroRequest(TITULO_EM_BRANCO, PRECO_NULO, ISBN_EM_BRANCO);
    }
}
