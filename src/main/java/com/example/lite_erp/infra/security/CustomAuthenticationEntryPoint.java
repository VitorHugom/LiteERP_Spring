package com.example.lite_erp.infra.security;

import com.example.lite_erp.infra.exceptions.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Entry point customizado para tratar erros de autenticação
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, 
                        AuthenticationException authException) throws IOException, ServletException {
        
        logger.error("Erro de autenticação: {} - URI: {}", authException.getMessage(), request.getRequestURI());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.UNAUTHORIZED.value(),
            "AUTHENTICATION_REQUIRED",
            "Autenticação necessária para acessar este recurso",
            "Forneça um token JWT válido no header Authorization",
            request.getRequestURI()
        );
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
