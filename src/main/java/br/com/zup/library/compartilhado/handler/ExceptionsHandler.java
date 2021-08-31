package br.com.zup.library.compartilhado.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionHandlerResponse> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException ex) {
        var fieldErrors = ex.getBindingResult().getFieldErrors();
        var campo = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(" - "));
        var mensagem = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(" - "));

        var response = new ExceptionHandlerResponse(campo, mensagem, String.valueOf(HttpStatus.BAD_REQUEST));

        return ResponseEntity.badRequest().body(response);
    }
}
