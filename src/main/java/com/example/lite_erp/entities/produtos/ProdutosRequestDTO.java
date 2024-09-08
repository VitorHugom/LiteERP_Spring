package com.example.lite_erp.entities.produtos;

import com.example.lite_erp.entities.grupo_produto.GrupoProdutos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutosRequestDTO(String descricao, GrupoProdutos grupoProdutos, String marca, LocalDate dataUltimaCompra, BigDecimal precoCompra, BigDecimal precoVenda, BigDecimal peso, String codEan, String codNcm, String codCest) {

}
