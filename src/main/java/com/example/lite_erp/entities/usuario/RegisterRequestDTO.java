package com.example.lite_erp.entities.usuario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegisterRequestDTO(String nomeUsuario, String email, String senha, Integer categoria_id, String telefone) {
}
