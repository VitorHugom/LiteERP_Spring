package com.example.lite_erp.infra.security;

import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.entities.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        logger.info("Token recuperado: {}", token); // Log do token recuperado

        var login = tokenService.validateToken(token);
        logger.info("Login associado ao token: {}", login); // Log do login extraído do token

        if (login != null) {
            Optional<Usuario> optionalUser = userRepository.findByEmail(login);
            if (optionalUser.isPresent()) {
                Usuario user = optionalUser.get();
                logger.info("Usuário encontrado: {}", user.getEmail()); // Log após encontrar o usuário

                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Autenticação configurada para o usuário: {}", user.getEmail());
                logger.info("Contexto de Segurança: {}", SecurityContextHolder.getContext().getAuthentication());
            } else {
                logger.warn("Usuário não encontrado para o login: {}", login); // Log se o usuário não for encontrado
            }
        } else {
            logger.warn("Token inválido ou não fornecido."); // Log se o token for inválido
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            logger.warn("Cabeçalho de autorização ausente"); // Log se o cabeçalho de autorização estiver ausente
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
