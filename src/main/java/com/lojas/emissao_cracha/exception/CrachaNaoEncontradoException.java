package com.lojas.emissao_cracha.exception;

import lombok.Getter;

public class CrachaNaoEncontradoException extends RuntimeException{

    public CrachaNaoEncontradoException(String message) {
        super(message);
    }


}
