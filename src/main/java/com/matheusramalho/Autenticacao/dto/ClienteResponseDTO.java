package com.matheusramalho.Autenticacao.dto;


import com.matheusramalho.Autenticacao.model.Cliente;

public class ClienteResponseDTO {

    private final Long id;
    private final String nome;
    private final String cpf;
    private final String cep;

    public ClienteResponseDTO(Long id, String nome, String cpf, String cep) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.cep = cep;
    }

    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getCep());
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getCep() { return cep; }
}
