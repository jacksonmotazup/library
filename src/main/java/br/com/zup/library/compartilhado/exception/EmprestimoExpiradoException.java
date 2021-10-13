package br.com.zup.library.compartilhado.exception;

public class EmprestimoExpiradoException extends RuntimeException {

    public EmprestimoExpiradoException(String mensagem) {
        super(mensagem);
    }
}
