package com.example.lite_erp.entities.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDTO(@Schema(description = "nomeUsuario", example = "admin")String nomeUsuario, @Schema(description = "senha", example = "123")String senha) {
}
