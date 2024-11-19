package com.example.lite_erp.entities.estoque;

import com.example.lite_erp.entities.produtos.Produtos;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "estoque")
public class Estoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_produto", nullable = false, unique = true)
    private Produtos produto;

    @Column(name = "qtd_estoque", precision = 10, scale = 4)
    private BigDecimal qtdEstoque = BigDecimal.ZERO;
}
