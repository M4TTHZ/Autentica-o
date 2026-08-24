package com.matheusramalho.Autenticacao.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timeStamp = LocalDateTime.now();

    // Username tentado/afetado. Pode ser nulo em eventos sem usuario associado.
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipoEvento;

    private String detalhe;

}
