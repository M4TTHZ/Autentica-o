package com.matheusramalho.Autenticacao.dto;

import jakarta.validation.constraints.NotBlank;

public class ClienteRequestDTO {

    @NotBlank(message = "nome e obrigatorio")
    private String nome;

    @NotBlank(message = "cpf e obrigatorio")
    private String cpf;

    @NotBlank(message = "cep e obrigatorio")
    private String cep;

    public ClienteRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
}

