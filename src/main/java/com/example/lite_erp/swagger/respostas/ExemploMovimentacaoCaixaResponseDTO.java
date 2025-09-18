package com.example.lite_erp.swagger.respostas;

import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixa;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Exemplo de resposta para movimentação de caixa")
public class ExemploMovimentacaoCaixaResponseDTO {

    @Schema(description = "ID da movimentação", example = "1")
    private Long id;

    @Schema(description = "ID da conta de caixa", example = "1")
    private Long contaCaixaId;

    @Schema(description = "Descrição da conta de caixa", example = "Caixa Principal")
    private String contaCaixaDescricao;

    @Schema(description = "ID do tipo de movimentação", example = "1")
    private Long tipoMovimentacaoId;

    @Schema(description = "Descrição do tipo de movimentação", example = "Venda à Vista")
    private String tipoMovimentacaoDescricao;

    @Schema(description = "Categoria da movimentação", example = "RECEITA")
    private TipoMovimentacao.CategoriaMovimentacao categoria;

    @Schema(description = "Cor do tipo de movimentação", example = "#28a745")
    private String corHex;

    @Schema(description = "ID do centro de custo", example = "1")
    private Long centroCustoId;

    @Schema(description = "Descrição do centro de custo", example = "Vendas")
    private String centroCustoDescricao;

    @Schema(description = "Origem da movimentação", example = "MANUAL")
    private MovimentacaoCaixa.TipoOrigem tipoOrigem;

    @Schema(description = "ID de referência", example = "123")
    private Long referenciaId;

    @Schema(description = "Número do documento", example = "DOC-001")
    private String numeroDocumento;

    @Schema(description = "Descrição da movimentação", example = "Venda à vista")
    private String descricao;

    @Schema(description = "Valor da movimentação", example = "1500.50")
    private BigDecimal valor;

    @Schema(description = "Data da movimentação", example = "2024-01-15")
    private LocalDate dataMovimentacao;

    @Schema(description = "Data e hora do lançamento", example = "2024-01-15T10:30:00")
    private LocalDateTime dataLancamento;

    @Schema(description = "ID do usuário que fez o lançamento", example = "1")
    private Long usuarioLancamentoId;

    @Schema(description = "Nome do usuário que fez o lançamento", example = "João Silva")
    private String usuarioLancamentoNome;

    @Schema(description = "Observações adicionais", example = "Pagamento à vista com desconto")
    private String observacoes;

    @Schema(description = "Status da movimentação", example = "CONFIRMADO")
    private MovimentacaoCaixa.StatusMovimentacao status;
}
