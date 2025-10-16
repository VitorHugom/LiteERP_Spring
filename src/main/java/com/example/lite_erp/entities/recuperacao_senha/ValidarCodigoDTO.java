package com.example.lite_erp.entities.recuperacao_senha;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para validar código de recuperação
 */
@Schema(description = "Dados para validar código de recuperação")
public record ValidarCodigoDTO(
    
    @NotBlank(message = "Nome de usuário é obrigatório")
    @Schema(description = "Nome de usuário", example = "joao.silva", required = true)
    String nomeUsuario,
    
    @NotBlank(message = "Código é obrigatório")
    @Size(min = 6, max = 6, message = "Código deve ter 6 dígitos")
    @Pattern(regexp = "\\d{6}", message = "Código deve conter apenas números")
    @Schema(description = "Código de 6 dígitos recebido por email", example = "123456", required = true)
    String codigo
    
) {}

