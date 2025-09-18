package com.example.lite_erp.infra.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando há violação de regras de negócio
 */
public class BusinessException extends BaseException {
    
    private static final String DEFAULT_ERROR_CODE = "BUSINESS_RULE_VIOLATION";
    
    public BusinessException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, DEFAULT_ERROR_CODE);
    }
    
    public BusinessException(String message, String errorCode) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, errorCode);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause, HttpStatus.UNPROCESSABLE_ENTITY, DEFAULT_ERROR_CODE);
    }
    
    // Métodos de conveniência para regras de negócio específicas
    public static BusinessException usuarioJaExiste(String nomeUsuario) {
        return new BusinessException(
            String.format("Usuário '%s' já existe no sistema", nomeUsuario),
            "USUARIO_JA_EXISTE"
        );
    }
    
    public static BusinessException usuarioNaoAutorizado(String nomeUsuario) {
        return new BusinessException(
            String.format("Usuário '%s' não está autorizado", nomeUsuario),
            "USUARIO_NAO_AUTORIZADO"
        );
    }
    
    public static BusinessException senhaIncorreta() {
        return new BusinessException(
            "Senha incorreta",
            "SENHA_INCORRETA"
        );
    }
    
    public static BusinessException operacaoNaoPermitida(String operacao) {
        return new BusinessException(
            String.format("Operação '%s' não é permitida", operacao),
            "OPERACAO_NAO_PERMITIDA"
        );
    }
    
    public static BusinessException statusInvalido(String status, String entidade) {
        return new BusinessException(
            String.format("Status '%s' inválido para %s", status, entidade),
            "STATUS_INVALIDO"
        );
    }
}
