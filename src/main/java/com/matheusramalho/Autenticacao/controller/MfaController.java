package com.matheusramalho.Autenticacao.controller;

import com.matheusramalho.Autenticacao.dto.MfaAtivarRequestDTO;
import com.matheusramalho.Autenticacao.dto.MfaSetupResponseDTO;
import com.matheusramalho.Autenticacao.exception.AcessoNegadoException;
import com.matheusramalho.Autenticacao.exception.CredenciaisInvalidasException;
import com.matheusramalho.Autenticacao.model.Papel;
import com.matheusramalho.Autenticacao.model.Usuario;
import com.matheusramalho.Autenticacao.repository.UsuarioRepository;
import com.matheusramalho.Autenticacao.security.UsuarioDetails;
import com.matheusramalho.Autenticacao.service.LogEventoService;
import com.matheusramalho.Autenticacao.service.MfaService;
import com.matheusramalho.Autenticacao.model.TipoEvento;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mfa")
public class MfaController {

    private final MfaService mfaService;
    private final UsuarioRepository usuarioRepository;
    private final LogEventoService logEventoService;

    public MfaController(MfaService mfaService, UsuarioRepository usuarioRepository, LogEventoService logEventoService) {
        this.mfaService = mfaService;
        this.usuarioRepository = usuarioRepository;
        this.logEventoService = logEventoService;
    }

    @PostMapping("/setup")
    public MfaSetupResponseDTO setup(@AuthenticationPrincipal UsuarioDetails principal) {
        Usuario usuario = principal.getUsuario();

        if (usuario.getPapel() != Papel.ADMIN) {
            throw new AcessoNegadoException("MFA e exigido apenas para o papel ADMIN.");
        }

        String secret = mfaService.gerarNovoSecret();
        usuario.setMfaSecret(secret);
        usuarioRepository.save(usuario);

        String qrCodeBase64 = mfaService.gerarQrCodeBase64(usuario.getUsername(), secret);
        return new MfaSetupResponseDTO(qrCodeBase64);
    }

    @PostMapping("/ativar")
    public Map<String, String> ativar(
            @Valid @RequestBody MfaAtivarRequestDTO dto,
            @AuthenticationPrincipal UsuarioDetails principal
    ) {
        Usuario usuario = principal.getUsuario();

        if (!mfaService.verificarCodigo(usuario.getMfaSecret(), dto.getCodigoTotp())) {
            throw new CredenciaisInvalidasException("Codigo invalido.");
        }

        usuario.setMfaAtivo(true);
        usuarioRepository.save(usuario);

        logEventoService.registrar(TipoEvento.MFA_ATIVO, usuario.getUsername(), null);

        return Map.of("mensagem", "MFA ativado com sucesso.");
    }
}