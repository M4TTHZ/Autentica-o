package com.matheusramalho.Autenticacao.controller;

import com.matheusramalho.Autenticacao.dto.CadastroRequestDTO;
import com.matheusramalho.Autenticacao.dto.UsuarioResponseDTO;
import com.matheusramalho.Autenticacao.model.Usuario;
import com.matheusramalho.Autenticacao.security.UsuarioDetails;
import com.matheusramalho.Autenticacao.service.CadastroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService){
        this.cadastroService = cadastroService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @Valid @RequestBody CadastroRequestDTO dto,
            @AuthenticationPrincipal UsuarioDetails principal
            ) {
        // se veio Basic Auth valido junto (ex: um ADMIN criando VENDEDOR),
        // principal nao e nulo; se ninguem estiver logado, e null

        Usuario usuarioAutenticado = principal != null ? principal.getUsuario() : null;

        UsuarioResponseDTO criado = cadastroService.cadastrar(dto, usuarioAutenticado);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);

    }

}
