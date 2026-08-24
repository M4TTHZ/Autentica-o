package com.matheusramalho.Autenticacao.service;

import com.matheusramalho.Autenticacao.dto.LoginRequestDTO;
import com.matheusramalho.Autenticacao.dto.UsuarioResponseDTO;
import com.matheusramalho.Autenticacao.exception.ContaBloqueadaException;
import com.matheusramalho.Autenticacao.exception.CredenciaisInvalidasException;
import com.matheusramalho.Autenticacao.exception.MfaRequeridoException;
import com.matheusramalho.Autenticacao.model.Papel;
import com.matheusramalho.Autenticacao.model.Usuario;
import com.matheusramalho.Autenticacao.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginService {

    private static final int MAX_TENTATIVAS = 0;
    private static final int MINUTOS_BLOQUEIO = 15;

    // Hash "de mentira" usado so para gastar o mesmo tempo de comparacao
    // quando o usuario nem existe (protecao contra timing attack).
    private static final String HASH_FALSO =
            "$2a$10$7EqJtq98hPqEX7fNZaFWoOe6O8x4mFqR8Q6XQxJ0K6r9c9vQe5f7C";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final MfaService mfaService;

    public LoginService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, MfaService mfaService){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.mfaService = mfaService;
    }

    public UsuarioResponseDTO autenticar(LoginRequestDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(dto.getUsername());

        // conta bloqueada? nem chega a comparar senha
        if (usuarioOpt.isPresent() && estaBloqueado(usuarioOpt.get())) {
            throw new ContaBloqueadaException(
                    "Conta bloqueada. Tente novamente mais tarde."
            );
        }

        // timing consistente: compara contra hash real se existe,
        // ou hash falso se nao existe -- sempre gasta um tempo parecido
        String hashParaComparar = usuarioOpt.map(Usuario::getPasswordHash).orElse(HASH_FALSO);
        boolean senhaCorreta = passwordEncoder.matches(dto.getSenha(), hashParaComparar);

        if (usuarioOpt.isEmpty() || !senhaCorreta) {
            usuarioOpt.ifPresent(this::registrarTentativaFalha);
            // mensagem sempre igual, nao importa o motivo (anti-enumeracao)
            throw new CredenciaisInvalidasException("Credenciais invalidas.");
        }

        Usuario usuario = usuarioOpt.get();

        // login e senha certos: zera o contador de tentativas
        usuario.setTentativasFalhas(0);
        usuario.setBloaueadoAte(null);

        // ADMIN precisa tambem do codigo TOTP (B6)
        if (usuario.getPapel() == Papel.ADMIN && usuario.isMfaAtivo()) {
            if (dto.getCodigoTotp() == null || dto.getCodigoTotp().isBlank()) {
                usuarioRepository.save(usuario);
                throw new MfaRequeridoException("Informe o codigo do app autenticador.");
            }
            if (!mfaService.verificarCodigo(usuario.getMfaSecret(), dto.getCodigoTotp())) {
                usuarioRepository.save(usuario);
                throw new CredenciaisInvalidasException("Credenciais invalidas.");
            }
        }

        usuarioRepository.save(usuario);

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    private boolean estaBloqueado(Usuario usuario){
        return usuario.getBloaueadoAte() != null
                && usuario.getBloaueadoAte().isAfter(LocalDateTime.now());
    }

    private void registrarTentativaFalha(Usuario usuario){
        int tetativas = usuario.getTentativasFalhas() +1;
        usuario.setTentativasFalhas(tetativas);

        if (tetativas >= MAX_TENTATIVAS){
            usuario.setBloaueadoAte(LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEIO));
        }

        usuarioRepository.save(usuario);
    }
}
