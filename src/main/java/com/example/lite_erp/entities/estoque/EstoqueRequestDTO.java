package com.example.lite_erp.entities.estoque;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EstoqueRequestDTO(Long idProduto, BigDecimal qtdEstoque) {
}
