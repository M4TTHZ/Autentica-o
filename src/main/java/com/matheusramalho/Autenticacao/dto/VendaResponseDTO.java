package com.matheusramalho.Autenticacao.dto;

import com.matheusramalho.Autenticacao.model.StatusVenda;
import com.matheusramalho.Autenticacao.model.Venda;

import java.time.LocalDateTime;

public class VendaResponseDTO {

    private final Long id;
    private final String produtoNome;
    private final String clienteNome;
    private final String vendedorUsername;
    private final int quantidade;
    private final StatusVenda status;
    private final LocalDateTime dataHora;

    public VendaResponseDTO(Long id, String produtoNome, String clienteNome, String vendedorUsername,
                            int quantidade, StatusVenda status, LocalDateTime dataHora) {
        this.id = id;
        this.produtoNome = produtoNome;
        this.clienteNome = clienteNome;
        this.vendedorUsername = vendedorUsername;
        this.quantidade = quantidade;
        this.status = status;
        this.dataHora = dataHora;
    }

    public static VendaResponseDTO fromEntity(Venda venda) {
        return new VendaResponseDTO(
                venda.getId(),
                venda.getProduto().getNome(),
                venda.getCliente().getNome(),
                venda.getVendedor().getUsername(),
                venda.getQuantidade(),
                venda.getStatus(),
                venda.getDataHora()
        );
    }

    public Long getId() { return id; }
    public String getProdutoNome() { return produtoNome; }
    public String getClienteNome() { return clienteNome; }
    public String getVendedorUsername() { return vendedorUsername; }
    public int getQuantidade() { return quantidade; }
    public StatusVenda getStatus() { return status; }
    public LocalDateTime getDataHora() { return dataHora; }
}
