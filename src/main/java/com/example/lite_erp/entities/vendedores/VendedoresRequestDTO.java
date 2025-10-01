package com.example.lite_erp.entities.vendedores;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VendedoresRequestDTO(String nome, String email, String telefone) {
}
