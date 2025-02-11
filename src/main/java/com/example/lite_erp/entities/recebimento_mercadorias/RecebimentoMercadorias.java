package com.example.lite_erp.entities.recebimento_mercadorias;


import com.example.lite_erp.entities.forma_pagamento.FormaPagamento;
import com.example.lite_erp.entities.fornecedores.Fornecedores;
import com.example.lite_erp.entities.itens_recebimento_mercadorias.ItensRecebimentoMercadorias;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "recebimento_mercadorias")
public class RecebimentoMercadorias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recebimento")
    private Integer idRecebimento;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedores fornecedor;

    @ManyToOne
    @JoinColumn (name = "tipo_cobranca_id")
    private TiposCobranca tipoCobranca;

    @Column(name = "data_recebimento")
    private LocalDate dataRecebimento;

    @OneToMany(mappedBy = "recebimento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItensRecebimentoMercadorias> itens = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

}
