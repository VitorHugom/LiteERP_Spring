package com.example.lite_erp.entities.pedidos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PedidosFiltroDTO(
        Long idCliente,
        Long idVendedor,
        LocalDate dataEmissaoInicio,
        LocalDate dataEmissaoFim,
        BigDecimal valorTotalInical,
        BigDecimal valorTotalFinal,
        String status,
        Long idTipoCobranca
) {
}
