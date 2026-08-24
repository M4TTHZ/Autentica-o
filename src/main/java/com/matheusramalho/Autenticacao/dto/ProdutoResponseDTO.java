package com.matheusramalho.Autenticacao.dto;

import com.matheusramalho.Autenticacao.model.Produto;

import java.math.BigDecimal;

/**
 * "custo" so vem preenchido quando quem pediu tem permissao para ver
 * margem (FINANCEIRO/ADMIN) -- ver ProdutoService.paraResposta().
 * Para VENDEDOR, o campo vem como null e nao aparece no JSON.
 */
public class ProdutoResponseDTO {

    private final Long id;
    private final String nome;
    private final BigDecimal precoVenda;
    private final BigDecimal custo;

    public ProdutoResponseDTO(Long id, String nome, BigDecimal precoVenda, BigDecimal custo) {
        this.id = id;
        this.nome = nome;
        this.precoVenda = precoVenda;
        this.custo = custo;
    }

    public static ProdutoResponseDTO fromEntity(Produto produto, boolean incluirCusto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getPrecoVenda(),
                incluirCusto ? produto.getCusto() : null
        );
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public BigDecimal getCusto() { return custo; }
}
