package com.example.lite_erp.infra.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando há problemas de autenticação
 */
public class AuthenticationException extends BaseException {
    
    private static final String DEFAULT_ERROR_CODE = "AUTHENTICATION_ERROR";
    
    public AuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, DEFAULT_ERROR_CODE);
    }
    
    public AuthenticationException(String message, String errorCode) {
        super(message, HttpStatus.UNAUTHORIZED, errorCode);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause, HttpStatus.UNAUTHORIZED, DEFAULT_ERROR_CODE);
    }
    
    // Métodos de conveniência para problemas de autenticação específicos
    public static AuthenticationException tokenInvalido() {
        return new AuthenticationException(
            "Token de autenticação inválido ou expirado",
            "TOKEN_INVALIDO"
        );
    }
    
    public static AuthenticationException tokenExpirado() {
        return new AuthenticationException(
            "Token de autenticação expirado",
            "TOKEN_EXPIRADO"
        );
    }
    
    public static AuthenticationException credenciaisInvalidas() {
        return new AuthenticationException(
            "Credenciais inválidas",
            "CREDENCIAIS_INVALIDAS"
        );
    }
    
    public static AuthenticationException acessoNegado() {
        return new AuthenticationException(
            "Acesso negado",
            "ACESSO_NEGADO"
        );
    }
}
