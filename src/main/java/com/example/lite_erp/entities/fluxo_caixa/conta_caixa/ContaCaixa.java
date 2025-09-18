package com.example.lite_erp.entities.fluxo_caixa.conta_caixa;

import com.example.lite_erp.entities.usuario.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conta_caixa")
public class ContaCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da conta de caixa", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Descrição da conta de caixa", example = "Caixa Principal")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Tipo da conta", example = "CAIXA_FISICO")
    private TipoConta tipo;

    @Column(length = 100)
    @Schema(description = "Nome do banco (para contas bancárias)", example = "Banco do Brasil")
    private String banco;

    @Column(length = 20)
    @Schema(description = "Número da agência", example = "1234-5")
    private String agencia;

    @Column(length = 30)
    @Schema(description = "Número da conta", example = "12345-6")
    private String conta;

    @Column(name = "saldo_atual", precision = 15, scale = 2)
    @Schema(description = "Saldo atual da conta", example = "1500.50")
    private BigDecimal saldoAtual = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_responsavel_id", nullable = false)
    @Schema(description = "Usuário responsável pela conta")
    private Usuario usuarioResponsavel;

    @Column(nullable = false)
    @Schema(description = "Indica se a conta está ativa", example = "true")
    private Boolean ativo = true;

    @Column(name = "data_criacao")
    @Schema(description = "Data de criação da conta")
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    public enum TipoConta {
        CAIXA_FISICO("Caixa Físico"),
        CONTA_BANCARIA("Conta Bancária"),
        CARTEIRA_DIGITAL("Carteira Digital");

        private final String descricao;

        TipoConta(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
