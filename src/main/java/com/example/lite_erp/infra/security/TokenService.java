package com.example.lite_erp.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.lite_erp.entities.usuario.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    public String generateToken(Usuario usuario) {
        try {
            logger.info("Iniciando a geração de token para o usuário: {}", usuario.getNomeUsuario());

            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(usuario.getNomeUsuario())
                    .withClaim("role", usuario.getRole())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);

            logger.info("Token gerado com sucesso para o usuário: {}", usuario.getNomeUsuario());
            return token;
        } catch (JWTCreationException exception) {
            logger.error("Erro ao gerar o token para o usuário: {} - Erro: {}", usuario.getNomeUsuario(), exception.getMessage());
            throw new RuntimeException("Erro ao autenticar", exception);
        }
    }

    public String validateToken(String token) {
        try {
            logger.info("Iniciando a validação do token.");

            Algorithm algorithm = Algorithm.HMAC256(secret);
            String subject = JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

            logger.info("Token validado com sucesso. Usuário autenticado: {}", subject);
            return subject;
        } catch (JWTVerificationException exception) {
            logger.error("Erro ao validar o token: {} - Erro: {}", token, exception.getMessage());
            return null;
        }
    }

    private Instant generateExpirationDate() {
        Instant expirationDate = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
        logger.info("Data de expiração do token gerada: {}", expirationDate);
        return expirationDate;
    }
}
