package com.example.lite_erp.entities.fluxo_caixa;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO de resposta para saldo de conta")
public record SaldoContaResponseDTO(
        @Schema(description = "ID da conta de caixa", example = "1")
        Long contaCaixaId,

        @Schema(description = "Descrição da conta de caixa", example = "Caixa Principal")
        String contaCaixaDescricao,

        @Schema(description = "Saldo atual da conta", example = "1500.50")
        BigDecimal saldoAtual,

        @Schema(description = "Total de entradas", example = "5000.00")
        BigDecimal totalEntradas,

        @Schema(description = "Total de saídas", example = "3500.50")
        BigDecimal totalSaidas
) {}
