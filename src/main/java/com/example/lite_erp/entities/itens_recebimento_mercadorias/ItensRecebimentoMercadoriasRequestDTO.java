package com.example.lite_erp.entities.itens_recebimento_mercadorias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItensRecebimentoMercadoriasRequestDTO(Integer idRecebimento, Long idProduto, BigDecimal quantidade, BigDecimal valorUnitario) {
}
