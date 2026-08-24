package com.matheusramalho.Autenticacao.security;

import com.matheusramalho.Autenticacao.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioDetails loadUserByUsername(String username){
        return usuarioRepository.findByUsername(username)
                .map(UsuarioDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Cade suas credenciais"));
    }

}
