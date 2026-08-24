package com.matheusramalho.Autenticacao.controller;

import com.matheusramalho.Autenticacao.dto.AtualizarStatusDTO;
import com.matheusramalho.Autenticacao.dto.VendaRequestDTO;
import com.matheusramalho.Autenticacao.dto.VendaResponseDTO;
import com.matheusramalho.Autenticacao.security.UsuarioDetails;
import com.matheusramalho.Autenticacao.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService){
        this.vendaService = vendaService;
    }

    @PostMapping("/vendas")
    public ResponseEntity<VendaResponseDTO> lancar(
            @Valid @RequestBody VendaRequestDTO dto,
            @AuthenticationPrincipal UsuarioDetails principal
            ) {
        VendaResponseDTO criada = vendaService.lancar(dto, principal.getUsuario());
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping("/pedidos")
    public List<VendaResponseDTO> listarPedidos(@AuthenticationPrincipal UsuarioDetails principal) {
        return vendaService.listarPedidos(principal.getUsuario());
    }

    @PutMapping("/pedidos/{id}/status")
    public VendaResponseDTO atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusDTO dto,
            @AuthenticationPrincipal UsuarioDetails principal
            ) {
        return vendaService.atualizarStatus(id, dto.getStatus(), principal.getUsuario());
    }
}
