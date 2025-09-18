package com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipo_movimentacao")
public class TipoMovimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do tipo de movimentação", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Descrição do tipo de movimentação", example = "Venda à Vista")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Categoria da movimentação", example = "RECEITA")
    private CategoriaMovimentacao categoria;

    @Column(name = "cor_hex", length = 7)
    @Schema(description = "Cor em hexadecimal para identificação visual", example = "#28a745")
    private String corHex;

    @Column(nullable = false)
    @Schema(description = "Indica se o tipo está ativo", example = "true")
    private Boolean ativo = true;

    @Column(name = "data_criacao")
    @Schema(description = "Data de criação do registro")
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    public enum CategoriaMovimentacao {
        RECEITA("Receita"),
        DESPESA("Despesa"),
        TRANSFERENCIA("Transferência");

        private final String descricao;

        CategoriaMovimentacao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
