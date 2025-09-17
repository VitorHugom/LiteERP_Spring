package com.example.lite_erp.infra.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO padronizado para respostas de erro
 */
@Schema(description = "Resposta padronizada para erros da API")
public record ErrorResponseDTO(
        
        @Schema(description = "Código de status HTTP", example = "400")
        int status,
        
        @Schema(description = "Código interno do erro", example = "VALIDATION_ERROR")
        String errorCode,
        
        @Schema(description = "Mensagem principal do erro", example = "Dados inválidos fornecidos")
        String message,
        
        @Schema(description = "Detalhes adicionais do erro", example = "O campo 'nome' é obrigatório")
        String details,
        
        @Schema(description = "Lista de erros de validação (quando aplicável)")
        List<ValidationErrorDTO> validationErrors,
        
        @Schema(description = "Timestamp do erro", example = "2024-01-15T10:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,
        
        @Schema(description = "Caminho da requisição que gerou o erro", example = "/api/produtos")
        String path
) {
    
    public ErrorResponseDTO(int status, String errorCode, String message, String path) {
        this(status, errorCode, message, null, null, LocalDateTime.now(), path);
    }
    
    public ErrorResponseDTO(int status, String errorCode, String message, String details, String path) {
        this(status, errorCode, message, details, null, LocalDateTime.now(), path);
    }
    
    public ErrorResponseDTO(int status, String errorCode, String message, List<ValidationErrorDTO> validationErrors, String path) {
        this(status, errorCode, message, null, validationErrors, LocalDateTime.now(), path);
    }
    
    /**
     * DTO para erros de validação específicos
     */
    @Schema(description = "Erro de validação específico de um campo")
    public record ValidationErrorDTO(
            @Schema(description = "Nome do campo com erro", example = "email")
            String field,
            
            @Schema(description = "Valor rejeitado", example = "email-invalido")
            Object rejectedValue,
            
            @Schema(description = "Mensagem do erro de validação", example = "Formato de email inválido")
            String message
    ) {}
}
