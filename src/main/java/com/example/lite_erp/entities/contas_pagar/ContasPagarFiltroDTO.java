package com.example.lite_erp.entities.contas_pagar;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContasPagarFiltroDTO(
        LocalDate dataVencimentoInicio,
        LocalDate dataVencimentoFim,
        Long idFornecedor,
        Integer idTipoCobranca,
        Integer idFormaPagamento,
        BigDecimal valorTotalInicial,
        BigDecimal valorTotalFinal
) { }
