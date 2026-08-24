package com.matheusramalho.Autenticacao.dto;

import com.matheusramalho.Autenticacao.model.StatusVenda;
import com.matheusramalho.Autenticacao.model.Venda;

import java.math.BigDecimal;

/**
 * Representa o dado de "pagamento" de uma venda. Nesse projeto academico,
 * o valor e derivado da propria Venda (precoVenda * quantidade) em vez de
 * ter uma entidade Pagamento separada -- decisao para manter o escopo
 * simples, ja que o foco e autenticacao/RBAC, nao um modulo financeiro.
 */
public class PagamentoResponseDTO {

    private final Long vendaId;
    private final BigDecimal valor;
    private final StatusVenda status;

    public PagamentoResponseDTO(Long vendaId, BigDecimal valor, StatusVenda status) {
        this.vendaId = vendaId;
        this.valor = valor;
        this.status = status;
    }

    public static PagamentoResponseDTO fromVenda(Venda venda) {
        BigDecimal valor = venda.getProduto().getPrecoVenda()
                .multiply(BigDecimal.valueOf(venda.getQuantidade()));
        return new PagamentoResponseDTO(venda.getId(), valor, venda.getStatus());
    }

    public Long getVendaId() { return vendaId; }
    public BigDecimal getValor() { return valor; }
    public StatusVenda getStatus() { return status; }
}
