package com.matheusramalho.Autenticacao.dto;

import com.matheusramalho.Autenticacao.model.Papel;
import com.matheusramalho.Autenticacao.model.Usuario;

/**
 * O que a API devolve sobre um Usuario. Existe justamente para NUNCA
 * deixar passwordHash ou mfaSecret vazarem em uma resposta JSON --
 * serializar a entidade Usuario direto seria um erro de seguranca.
 */
public class UsuarioResponseDTO {

    private final Long id;
    private final String username;
    private final Papel papel;
    private final boolean mfaAtivo;


    public UsuarioResponseDTO(Long id, String username, Papel papel, boolean mfaAtivo) {
       this.id = id;
       this.username = username;
       this.papel = papel;
       this.mfaAtivo = mfaAtivo;
    }

    public static UsuarioResponseDTO fromEntity(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPapel(),
                usuario.isMfaAtivo()
        );
    }

    public Long getId(){
        return id;
    }

    private String getUsername(){
        return username;
    }

    private Papel getPapel(){
        return papel;
    }

    private boolean isMfaAtivo(){
        return mfaAtivo;
    }

}
