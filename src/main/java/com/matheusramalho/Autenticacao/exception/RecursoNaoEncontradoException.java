package com.matheusramalho.Autenticacao.exception;

// Usada quando um id (cliente, produto, venda) nao existe no banco.
// Mapeada para HTTP 404.
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
