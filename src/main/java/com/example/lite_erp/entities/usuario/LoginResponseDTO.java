package com.example.lite_erp.entities.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(@Schema(description = "nome", example = "example") String nome, @Schema(description = "token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String token) {
}
