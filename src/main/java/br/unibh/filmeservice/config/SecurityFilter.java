package br.unibh.filmeservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    private final TokenService tokenService;

    public SecurityFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        var token = this.recoverToken(request);

        if (token != null) {
            try {
                var validationResult = tokenService.validateAndExtractClaims(token);
                String subject = validationResult.getSubject();
                var authorities = validationResult.getAuthorities();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("Usuário '{}' autenticado com sucesso com as permissões: {}", subject, authorities);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                logger.warn("Falha na validação do token JWT: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7); // Remove o prefixo "Bearer "
    }
}
