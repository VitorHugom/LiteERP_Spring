package com.example.lite_erp.entities.contas_receber;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContasReceberRequestDTO (Long id_cliente, String numeroDocumento, Integer parcela, BigDecimal valorParcela, BigDecimal valorTotal, Long idFormaPagamento, Long idTipoCobranca, LocalDate dataVencimento) {
}
