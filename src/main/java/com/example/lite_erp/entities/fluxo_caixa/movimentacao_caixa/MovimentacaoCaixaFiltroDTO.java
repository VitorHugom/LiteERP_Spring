package com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa;

import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO para filtros de movimentação de caixa")
public record MovimentacaoCaixaFiltroDTO(
        @Schema(description = "ID da conta de caixa para filtrar as movimentações. Se não informado, busca em todas as contas acessíveis ao usuário", example = "1")
        Long contaCaixaId,

        @Schema(description = "ID do tipo de movimentação para filtrar (ex: Venda à Vista, Pagamento Fornecedor)", example = "1")
        Long tipoMovimentacaoId,

        @Schema(description = "Categoria da movimentação para filtrar<br/>" +
                "<br/><strong>Valores possíveis:</strong>" +
                "<ul>" +
                "<li><strong>RECEITA:</strong> Entradas de dinheiro</li>" +
                "<li><strong>DESPESA:</strong> Saídas de dinheiro</li>" +
                "<li><strong>TRANSFERENCIA:</strong> Transferências entre contas</li>" +
                "</ul>",
                example = "RECEITA")
        TipoMovimentacao.CategoriaMovimentacao categoria,

        @Schema(description = "ID do centro de custo para filtrar as movimentações", example = "1")
        Long centroCustoId,

        @Schema(description = "Origem da movimentação para filtrar<br/>" +
                "<br/><strong>Valores possíveis:</strong>" +
                "<ul>" +
                "<li><strong>MANUAL:</strong> Movimentações criadas manualmente</li>" +
                "<li><strong>CONTA_PAGAR:</strong> Movimentações geradas por pagamentos</li>" +
                "<li><strong>CONTA_RECEBER:</strong> Movimentações geradas por recebimentos</li>" +
                "<li><strong>TRANSFERENCIA:</strong> Transferências entre contas</li>" +
                "</ul>",
                example = "MANUAL")
        MovimentacaoCaixa.TipoOrigem tipoOrigem,

        @Schema(description = "Data inicial do período para filtrar as movimentações por data de movimentação", example = "2024-01-01")
        LocalDate dataInicio,

        @Schema(description = "Data final do período para filtrar as movimentações por data de movimentação", example = "2024-01-31")
        LocalDate dataFim,

        @Schema(description = "Valor mínimo para filtrar as movimentações (valores negativos representam saídas)", example = "100.00")
        BigDecimal valorMinimo,

        @Schema(description = "Valor máximo para filtrar as movimentações (valores negativos representam saídas)", example = "5000.00")
        BigDecimal valorMaximo,

        @Schema(description = "Status da movimentação para filtrar<br/>" +
                "<br/><strong>Valores possíveis:</strong>" +
                "<ul>" +
                "<li><strong>PENDENTE:</strong> Movimentações pendentes de confirmação</li>" +
                "<li><strong>CONFIRMADO:</strong> Movimentações confirmadas</li>" +
                "<li><strong>CANCELADO:</strong> Movimentações canceladas</li>" +
                "</ul>",
                example = "CONFIRMADO")
        MovimentacaoCaixa.StatusMovimentacao status,

        @Schema(description = "ID do usuário responsável pela conta de caixa para filtrar movimentações", example = "1")
        Long usuarioResponsavelId,

        @Schema(description = "Texto para busca na descrição da movimentação (busca parcial, ignora maiúsculas/minúsculas)", example = "venda")
        String descricao
) {}
