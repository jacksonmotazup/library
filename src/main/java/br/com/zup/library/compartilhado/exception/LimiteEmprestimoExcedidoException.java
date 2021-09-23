package br.com.zup.library.compartilhado.exception;

public class LimiteEmprestimoExcedidoException extends RuntimeException {

    public LimiteEmprestimoExcedidoException(String mensagem) {
        super(mensagem);
    }
}
