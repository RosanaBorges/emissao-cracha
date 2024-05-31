package com.lojas.emissao_cracha.exception;


import lombok.Getter;

public class InserirFotoException extends RuntimeException{
    @Getter
    private final String field;

    public InserirFotoException(String message, String field) {
        super(message);
        this.field = field;
    }

}
