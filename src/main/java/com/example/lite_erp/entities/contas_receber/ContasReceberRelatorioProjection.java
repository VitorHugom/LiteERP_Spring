package com.example.lite_erp.entities.contas_receber;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ContasReceberRelatorioProjection {
    LocalDate getDataVencimento();
    BigDecimal getValorTotalParcelas();
    Long getQtdParcelas();
    String getIdsParcelas();
}
