package com.matheusramalho.Autenticacao.exception;

// Lancada quando a conta esta em periodo de bloqueio por tentativas (B3).
// Mapeada para HTTP 401 tambem.
public class ContaBloqueadaException extends RuntimeException {
    public ContaBloqueadaException(String mensagem) {
        super(mensagem);
    }
}
