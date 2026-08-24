package com.matheusramalho.Autenticacao.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "venda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(nullable = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false)
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    @Column(nullable = false)
    private int quantidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusVenda status = StatusVenda.PENDENTE;

    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

}
