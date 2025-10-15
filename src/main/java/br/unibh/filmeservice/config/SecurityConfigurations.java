package br.unibh.filmeservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe principal de configuração de segurança do Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    /**
     * Define a cadeia de filtros de segurança que será aplicada a todas as requisições.
     * @param http O objeto HttpSecurity para configurar a segurança.
     * @return A cadeia de filtros de segurança configurada.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Desabilita a proteção CSRF, pois a API é stateless e usa tokens.
                .csrf(csrf -> csrf.disable())

                // 2. Configura a gestão de sessão como STATELESS. O servidor não guardará estado de sessão.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configura as regras de autorização para as requisições HTTP.
                .authorizeHttpRequests(authorize -> authorize
                        // Por padrão, exige autenticação para QUALQUER requisição.
                        // Se você tiver rotas públicas (ex: GET /movies), você as definiria aqui com .permitAll().
                        // Ex: .requestMatchers(HttpMethod.GET, "/movies").permitAll()
                        .requestMatchers(HttpMethod.GET, "/generos/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 4. Adiciona nosso filtro customizado (SecurityFilter) para ser executado ANTES do filtro padrão de autenticação.
                // Esta é a linha mais importante, que integra nossa lógica de validação de token.
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}
