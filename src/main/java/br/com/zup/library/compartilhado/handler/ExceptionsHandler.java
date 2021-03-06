package br.com.zup.library.compartilhado.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> responseStatusExceptionHandler(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getReason());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public String exceptionHandler(Exception ex) {
        return ex.getMessage();
    }


}
