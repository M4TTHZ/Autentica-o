package com.matheusramalho.Autenticacao.dto;

import com.matheusramalho.Autenticacao.model.Papel;
import jakarta.validation.constraints.NotNull;

public class AlterarPapelDTO {

    @NotNull(message = "papel e obrigatorio")
    private Papel papel;

    public AlterarPapelDTO() {}

    public Papel getPapel() { return papel; }
    public void setPapel(Papel papel) { this.papel = papel; }
}
