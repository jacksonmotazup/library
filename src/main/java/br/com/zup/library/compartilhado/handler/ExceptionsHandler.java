package br.com.zup.library.compartilhado.handler;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionHandlerResponse methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException ex) {

        var map = new HashMap<String, List<String>>();

        ex.getBindingResult().getFieldErrors().forEach(erro -> {
            if (map.containsKey(erro.getField())) {
                map.get(erro.getField()).add(erro.getDefaultMessage());
            } else {
                var list = new ArrayList<String>();
                list.add(erro.getDefaultMessage());
                map.put(erro.getField(), list);
            }
        });

        return new ExceptionHandlerResponse(map);
    }
}
