package com.example.lite_erp.entities.fluxo_caixa;

import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixa;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "DTO de resposta para dashboard do fluxo de caixa")
public record DashboardFluxoCaixaResponseDTO(
        @Schema(description = "Data de referência do dashboard", example = "2024-01-15")
        LocalDate dataReferencia,

        @Schema(description = "Saldo total atual de todas as contas", example = "15000.50")
        BigDecimal saldoTotalAtual,

        @Schema(description = "Total de receitas do mês atual", example = "25000.00")
        BigDecimal receitasMesAtual,

        @Schema(description = "Total de despesas do mês atual", example = "18000.00")
        BigDecimal despesasMesAtual,

        @Schema(description = "Saldo do mês atual", example = "7000.00")
        BigDecimal saldoMesAtual,

        @Schema(description = "Total de receitas do mês anterior", example = "22000.00")
        BigDecimal receitasMesAnterior,

        @Schema(description = "Total de despesas do mês anterior", example = "16000.00")
        BigDecimal despesasMesAnterior,

        @Schema(description = "Percentual de variação das receitas", example = "13.64")
        BigDecimal percentualVariacaoReceitas,

        @Schema(description = "Percentual de variação das despesas", example = "12.50")
        BigDecimal percentualVariacaoDespesas,

        @Schema(description = "Quantidade de movimentações do mês", example = "45")
        Long quantidadeMovimentacoesMes,

        @Schema(description = "Conta com maior saldo")
        ContaResumoDTO contaMaiorSaldo,

        @Schema(description = "Resumo das últimas movimentações")
        List<MovimentacaoResumoDTO> ultimasMovimentacoes,

        @Schema(description = "Resumo por categoria de movimentação")
        List<CategoriaResumoDTO> resumoPorCategoria
) {

    @Schema(description = "Resumo de conta para dashboard")
    public record ContaResumoDTO(
            @Schema(description = "ID da conta", example = "1")
            Long id,

            @Schema(description = "Descrição da conta", example = "Caixa Principal")
            String descricao,

            @Schema(description = "Saldo atual", example = "5000.50")
            BigDecimal saldo,

            @Schema(description = "Tipo da conta", example = "CAIXA_FISICO")
            ContaCaixa.TipoConta tipo
    ) {}

    @Schema(description = "Resumo de movimentação para dashboard")
    public record MovimentacaoResumoDTO(
            @Schema(description = "ID da movimentação", example = "1")
            Long id,

            @Schema(description = "Descrição", example = "Venda à vista")
            String descricao,

            @Schema(description = "Valor", example = "1500.50")
            BigDecimal valor,

            @Schema(description = "Data da movimentação", example = "2024-01-15")
            LocalDate data,

            @Schema(description = "Categoria", example = "RECEITA")
            TipoMovimentacao.CategoriaMovimentacao categoria,

            @Schema(description = "Conta de caixa", example = "Caixa Principal")
            String contaCaixa
    ) {}

    @Schema(description = "Resumo por categoria para dashboard")
    public record CategoriaResumoDTO(
            @Schema(description = "Categoria", example = "RECEITA")
            TipoMovimentacao.CategoriaMovimentacao categoria,

            @Schema(description = "Total do período", example = "25000.00")
            BigDecimal total,

            @Schema(description = "Quantidade de movimentações", example = "15")
            Long quantidade,

            @Schema(description = "Percentual do total", example = "58.14")
            BigDecimal percentual
    ) {}
}
