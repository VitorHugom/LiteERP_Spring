package com.example.lite_erp.entities.movimento_estoque;


import com.example.lite_erp.entities.itens_pedido.ItensPedido;
import com.example.lite_erp.entities.itens_recebimento_mercadorias.ItensRecebimentoMercadorias;
import com.example.lite_erp.entities.produtos.Produtos;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movimento_estoque")
public class MovimentoEstoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_itens_pedido")
    private ItensPedido itemPedido;

    @ManyToOne
    @JoinColumn(name = "id_itens_recebimento_mercadoria")
    private ItensRecebimentoMercadorias itemRecebimentoMercadoria;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produtos produto;

    private BigDecimal qtd;

    @Column(name = "data_movimentacao")
    private LocalDateTime dataMovimentacao;
}
