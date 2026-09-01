package com.matheusramalho.Autenticacao.dto;

public class MfaSetupResponseDTO {

    private final String qrCodeBase64;

    public MfaSetupResponseDTO(String qrCodeBase64) {
        this.qrCodeBase64 = qrCodeBase64;
    }

    public String getQrCodeBase64() { return qrCodeBase64; }
}