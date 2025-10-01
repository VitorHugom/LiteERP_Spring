package com.example.lite_erp.entities.contas_receber;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ContasReceberRequestDTO (Long idCliente, String numeroDocumento, Integer parcela, BigDecimal valorParcela, BigDecimal valorTotal, Long idFormaPagamento, Long idTipoCobranca, LocalDate dataVencimento, String status) {
}
