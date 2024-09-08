package com.example.lite_erp.entities.produtos;

import com.example.lite_erp.entities.grupo_produto.GrupoProdutos;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produtos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "grupo_produtos", referencedColumnName = "id")
    private GrupoProdutos grupoProdutos;

    private String marca;

    @Column(name = "data_ultima_compra")
    private LocalDate dataUltimaCompra;

    @Column(name = "preco_compra", precision = 10, scale = 2)
    private BigDecimal precoCompra;

    @Column(name = "preco_venda", precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "peso", precision = 10, scale = 3)
    private BigDecimal peso;

    @Column(name = "cod_ean", length = 13)
    private String codEan;

    @Column(name = "cod_ncm", length = 8)
    private String codNcm;

    @Column(name = "cod_cest", length = 7)
    private String codCest;
}
