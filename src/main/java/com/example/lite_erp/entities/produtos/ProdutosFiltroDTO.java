package com.example.lite_erp.entities.produtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutosFiltroDTO(
        LocalDate dataCompraInicio,
        LocalDate dataCompraFim,
        Long grupoId,
        BigDecimal precoVendaInicio,
        BigDecimal precoVendaFim,
        BigDecimal precoCompraInicio,
        BigDecimal precoCompraFim,
        BigDecimal pesoInicio,
        BigDecimal pesoFim
) {
}
