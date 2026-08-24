package com.matheusramalho.Autenticacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Gera o hash de senha. O BCrypt cria um salt aleatorio a cada
     * chamada de encode() e o embute no proprio hash resultante -- por isso
     * a entidade Usuario nao tem uma coluna "salt" separada.
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
    }

    /**
     * Define quais endpoints exigem autenticacao. /cadastro e /login
     * precisam ficar publicos porque sao o proprio mecanismo de entrada
     * no sistema -- exigir login para logar seria um paradoxo.
     *
     * Todo o resto do RBAC (quem pode fazer o que DEPOIS de autenticado)
     * NAO fica aqui -- fica no PermissaoService, checado manualmente nos
     * controllers/services, por decisao de design (ver planejamento).
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/cadastro", "/login").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {})
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

}
