package com.matheusramalho.Autenticacao.dto;

import com.matheusramalho.Autenticacao.model.StatusVenda;
import jakarta.validation.constraints.NotNull;

public class AtualizarStatusDTO {

    @NotNull(message = "status e obrigatorio")
    private StatusVenda status;

    public AtualizarStatusDTO() {}

    public StatusVenda getStatus() { return status; }
    public void setStatus(StatusVenda status) { this.status = status; }
}
