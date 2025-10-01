package com.example.lite_erp.entities.itens_pedido;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItensPedidoRequestDTO(Long idPedido, Long idProduto, BigDecimal preco, Integer quantidade) {
}
