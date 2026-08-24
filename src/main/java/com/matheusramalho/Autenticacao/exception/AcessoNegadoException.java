package com.matheusramalho.Autenticacao.exception;

/**
 * Lancada quando uma acao exige um papel/autenticacao que a requisicao
 * atual nao tem (ex: nao-admin tentando criar VENDEDOR/FINANCEIRO).
 * Mapeada pelo GlobalExceptionHandler para HTTP 403 (Forbidden).
 */
public class AcessoNegadoException extends RuntimeException {
    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}
