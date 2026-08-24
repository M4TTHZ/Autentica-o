package com.matheusramalho.Autenticacao.exception;

public class UsuarioDuplicadoException extends RuntimeException{
    public UsuarioDuplicadoException(String mensagem){
        super(mensagem);
    }
}
