package com.example.lite_erp.infra.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um recurso não é encontrado
 */
public class ResourceNotFoundException extends BaseException {
    
    private static final String DEFAULT_ERROR_CODE = "RESOURCE_NOT_FOUND";
    
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, DEFAULT_ERROR_CODE);
    }
    
    public ResourceNotFoundException(String message, String errorCode) {
        super(message, HttpStatus.NOT_FOUND, errorCode);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.NOT_FOUND, DEFAULT_ERROR_CODE);
    }
    
    // Métodos de conveniência para recursos específicos
    public static ResourceNotFoundException produto(Long id) {
        return new ResourceNotFoundException(
            String.format("Produto com ID %d não encontrado", id),
            "PRODUTO_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException cliente(Long id) {
        return new ResourceNotFoundException(
            String.format("Cliente com ID %d não encontrado", id),
            "CLIENTE_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException fornecedor(Long id) {
        return new ResourceNotFoundException(
            String.format("Fornecedor com ID %d não encontrado", id),
            "FORNECEDOR_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException usuario(String nomeUsuario) {
        return new ResourceNotFoundException(
            String.format("Usuário '%s' não encontrado", nomeUsuario),
            "USUARIO_NOT_FOUND"
        );
    }
    
    public static ResourceNotFoundException pedido(Long id) {
        return new ResourceNotFoundException(
            String.format("Pedido com ID %d não encontrado", id),
            "PEDIDO_NOT_FOUND"
        );
    }
}
