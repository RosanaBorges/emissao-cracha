package com.lojas.emissao_cracha.exception.handler;


import com.lojas.emissao_cracha.exception.CrachaNaoEncontradoException;
import com.lojas.emissao_cracha.exception.ErroInternoException;
import com.lojas.emissao_cracha.exception.InserirFotoException;
import com.lojas.emissao_cracha.exception.SalvarFotoException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            Map<String, String> errorMap = new HashMap<>();
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMap.put("field", fieldName);
            errorMap.put("message", errorMessage);
            errors.add(errorMap);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InserirFotoException.class)
    public Map<String, String> handleInserirFotoException(InserirFotoException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(exception.getField(), exception.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CrachaNaoEncontradoException.class)
    public Map<String, String> handleCrachaNaoEncontradoException(CrachaNaoEncontradoException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", exception.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ErroInternoException.class)
    public Map<String, String> handleErroInternoException(ErroInternoException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", exception.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SalvarFotoException.class)
    public Map<String, String> handleSalvarFotoException(SalvarFotoException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", exception.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleRuntimeException(RuntimeException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", exception.getMessage());
        return errorMap;
    }
}
