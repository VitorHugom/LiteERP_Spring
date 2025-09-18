package com.example.lite_erp.infra.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tratador global de exceções para toda a aplicação
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Trata exceções customizadas que estendem BaseException
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDTO> handleBaseException(BaseException ex, HttpServletRequest request) {
        logger.error("Erro de negócio: {} - Código: {}", ex.getMessage(), ex.getErrorCode(), ex);
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            ex.getHttpStatus().value(),
            ex.getErrorCode(),
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }
    
    /**
     * Trata erros de validação do Bean Validation (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logger.error("Erro de validação: {}", ex.getMessage());
        
        List<ErrorResponseDTO.ValidationErrorDTO> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::mapFieldError)
            .collect(Collectors.toList());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            "Dados inválidos fornecidos",
            validationErrors,
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Trata violações de constraint do Bean Validation
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        logger.error("Erro de constraint: {}", ex.getMessage());
        
        List<ErrorResponseDTO.ValidationErrorDTO> validationErrors = ex.getConstraintViolations()
            .stream()
            .map(this::mapConstraintViolation)
            .collect(Collectors.toList());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "CONSTRAINT_VIOLATION",
            "Violação de restrições de dados",
            validationErrors,
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Trata erros de acesso negado do Spring Security
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        logger.error("Acesso negado: {}", ex.getMessage());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.FORBIDDEN.value(),
            "ACCESS_DENIED",
            "Acesso negado. Você não tem permissão para acessar este recurso",
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    
    /**
     * Trata erros de integridade de dados (violação de chave estrangeira, etc.)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        logger.error("Erro de integridade de dados: {}", ex.getMessage(), ex);
        
        String message = "Erro de integridade de dados. Verifique se não há dependências que impedem esta operação";
        String errorCode = "DATA_INTEGRITY_VIOLATION";
        
        // Tentar identificar o tipo específico de violação
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("foreign key constraint")) {
                message = "Não é possível realizar esta operação pois existem registros dependentes";
                errorCode = "FOREIGN_KEY_VIOLATION";
            } else if (ex.getMessage().contains("unique constraint")) {
                message = "Já existe um registro com estes dados únicos";
                errorCode = "UNIQUE_CONSTRAINT_VIOLATION";
            }
        }
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.CONFLICT.value(),
            errorCode,
            message,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    
    /**
     * Trata erros de JSON malformado
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.error("Erro de parsing JSON: {}", ex.getMessage());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "MALFORMED_JSON",
            "Formato JSON inválido",
            "Verifique a sintaxe do JSON enviado",
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Trata erros de método HTTP não suportado
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        logger.error("Método HTTP não suportado: {}", ex.getMessage());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            "METHOD_NOT_ALLOWED",
            String.format("Método %s não é suportado para este endpoint", ex.getMethod()),
            String.format("Métodos suportados: %s", String.join(", ", ex.getSupportedMethods())),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }
    
    /**
     * Trata erros de parâmetro obrigatório ausente
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        logger.error("Parâmetro obrigatório ausente: {}", ex.getMessage());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "MISSING_PARAMETER",
            String.format("Parâmetro obrigatório '%s' está ausente", ex.getParameterName()),
            String.format("Tipo esperado: %s", ex.getParameterType()),
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Trata erros de tipo de argumento inválido
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        logger.error("Tipo de argumento inválido: {}", ex.getMessage());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "INVALID_ARGUMENT_TYPE",
            String.format("Valor inválido para o parâmetro '%s'", ex.getName()),
            String.format("Valor fornecido: '%s', Tipo esperado: %s", ex.getValue(), ex.getRequiredType().getSimpleName()),
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Trata erros de endpoint não encontrado
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        logger.error("Endpoint não encontrado: {}", ex.getMessage());
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.NOT_FOUND.value(),
            "ENDPOINT_NOT_FOUND",
            "Endpoint não encontrado",
            String.format("Não foi possível encontrar %s %s", ex.getHttpMethod(), ex.getRequestURL()),
            request.getRequestURI()
        );
        
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Trata todas as outras exceções não mapeadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Erro interno do servidor: {}", ex.getMessage(), ex);
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "INTERNAL_SERVER_ERROR",
            "Erro interno do servidor",
            "Entre em contato com o suporte técnico",
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    // Métodos auxiliares
    private ErrorResponseDTO.ValidationErrorDTO mapFieldError(FieldError fieldError) {
        return new ErrorResponseDTO.ValidationErrorDTO(
            fieldError.getField(),
            fieldError.getRejectedValue(),
            fieldError.getDefaultMessage()
        );
    }
    
    private ErrorResponseDTO.ValidationErrorDTO mapConstraintViolation(ConstraintViolation<?> violation) {
        String fieldName = violation.getPropertyPath().toString();
        return new ErrorResponseDTO.ValidationErrorDTO(
            fieldName,
            violation.getInvalidValue(),
            violation.getMessage()
        );
    }
}
