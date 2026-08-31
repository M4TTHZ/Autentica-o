package com.matheusramalho.Autenticacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Gera o hash de senha (B1/B3). O BCrypt cria um salt aleatorio a cada
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
    /**
     * Libera o front-end (arquivo HTML aberto separado, em outra origem)
     * a chamar essa API. Sem isso, o navegador bloqueia a resposta antes
     * mesmo dela chegar no JavaScript -- a tela de login "trava" sem
     * erro nenhum aparecer, porque o fetch nem completa.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/cadastro", "/login").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {})
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
