package com.matheusramalho.Autenticacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VendaRequestDTO {

    @NotNull(message = "produtoId e obrigatorio")
    private Long produtoId;

    @NotNull(message = "clienteId e obrigatorio")
    private Long clienteId;

    @NotNull(message = "quantidade e obrigatoria")
    @Positive(message = "quantidade precisa ser maior que zero")
    private Integer quantidade;

    public VendaRequestDTO() {}

    public Long getProdutoId() {return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
