package br.com.zup.library.utils;

import br.com.zup.library.exemplar.NovoExemplarRequest;
import br.com.zup.library.livro.NovoLivroRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.UUID;

public class TestFactory {

    public static final String TITULO_EM_BRANCO = "";
    public static final String TITULO = "Titulo livro";
    public static final String ISBN = UUID.randomUUID().toString();
    public static final String ISBN_EM_BRANCO = "";
    public static final BigDecimal PRECO = BigDecimal.valueOf(100);
    public static final BigDecimal PRECO_NULO = null;
    public static final String CAMPO_CIRCULACAO = "circulacao";
    public static final String CIRCULACAO_LIVRE = "LIVRE";
    public static final String CIRCULACAO_RESTRITA = "RESTRITA";
    public static final String CIRCULACAO_INVALIDA = "invalida";

    public static NovoLivroRequest criaNovoLivroRequest() {
        return new NovoLivroRequest(TITULO, PRECO, ISBN);
    }

    public static NovoLivroRequest criaNovoLivroRequestEmBranco() {
        return new NovoLivroRequest(TITULO_EM_BRANCO, PRECO_NULO, ISBN_EM_BRANCO);
    }

    public static NovoExemplarRequest criaNovoExemplarRequestLivre() {
        var exemplar = new NovoExemplarRequest();
        ReflectionTestUtils.setField(exemplar, CAMPO_CIRCULACAO, CIRCULACAO_LIVRE);
        return exemplar;
    }

    public static NovoExemplarRequest criaNovoExemplarRequestRestrita() {
        var exemplar = new NovoExemplarRequest();
        ReflectionTestUtils.setField(exemplar, CAMPO_CIRCULACAO, CIRCULACAO_RESTRITA);
        return exemplar;
    }

    public static NovoExemplarRequest criaNovoExemplarRequestEmBranco() {
        return new NovoExemplarRequest();
    }

    public static NovoExemplarRequest criaNovoExemplarRequestCirculacaoInvalida() {
        var exemplar = new NovoExemplarRequest();
        ReflectionTestUtils.setField(exemplar, CAMPO_CIRCULACAO, CIRCULACAO_INVALIDA);
        return exemplar;
    }
}
