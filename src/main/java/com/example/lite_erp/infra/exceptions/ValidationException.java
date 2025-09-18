package com.example.lite_erp.infra.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando há erro de validação de dados
 */
public class ValidationException extends BaseException {
    
    private static final String DEFAULT_ERROR_CODE = "VALIDATION_ERROR";
    
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, DEFAULT_ERROR_CODE);
    }
    
    public ValidationException(String message, String errorCode) {
        super(message, HttpStatus.BAD_REQUEST, errorCode);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST, DEFAULT_ERROR_CODE);
    }
    
    // Métodos de conveniência para validações específicas
    public static ValidationException campoObrigatorio(String campo) {
        return new ValidationException(
            String.format("O campo '%s' é obrigatório", campo),
            "CAMPO_OBRIGATORIO"
        );
    }
    
    public static ValidationException valorInvalido(String campo, Object valor) {
        return new ValidationException(
            String.format("Valor inválido para o campo '%s': %s", campo, valor),
            "VALOR_INVALIDO"
        );
    }
    
    public static ValidationException formatoInvalido(String campo) {
        return new ValidationException(
            String.format("Formato inválido para o campo '%s'", campo),
            "FORMATO_INVALIDO"
        );
    }
    
    public static ValidationException estoqueInsuficiente(String produto, Object qtdDisponivel) {
        return new ValidationException(
            String.format("Estoque insuficiente para o produto '%s'. Quantidade disponível: %s", produto, qtdDisponivel),
            "ESTOQUE_INSUFICIENTE"
        );
    }
}
