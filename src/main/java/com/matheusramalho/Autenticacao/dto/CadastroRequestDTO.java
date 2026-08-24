package com.matheusramalho.Autenticacao.dto;


import com.matheusramalho.Autenticacao.model.Papel;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.parameters.P;

/**
 * Corpo esperado em POST /cadastro. A validacao de formato (campo vazio,
 * enum invalido) acontece aqui via Bean Validation, ANTES mesmo de chegar
 * no service -- validacao de REGRA DE NEGOCIO (politica de senha, quem
 * pode criar quem) fica no CadastroService, nao aqui.
 */
public class CadastroRequestDTO {

    @NotBlank(message = "cade o username pazao")
    private String username;

    @NotBlank(message = "sem senha nao da")
    private String senha;

    @NotBlank(message = "bata tua funca ai")
    private Papel papel;

    public CadastroRequestDTO(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getSenha(){
        return senha;
    }

    public void setSenha(){
        this.senha = senha;
    }

    public Papel getPapel(){
        return papel;
    }

    public void setPapel(){
        this.papel = papel;
    }

}
