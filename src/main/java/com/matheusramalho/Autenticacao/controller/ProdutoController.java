package com.matheusramalho.Autenticacao.controller;

import com.matheusramalho.Autenticacao.dto.ProdutoRequestDTO;
import com.matheusramalho.Autenticacao.dto.ProdutoResponseDTO;
import com.matheusramalho.Autenticacao.security.UsuarioDetails;
import com.matheusramalho.Autenticacao.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(
            @Valid @RequestBody ProdutoRequestDTO dto,
            @AuthenticationPrincipal UsuarioDetails principal
            ) {
        ProdutoResponseDTO criado = produtoService.criar(dto, principal.getUsuario());
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public List<ProdutoResponseDTO> listar(@AuthenticationPrincipal UsuarioDetails principal){
        return produtoService.listar(principal.getUsuario());
    }
}
