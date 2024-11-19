package com.example.lite_erp.entities.itens_recebimento_mercadorias;

import com.example.lite_erp.entities.produtos.Produtos;
import com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadorias;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "itens_recebimento_mercadoria")
public class ItensRecebimentoMercadorias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_recebimento")
    private RecebimentoMercadorias recebimento;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produtos produto;

    private BigDecimal quantidade;

    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario;

}
