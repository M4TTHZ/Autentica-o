package com.matheusramalho.Autenticacao.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String senha;

    private String codigoTotp;

    public LoginRequestDTO(){}

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getSenha(){
        return senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public String getCodigoTotp(){
        return codigoTotp;
    }

    public void setCodigoTotp(String codigoTotp){
        this.codigoTotp = codigoTotp;
    }

}
