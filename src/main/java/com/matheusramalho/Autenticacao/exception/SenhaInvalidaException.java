package com.matheusramalho.Autenticacao.exception;

import com.matheusramalho.Autenticacao.service.PasswordPolicyService;

public class SenhaInvalidaException extends RuntimeException{
    public SenhaInvalidaException(String mensagem){
        super(mensagem);
    }
}
