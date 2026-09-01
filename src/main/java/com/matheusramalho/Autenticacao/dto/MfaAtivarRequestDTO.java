package com.matheusramalho.Autenticacao.dto;

import jakarta.validation.constraints.NotBlank;

public class MfaAtivarRequestDTO {

    @NotBlank(message = "codigoTotp e obrigatorio")
    private String codigoTotp;

    public MfaAtivarRequestDTO() {}

    public String getCodigoTotp() { return codigoTotp; }
    public void setCodigoTotp(String codigoTotp) { this.codigoTotp = codigoTotp; }
}