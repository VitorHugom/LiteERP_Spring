package com.example.lite_erp.entities.contas_receber;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContasReceberFiltroDTO(
        LocalDate dataRecebimentoInicio,
        LocalDate dataRecebimentoFim,
        Integer idCliente,
        Integer idTipoCobranca,
        Integer idFormaPagamento,
        BigDecimal valorTotalInicial,
        BigDecimal valorTotalFinal
) { }
