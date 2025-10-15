package br.unibh.filmeservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de segurança que intercepta todas as requisições para validar o token JWT.
 * Este filtro é executado uma vez por requisição.
 */
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

        // 1. Tenta recuperar o token do cabeçalho da requisição
        var token = this.recoverToken(request);

        if (token != null) {
            try {
                // 2. Valida o token e extrai o "subject" (ex: email do usuário) e as "roles"
                var validationResult = tokenService.validateAndExtractClaims(token);
                String subject = validationResult.getSubject();
                var authorities = validationResult.getAuthorities();

                // 3. Cria o objeto de autenticação para o Spring Security
                // O "principal" é o identificador do usuário (subject)
                // As "credentials" (credenciais) são nulas, pois estamos usando token
                // As "authorities" são as permissões/roles do usuário
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);

                // 4. Define o objeto de autenticação no contexto de segurança do Spring
                // A partir daqui, o Spring considera o usuário como autenticado para esta requisição
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("Usuário '{}' autenticado com sucesso com as permissões: {}", subject, authorities);

            } catch (Exception e) {
                // Se a validação do token falhar (expirado, assinatura inválida, etc.)
                // Apenas limpamos o contexto e seguimos em frente. O usuário não será autenticado.
                SecurityContextHolder.clearContext();
                logger.warn("Falha na validação do token JWT: {}", e.getMessage());
            }
        }

        // 5. Continua a cadeia de filtros. Essencial para que a requisição chegue ao controller.
        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token do cabeçalho "Authorization".
     * O formato esperado é "Bearer <token>".
     *
     * @param request A requisição HTTP
     * @return O token JWT como String, ou null se não for encontrado.
     */
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7); // Remove o prefixo "Bearer "
    }
}
