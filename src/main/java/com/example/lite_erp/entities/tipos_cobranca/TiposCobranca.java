package com.example.lite_erp.entities.tipos_cobranca;

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
    private Long id;

    private String descricao;
}
