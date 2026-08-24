package com.matheusramalho.Autenticacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProdutoRequestDTO {

    @NotBlank(message = "nome e obrigatorio")
    private String nome;

    @NotNull(message = "precoVenda e obrigatorio")
    @Positive(message = "precoVenda precisa ser maior que zero")
    private BigDecimal precoVenda;

    @NotNull(message = "custo e obrigatorio")
    @Positive(message = "custo precisa ser maior que zero")
    private BigDecimal custo;

    public ProdutoRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }
    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }
}
