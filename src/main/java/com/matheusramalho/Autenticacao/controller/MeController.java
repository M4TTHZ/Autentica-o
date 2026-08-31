package com.matheusramalho.Autenticacao.controller;

import com.matheusramalho.Autenticacao.dto.UsuarioResponseDTO;
import com.matheusramalho.Autenticacao.security.UsuarioDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    @GetMapping("/me")
    public UsuarioResponseDTO me(@AuthenticationPrincipal UsuarioDetails principal) {
        return UsuarioResponseDTO.fromEntity(principal.getUsuario());
    }
}