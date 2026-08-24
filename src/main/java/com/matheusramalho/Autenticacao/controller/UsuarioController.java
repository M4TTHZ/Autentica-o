package com.matheusramalho.Autenticacao.controller;

import com.matheusramalho.Autenticacao.dto.AlterarPapelDTO;
import com.matheusramalho.Autenticacao.dto.UsuarioResponseDTO;
import com.matheusramalho.Autenticacao.security.UsuarioDetails;
import com.matheusramalho.Autenticacao.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar(@AuthenticationPrincipal UsuarioDetails principal) {
        return usuarioService.listar(principal.getUsuario());
    }

    @PutMapping("/{id}/desbloquear")
    public UsuarioResponseDTO desbloquear(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioDetails principal
    ) {
        return usuarioService.desbloquear(id, principal.getUsuario());
    }

    @PutMapping("/{id}/papel")
    public UsuarioResponseDTO alterarPapel(
            @PathVariable Long id,
            @Valid @RequestBody AlterarPapelDTO dto,
            @AuthenticationPrincipal UsuarioDetails principal
    ) {
        return usuarioService.alterarPapel(id, dto.getPapel(), principal.getUsuario());
    }
}
