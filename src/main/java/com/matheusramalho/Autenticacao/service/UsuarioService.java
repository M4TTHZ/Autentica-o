package com.matheusramalho.Autenticacao.service;

import com.matheusramalho.Autenticacao.dto.UsuarioResponseDTO;
import com.matheusramalho.Autenticacao.exception.RecursoNaoEncontradoException;
import com.matheusramalho.Autenticacao.model.Acao;
import com.matheusramalho.Autenticacao.model.Papel;
import com.matheusramalho.Autenticacao.model.TipoEvento;
import com.matheusramalho.Autenticacao.model.Usuario;
import com.matheusramalho.Autenticacao.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Gestao de contas de login. Toda acao aqui exige Acao.GERENCIAR_USUARIOS,
 * que so o papel ADMIN possui (ver PermissaoService).
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PermissaoService permissaoService;
    private final LogEventoService logEventoService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PermissaoService permissaoService,
            LogEventoService logEventoService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.permissaoService = permissaoService;
        this.logEventoService = logEventoService;
    }

    public List<UsuarioResponseDTO> listar(Usuario usuarioLogado) {
        permissaoService.verificarPermissao(usuarioLogado, Acao.GERENCIAR_USUARIOS);

        return usuarioRepository.findAll().stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    public UsuarioResponseDTO desbloquear(Long id, Usuario usuarioLogado) {
        permissaoService.verificarPermissao(usuarioLogado, Acao.GERENCIAR_USUARIOS);

        Usuario usuario = buscarOuFalhar(id);
        usuario.setTentativasFalhas(0);
        usuario.setBloaueadoAte(null);
        usuarioRepository.save(usuario);

        logEventoService.registrar(TipoEvento.CONTA_DESBLOQUEADA, usuario.getUsername(),
                "desbloqueado manualmente por " + usuarioLogado.getUsername());

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    public UsuarioResponseDTO alterarPapel(Long id, Papel novoPapel, Usuario usuarioLogado) {
        permissaoService.verificarPermissao(usuarioLogado, Acao.GERENCIAR_USUARIOS);

        Usuario usuario = buscarOuFalhar(id);
        Papel papelAnterior = usuario.getPapel();
        usuario.setPapel(novoPapel);
        usuarioRepository.save(usuario);

        logEventoService.registrar(TipoEvento.PAPEL_ALTERADO, usuario.getUsername(),
                papelAnterior + " -> " + novoPapel + " (por " + usuarioLogado.getUsername() + ")");

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    private Usuario buscarOuFalhar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado."));
    }
}
