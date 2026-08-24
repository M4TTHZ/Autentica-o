package com.matheusramalho.Autenticacao.service;

import com.matheusramalho.Autenticacao.dto.CadastroRequestDTO;
import com.matheusramalho.Autenticacao.dto.UsuarioResponseDTO;
import com.matheusramalho.Autenticacao.exception.AcessoNegadoException;
import com.matheusramalho.Autenticacao.exception.UsuarioDuplicadoException;
import com.matheusramalho.Autenticacao.model.Papel;
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
 * 3) se o banco estiver vazio, o primeiro usuario cadastrado vira ADMIN
 *    automaticamente, independente do papel pedido no corpo da requisicao
 * 4) caso contrario, criar papel VENDEDOR, FINANCEIRO ou ADMIN exige que
 *    quem esta fazendo a requisicao ja esteja autenticado E seja ADMIN
 */
@Service
public class CadastroService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordPolicyService passwordPolicyService;
    private final PasswordEncoder passwordEncoder;

    public CadastroService(
            UsuarioRepository usuarioRepository,
            PasswordPolicyService passwordPolicyService,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordPolicyService = passwordPolicyService;
        this.passwordEncoder = passwordEncoder;
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

        boolean bancoVazio = usuarioRepository.count() == 0;

        Papel papelFinal;
        if (bancoVazio) {
            // 3) primeiro usuario do sistema: forca ADMIN, ignora o que
            // foi enviado no corpo da requisicao
            papelFinal = Papel.ADMIN;
        } else {
            // 4) qualquer outro cadastro (VENDEDOR, FINANCEIRO ou ADMIN)
            // exige que quem esta chamando ja seja ADMIN autenticado
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
            papelFinal = dto.getPapel();
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername(dto.getUsername());
        novoUsuario.setPasswordHash(passwordEncoder.encode(dto.getSenha()));
        novoUsuario.setPapel(papelFinal);
        // tentativasFalhas, bloqueadoAte e mfaAtivo ja nascem com os
        // valores padrao definidos na entidade Usuario (0, null, false)

        Usuario salvo = usuarioRepository.save(novoUsuario);

        // TODO: quando LogEventoService existir, registrar aqui um
        // TipoEvento.USUARIO_CRIADO com o username e o papel atribuido

        return UsuarioResponseDTO.fromEntity(salvo);
    }
}
