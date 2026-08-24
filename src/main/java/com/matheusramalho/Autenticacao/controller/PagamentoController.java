package com.matheusramalho.Autenticacao.controller;

import com.matheusramalho.Autenticacao.dto.PagamentoResponseDTO;
import com.matheusramalho.Autenticacao.security.UsuarioDetails;
import com.matheusramalho.Autenticacao.service.VendaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PagamentoController {

    private final VendaService vendaService;

    public PagamentoController(VendaService vendaService){
        this.vendaService = vendaService;
    }

    @GetMapping("/pagamentos")
    public List<PagamentoResponseDTO> listar(@AuthenticationPrincipal UsuarioDetails principal){
        // VendaService.listarEntidades ja checa Acao.VER_PAGAMENTO internamente
        return vendaService.listarEntidades(principal.getUsuario()).stream()
                .map(PagamentoResponseDTO::fromVenda)
                .toList();
    }
}
