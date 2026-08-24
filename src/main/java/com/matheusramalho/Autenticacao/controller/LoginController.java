package com.matheusramalho.Autenticacao.controller;


import com.matheusramalho.Autenticacao.dto.LoginRequestDTO;
import com.matheusramalho.Autenticacao.dto.UsuarioResponseDTO;
import com.matheusramalho.Autenticacao.model.Usuario;
import com.matheusramalho.Autenticacao.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping("/login")
    private ResponseEntity<UsuarioResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto){
        return ResponseEntity.ok(loginService.autenticar(dto));
    }
}
