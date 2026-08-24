package com.matheusramalho.Autenticacao.security;

import com.matheusramalho.Autenticacao.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 * "Traducao" do nosso Usuario para o formato que o Spring Security usa
 * internamente para validar o Basic Auth. Guarda o Usuario original para
 * os controllers poderem pegar de volta (getUsuario()).
 */
public class UsuarioDetails implements UserDetails {

    private final Usuario usuario;

    public UsuarioDetails(Usuario usuario){
        this.usuario = usuario;
    }

    public Usuario getUsuario(){
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_"+ usuario.getPapel().name()));
    }

    @Override
    public String getPassword(){
        return usuario.getPasswordHash();
    }

    @Override
    public String getUsername(){
        return usuario.getUsername();
    }

    @Override
    public boolean isAccountNonLocked(){
        // reaproveita a mesma regra de bloqueio do B3 aqui tambem
        return usuario.getBloaueadoAte() == null
                || usuario.getBloaueadoAte().isBefore(java.time.LocalDateTime.now());
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }



}
