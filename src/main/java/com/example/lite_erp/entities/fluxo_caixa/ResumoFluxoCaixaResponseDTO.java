package com.example.lite_erp.entities.fluxo_caixa;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO de resposta para resumo do fluxo de caixa")
public record ResumoFluxoCaixaResponseDTO(
        @Schema(description = "Data de referência", example = "2024-01-15")
        LocalDate dataReferencia,

        @Schema(description = "Total de receitas", example = "5000.00")
        BigDecimal totalReceitas,

        @Schema(description = "Total de despesas", example = "3500.50")
        BigDecimal totalDespesas,

        @Schema(description = "Saldo do período", example = "1499.50")
        BigDecimal saldoPeriodo,

        @Schema(description = "Quantidade de movimentações", example = "25")
        Long quantidadeMovimentacoes
) {}
