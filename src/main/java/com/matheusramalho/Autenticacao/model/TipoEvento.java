package com.matheusramalho.Autenticacao.model;

public enum TipoEvento {
    USUARIO_CRIADO,
    LOGIN_SUCESSO,
    LOGIN_FALHOU,
    CONTA_BLOQUEADA,
    CONTA_DESBLOQUEADA,
    PAPEL_ALTERADO,
    MFA_ATIVO,
    ACESSO_NEGADO
}
