package com.example.lite_erp.entities.contas_pagar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ContasPagarRequestDTO(
        Integer fornecedorId,
        String numeroDocumento,
        Integer parcela,
        BigDecimal valorParcela,
        BigDecimal valorTotal,
        LocalDate dataVencimento,
        Long formaPagamentoId,
        Long tipoCobrancaId,
        String status
) {}