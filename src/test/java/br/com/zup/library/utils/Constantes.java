package br.com.zup.library.utils;

import java.math.BigDecimal;
import java.util.UUID;

public class Constantes {

    public static final String STRING_EM_BRANCO = "";
    public static final String TITULO = "Titulo livro";
    public static final String ISBN = UUID.randomUUID().toString();
    public static final BigDecimal PRECO = BigDecimal.valueOf(100);
    public static final String CAMPO_CIRCULACAO = "circulacao";
    public static final String CIRCULACAO_LIVRE = "LIVRE";
    public static final String CIRCULACAO_RESTRITA = "RESTRITA";
    public static final String CIRCULACAO_INVALIDA = "invalida";
    public static final String TIPO_USUARIO = "tipo";
    public static final String USUARIO_PADRAO = "PADRAO";
    public static final String USUARIO_PESQUISADOR = "PESQUISADOR";
    public static final String TIPO_INVALIDO = "INVALIDO";

}
