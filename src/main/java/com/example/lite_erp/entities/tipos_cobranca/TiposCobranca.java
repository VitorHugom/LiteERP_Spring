package com.example.lite_erp.entities.tipos_cobranca;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipos_cobranca")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TiposCobranca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do tipo de cobrança", example = "1")
    private Long id;

    @Schema(description = "Descrição do tipo de cobrança", example = "À vista")
    private String descricao;
}
