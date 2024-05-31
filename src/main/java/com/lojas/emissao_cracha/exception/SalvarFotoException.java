package com.lojas.emissao_cracha.exception;

import lombok.Getter;

public class SalvarFotoException extends RuntimeException{
    @Getter
    private String field;

    public SalvarFotoException(String message, String field) {
        super(message);
        this.field = field;
    }
}
