package com.matheusramalho.Autenticacao.service;

import com.matheusramalho.Autenticacao.dto.CadastroRequestDTO;
import com.matheusramalho.Autenticacao.dto.UsuarioResponseDTO;
import com.matheusramalho.Autenticacao.exception.AcessoNegadoException;
import com.matheusramalho.Autenticacao.exception.UsuarioDuplicadoException;
import com.matheusramalho.Autenticacao.model.Papel;
import com.matheusramalho.Autenticacao.model.TipoEvento;
import com.matheusramalho.Autenticacao.model.Usuario;
import com.matheusramalho.Autenticacao.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Orquestra a criacao de contas de LOGIN (Usuario). Regras de negocio
 * consolidadas aqui, na ordem em que sao checadas:
 *
 * 1) senha precisa obedecer a politica (B1)
 * 2) username precisa ser unico
 * 3) criar qualquer papel (VENDEDOR, FINANCEIRO ou ADMIN) exige que quem
 *    esta fazendo a requisicao ja esteja autenticado E seja ADMIN --
 *    nao existe mais bootstrap automatico do primeiro usuario; a conta
 *    inicial de ADMIN vem do DataSeeder, que roda no startup da aplicacao
 */
@Service
public class CadastroService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordPolicyService passwordPolicyService;
    private final PasswordEncoder passwordEncoder;
    private final LogEventoService logEventoService;

    public CadastroService(
            UsuarioRepository usuarioRepository,
            PasswordPolicyService passwordPolicyService,
            PasswordEncoder passwordEncoder,
            LogEventoService logEventoService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordPolicyService = passwordPolicyService;
        this.passwordEncoder = passwordEncoder;
        this.logEventoService = logEventoService;
    }

    /**
     * @param dto               dados vindos do corpo da requisicao
     * @param usuarioAutenticado o Usuario ja logado que esta fazendo a
     *                           chamada, ou null se a requisicao chegou
     *                           sem autenticacao (visitante anonimo)
     */
    public UsuarioResponseDTO cadastrar(CadastroRequestDTO dto, Usuario usuarioAutenticado) {

        // 1) politica de senha (B1) -- interrompe aqui se nao passar
        passwordPolicyService.validar(dto.getSenha());

        // 2) duplicidade de username
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new UsuarioDuplicadoException(
                    "Ja existe uma conta com o username '" + dto.getUsername() + "'."
            );
        }

        // 3) criar qualquer conta exige ADMIN autenticado
        if (usuarioAutenticado == null) {
            throw new AcessoNegadoException(
                    "E preciso estar autenticado como administrador para criar essa conta."
            );
        }
        if (usuarioAutenticado.getPapel() != Papel.ADMIN) {
            throw new AcessoNegadoException(
                    "Apenas administradores podem criar contas de vendedor, financeiro ou admin."
            );
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername(dto.getUsername());
        novoUsuario.setPasswordHash(passwordEncoder.encode(dto.getSenha()));
        novoUsuario.setPapel(dto.getPapel());
        // tentativasFalhas, bloqueadoAte e mfaAtivo ja nascem com os
        // valores padrao definidos na entidade Usuario (0, null, false)

        Usuario salvo = usuarioRepository.save(novoUsuario);

        logEventoService.registrar(TipoEvento.USUARIO_CRIADO, salvo.getUsername(),
                "papel=" + dto.getPapel() + " (criado por " + usuarioAutenticado.getUsername() + ")");

        return UsuarioResponseDTO.fromEntity(salvo);
    }
}
