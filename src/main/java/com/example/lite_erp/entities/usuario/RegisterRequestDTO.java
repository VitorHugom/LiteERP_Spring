package com.example.lite_erp.entities.usuario;

import lombok.ToString;

public record RegisterRequestDTO(String nome_usuario, String email, String senha, Integer categoria_id) {
}
