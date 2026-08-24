package com.matheusramalho.Autenticacao.controller;

import com.matheusramalho.Autenticacao.dto.ClienteRequestDTO;
import com.matheusramalho.Autenticacao.dto.ClienteResponseDTO;
import com.matheusramalho.Autenticacao.security.UsuarioDetails;
import com.matheusramalho.Autenticacao.service.CadastroService;
import com.matheusramalho.Autenticacao.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(
            @Valid @RequestBody ClienteRequestDTO dto,
            @AuthenticationPrincipal UsuarioDetails principal
            ) {
        ClienteResponseDTO criado = clienteService.criar(dto, principal.getUsuario());
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public List<ClienteResponseDTO> listar(@AuthenticationPrincipal UsuarioDetails principal){
        return clienteService.listar(principal.getUsuario());
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO buscar(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioDetails pricipal
    ) {
        return clienteService.buscarPorId(id, pricipal.getUsuario());
    }
}
