package com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa;

import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import com.example.lite_erp.entities.fluxo_caixa.centro_custo.CentroCusto;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixa;
import com.example.lite_erp.entities.usuario.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movimentacao_caixa")
public class MovimentacaoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da movimentação", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_caixa_id", nullable = false)
    @Schema(description = "Conta de caixa da movimentação")
    private ContaCaixa contaCaixa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_movimentacao_id", nullable = false)
    @Schema(description = "Tipo da movimentação")
    private TipoMovimentacao tipoMovimentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_custo_id")
    @Schema(description = "Centro de custo da movimentação")
    private CentroCusto centroCusto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_origem", nullable = false, length = 20)
    @Schema(description = "Origem da movimentação", example = "MANUAL")
    private TipoOrigem tipoOrigem;

    @Column(name = "referencia_id")
    @Schema(description = "ID de referência (conta a pagar/receber)", example = "123")
    private Long referenciaId;

    @Column(name = "numero_documento", length = 50)
    @Schema(description = "Número do documento", example = "DOC-001")
    private String numeroDocumento;

    @Column(nullable = false)
    @Schema(description = "Descrição da movimentação", example = "Pagamento de fornecedor")
    private String descricao;

    @Column(nullable = false, precision = 15, scale = 2)
    @Schema(description = "Valor da movimentação (positivo=entrada, negativo=saída)", example = "1500.50")
    private BigDecimal valor;

    @Column(name = "data_movimentacao", nullable = false)
    @Schema(description = "Data da movimentação", example = "2024-01-15")
    private LocalDate dataMovimentacao;

    @Column(name = "data_lancamento")
    @Schema(description = "Data e hora do lançamento")
    private LocalDateTime dataLancamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_lancamento_id", nullable = false)
    @Schema(description = "Usuário que fez o lançamento")
    private Usuario usuarioLancamento;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Observações adicionais", example = "Pagamento à vista com desconto")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Status da movimentação", example = "CONFIRMADO")
    private StatusMovimentacao status = StatusMovimentacao.CONFIRMADO;

    @PrePersist
    protected void onCreate() {
        dataLancamento = LocalDateTime.now();
    }

    public enum TipoOrigem {
        MANUAL("Manual"),
        CONTA_PAGAR("Conta a Pagar"),
        CONTA_RECEBER("Conta a Receber"),
        TRANSFERENCIA("Transferência");

        private final String descricao;

        TipoOrigem(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public enum StatusMovimentacao {
        PENDENTE("Pendente"),
        CONFIRMADO("Confirmado"),
        CANCELADO("Cancelado");

        private final String descricao;

        StatusMovimentacao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
