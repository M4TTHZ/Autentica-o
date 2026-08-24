package com.matheusramalho.Autenticacao.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordPolicyService {

    private static final int TAMANHO_MINIMO = 8;

    private static final List<String> SENHAS_PROIBIDAS = List.of(
            "12345678",
            "senha123",
            "admin123",
            "password",
            "qwerty123",
            "loja2026"
    );


    /**
     * Valida a senha contra a politica. Lanca SenhaInvalidaException com
     * uma mensagem explicando qual regra falhou, para o cadastro poder
     * devolver isso ao cliente da API.
     */

    public void validar(String senha){

        if (senha == null || senha.length() < TAMANHO_MINIMO) {
            throw new SenhaInvalidaException(
                    "A senha precisa conter pelo menos "+ TAMANHO_MINIMO +" caracteres."
            );
        }

        if (!contemLetra(senha) || !contemNumero(senha)) {
            throw new SenhaInvalidaException(
                    "A senha precisa conter LETRAS e NUMEROS."
            );
        }

        if (SENHAS_PROIBIDAS.contains(senha.toLowerCase())){
            throw new SenhaInvalidaException(
                    "Essa senha e muito fraca seu bobo (*^_^*)"
            );
        }


    }

    private boolean contemLetra(String senha){
        return senha.chars().anyMatch(Character::isLetter);
    }

    private boolean contemNumero(String senha){
        return senha.chars().anyMatch(Character::isDigit);
    }

    // Exception dedicada, para o GlobalExceptionHandler (a ser criado)
    // conseguir capturar isso especificamente e devolver HTTP 400 com a
    // mensagem certa, em vez de um erro 500 generico.
    private static class SenhaInvalidaException extends RuntimeException {
        public SenhaInvalidaException(String mensagem) {
            super(mensagem);
        }
    }

}
