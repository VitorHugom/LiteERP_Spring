package com.example.lite_erp.entities.recuperacao_senha;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para redefinir senha
 */
@Schema(description = "Dados para redefinir senha")
public record RedefinirSenhaDTO(
    
    @NotBlank(message = "Token é obrigatório")
    @Schema(description = "Token temporário recebido após validação do código", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    String token,
    
    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Nova senha do usuário", example = "novaSenha123", required = true)
    String novaSenha
    
) {}

