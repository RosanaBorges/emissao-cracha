package com.lojas.emissao_cracha.exception;

public class ErroInternoException extends RuntimeException {
    public ErroInternoException(String message) {
        super(message);
    }

    public ErroInternoException(String message, Throwable cause) {
        super(message, cause);
    }
}
