package com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa;

import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "DTO de resposta para movimentação de caixa")
public record MovimentacaoCaixaResponseDTO(
        @Schema(description = "ID da movimentação", example = "1")
        Long id,

        @Schema(description = "ID da conta de caixa", example = "1")
        Long contaCaixaId,

        @Schema(description = "Descrição da conta de caixa", example = "Caixa Principal")
        String contaCaixaDescricao,

        @Schema(description = "ID do tipo de movimentação", example = "1")
        Long tipoMovimentacaoId,

        @Schema(description = "Descrição do tipo de movimentação", example = "Venda à Vista")
        String tipoMovimentacaoDescricao,

        @Schema(description = "Categoria da movimentação", example = "RECEITA")
        TipoMovimentacao.CategoriaMovimentacao categoria,

        @Schema(description = "Cor do tipo de movimentação", example = "#28a745")
        String corHex,

        @Schema(description = "ID do centro de custo", example = "1")
        Long centroCustoId,

        @Schema(description = "Descrição do centro de custo", example = "Vendas")
        String centroCustoDescricao,

        @Schema(description = "Origem da movimentação", example = "MANUAL")
        MovimentacaoCaixa.TipoOrigem tipoOrigem,

        @Schema(description = "ID de referência", example = "123")
        Long referenciaId,

        @Schema(description = "Número do documento", example = "DOC-001")
        String numeroDocumento,

        @Schema(description = "Descrição da movimentação", example = "Venda à vista")
        String descricao,

        @Schema(description = "Valor da movimentação", example = "1500.50")
        BigDecimal valor,

        @Schema(description = "Data da movimentação", example = "2024-01-15")
        LocalDate dataMovimentacao,

        @Schema(description = "Data e hora do lançamento")
        LocalDateTime dataLancamento,

        @Schema(description = "ID do usuário que fez o lançamento", example = "1")
        Long usuarioLancamentoId,

        @Schema(description = "Nome do usuário que fez o lançamento", example = "João Silva")
        String usuarioLancamentoNome,

        @Schema(description = "Observações adicionais", example = "Pagamento à vista com desconto")
        String observacoes,

        @Schema(description = "Status da movimentação", example = "CONFIRMADO")
        MovimentacaoCaixa.StatusMovimentacao status
) {
    public MovimentacaoCaixaResponseDTO(MovimentacaoCaixa movimentacao) {
        this(
                movimentacao.getId(),
                movimentacao.getContaCaixa().getId(),
                movimentacao.getContaCaixa().getDescricao(),
                movimentacao.getTipoMovimentacao().getId(),
                movimentacao.getTipoMovimentacao().getDescricao(),
                movimentacao.getTipoMovimentacao().getCategoria(),
                movimentacao.getTipoMovimentacao().getCorHex(),
                movimentacao.getCentroCusto() != null ? movimentacao.getCentroCusto().getId() : null,
                movimentacao.getCentroCusto() != null ? movimentacao.getCentroCusto().getDescricao() : null,
                movimentacao.getTipoOrigem(),
                movimentacao.getReferenciaId(),
                movimentacao.getNumeroDocumento(),
                movimentacao.getDescricao(),
                movimentacao.getValor(),
                movimentacao.getDataMovimentacao(),
                movimentacao.getDataLancamento(),
                movimentacao.getUsuarioLancamento().getId(),
                movimentacao.getUsuarioLancamento().getNomeUsuario(),
                movimentacao.getObservacoes(),
                movimentacao.getStatus()
        );
    }
}
