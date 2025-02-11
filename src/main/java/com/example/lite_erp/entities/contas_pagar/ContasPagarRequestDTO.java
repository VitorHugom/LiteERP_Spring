package com.example.lite_erp.entities.contas_pagar;

import java.math.BigDecimal;
import java.time.LocalDate;

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