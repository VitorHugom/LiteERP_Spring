package com.example.lite_erp.entities.recuperacao_senha;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de resposta para operações de recuperação de senha
 */
@Schema(description = "Resposta de operações de recuperação de senha")
public record RecuperacaoSenhaResponseDTO(
    
    @Schema(description = "Mensagem de sucesso ou erro", example = "Código enviado para o email cadastrado")
    String mensagem,
    
    @Schema(description = "Token temporário (apenas após validação do código)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token,
    
    @Schema(description = "Email mascarado para onde o código foi enviado", example = "j***@gmail.com")
    String emailMascarado,
    
    @Schema(description = "Tempo de expiração em minutos", example = "15")
    Integer expiracaoMinutos
    
) {
    // Construtor para resposta de solicitação
    public RecuperacaoSenhaResponseDTO(String mensagem, String emailMascarado, Integer expiracaoMinutos) {
        this(mensagem, null, emailMascarado, expiracaoMinutos);
    }
    
    // Construtor para resposta de validação
    public RecuperacaoSenhaResponseDTO(String mensagem, String token) {
        this(mensagem, token, null, null);
    }
    
    // Construtor para resposta de redefinição
    public RecuperacaoSenhaResponseDTO(String mensagem) {
        this(mensagem, null, null, null);
    }
}

