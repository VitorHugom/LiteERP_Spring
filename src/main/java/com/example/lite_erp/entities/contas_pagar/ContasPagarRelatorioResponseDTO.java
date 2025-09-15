package com.example.lite_erp.entities.contas_pagar;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ContasPagarRelatorioResponseDTO(
        @Schema(description = "Data de vencimento das contas a pagar", example = "2025-02-01")
        LocalDate dataVencimento,

        @Schema(description = "Valor total das parcelas que vencem nesta data", example = "3750.00")
        BigDecimal valorTotalParcelas,

        @Schema(description = "Quantidade de parcelas que vencem nesta data", example = "2")
        Long qtdParcelas,

        @Schema(description = "Lista com os IDs das parcelas que vencem nesta data", example = "[10, 12]")
        List<Long> idsParcelas
) {}
