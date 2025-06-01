package com.example.lite_erp.entities.pedidos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidosFiltroDTO(
        Long idCliente,
        Long idVendedor,
        LocalDateTime dataEmissao,
        BigDecimal valorTotal,
        String status,
        Long idTipoCobranca
) {
}
