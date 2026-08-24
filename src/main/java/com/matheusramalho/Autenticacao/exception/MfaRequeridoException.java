package com.matheusramalho.Autenticacao.exception;

// Lancada quando username+senha estao corretos mas falta o codigo TOTP
// (papel ADMIN). O controller usa isso para pedir o codigo, sem contar
// como tentativa falha.
public class MfaRequeridoException extends RuntimeException {
  public MfaRequeridoException(String mensagem) {
    super(mensagem);
  }
}
