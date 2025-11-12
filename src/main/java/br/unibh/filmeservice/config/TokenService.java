package br.unibh.filmeservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TokenService {

    @Value("${jwt.token.secret}")
    private String secret;

    public ValidationResult validateAndExtractClaims(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token);

        String subject = decodedJWT.getSubject();
        String role = decodedJWT.getClaim("role").asString();

        List<GrantedAuthority> authorities;
        if (role != null && !role.isBlank()) {
            authorities = List.of(new SimpleGrantedAuthority(role));
        } else {
            authorities = Collections.emptyList();
        }

        return new ValidationResult(subject, authorities);
    }

    public record ValidationResult(String getSubject, List<GrantedAuthority> getAuthorities) {}
}
