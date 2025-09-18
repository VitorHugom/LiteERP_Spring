package com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa;

import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO para filtros de movimentação de caixa")
public record MovimentacaoCaixaFiltroDTO(
        @Schema(description = "ID da conta de caixa", example = "1")
        Long contaCaixaId,

        @Schema(description = "ID do tipo de movimentação", example = "1")
        Long tipoMovimentacaoId,

        @Schema(description = "Categoria da movimentação", example = "RECEITA")
        TipoMovimentacao.CategoriaMovimentacao categoria,

        @Schema(description = "ID do centro de custo", example = "1")
        Long centroCustoId,

        @Schema(description = "Origem da movimentação", example = "MANUAL")
        MovimentacaoCaixa.TipoOrigem tipoOrigem,

        @Schema(description = "Data inicial", example = "2024-01-01")
        LocalDate dataInicio,

        @Schema(description = "Data final", example = "2024-01-31")
        LocalDate dataFim,

        @Schema(description = "Valor mínimo", example = "100.00")
        BigDecimal valorMinimo,

        @Schema(description = "Valor máximo", example = "5000.00")
        BigDecimal valorMaximo,

        @Schema(description = "Status da movimentação", example = "CONFIRMADO")
        MovimentacaoCaixa.StatusMovimentacao status,

        @Schema(description = "ID do usuário responsável", example = "1")
        Long usuarioResponsavelId,

        @Schema(description = "Descrição para busca", example = "venda")
        String descricao
) {}
