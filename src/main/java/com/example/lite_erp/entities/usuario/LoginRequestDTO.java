package com.example.lite_erp.entities.usuario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginRequestDTO(@Schema(description = "nomeUsuario", example = "admin")String nomeUsuario, @Schema(description = "senha", example = "123")String senha) {
}
