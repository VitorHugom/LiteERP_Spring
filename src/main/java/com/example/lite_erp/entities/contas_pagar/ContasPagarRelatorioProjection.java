package com.example.lite_erp.entities.contas_pagar;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ContasPagarRelatorioProjection {
    LocalDate getDataVencimento();
    BigDecimal getValorTotalParcelas();
    Long getQtdParcelas();
    String getIdsParcelas();
}
