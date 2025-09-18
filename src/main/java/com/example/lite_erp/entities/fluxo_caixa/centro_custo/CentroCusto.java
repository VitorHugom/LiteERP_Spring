package com.example.lite_erp.entities.fluxo_caixa.centro_custo;

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
@Table(name = "centro_custo")
public class CentroCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do centro de custo", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Descrição do centro de custo", example = "Vendas")
    private String descricao;

    @Column(nullable = false, unique = true, length = 20)
    @Schema(description = "Código identificador do centro de custo", example = "VENDAS")
    private String codigo;

    @Column(nullable = false)
    @Schema(description = "Indica se o centro de custo está ativo", example = "true")
    private Boolean ativo = true;

    @Column(name = "data_criacao")
    @Schema(description = "Data de criação do registro")
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }
}
