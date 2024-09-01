package com.example.lite_erp.entities.produtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutosRequestDTO(String descricao, Long grupoProdutos, String marca, LocalDate dataUltimaCompra, BigDecimal precoCompra, BigDecimal precoVenda, BigDecimal peso, String codEan, String codNcm, String codCest) {

}
