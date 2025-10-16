package com.example.lite_erp.entities.recuperacao_senha;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para solicitar recuperação de senha
 */
@Schema(description = "Dados para solicitar recuperação de senha")
public record SolicitarRecuperacaoDTO(
    
    @NotBlank(message = "Nome de usuário é obrigatório")
    @Schema(description = "Nome de usuário", example = "joao.silva", required = true)
    String nomeUsuario
    
) {}

