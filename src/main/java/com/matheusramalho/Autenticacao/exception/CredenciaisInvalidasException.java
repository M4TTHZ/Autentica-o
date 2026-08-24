package com.matheusramalho.Autenticacao.exception;

// Sempre a mesma mensagem, nao importa se o motivo foi "usuario nao existe"
// ou "senha errada" -- evita enumeracao de usuario. Mapeada para HTTP 401.
public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
