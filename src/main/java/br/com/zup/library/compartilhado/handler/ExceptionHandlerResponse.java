package br.com.zup.library.compartilhado.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ExceptionHandlerResponse {

    private Map<String, List<String>> erros;
    private final LocalDateTime ocorridoEm = LocalDateTime.now();

    public ExceptionHandlerResponse(Map<String, List<String>> erros) {
        this.erros = erros;
    }

    public ExceptionHandlerResponse() {
    }

    public Map<String, List<String>> getErros() {
        return erros;
    }

    public LocalDateTime getOcorridoEm() {
        return ocorridoEm;
    }

}
