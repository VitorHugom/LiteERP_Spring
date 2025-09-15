package com.example.lite_erp.entities.contas_pagar;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ContasPagarRelatorioFiltroDTO(
        @Schema(description = "Data inicial do período para filtrar as contas a pagar", example = "2024-01-01")
        LocalDate dataInicio,

        @Schema(description = "Data final do período para filtrar as contas a pagar", example = "2024-12-31")
        LocalDate dataFim,

        @Schema(description = "Status das contas a excluir do relatório. Por padrão exclui contas com status 'paga'", example = "paga")
        String status,

        @Schema(description = "ID do fornecedor para filtrar as contas a pagar", example = "1")
        Long idFornecedor,

        @Schema(description = "ID da forma de pagamento para filtrar as contas a pagar", example = "2")
        Long idFormaPagamento
) {
}
