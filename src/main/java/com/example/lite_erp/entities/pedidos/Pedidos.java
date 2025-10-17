package com.example.lite_erp.entities.pedidos;

import com.example.lite_erp.entities.clientes.Clientes;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;
import com.example.lite_erp.entities.vendedores.Vendedores;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = true)
    private Clientes cliente;

    @Column(name = "cliente_final")
    private String clienteFinal;

    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Vendedores vendedor;

    @Column(name = "data_emissao", nullable = false)
    private LocalDateTime dataEmissao;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_tipo_cobranca", nullable = false)
    private TiposCobranca tipoCobranca;

    @Column(name = "ultima_atualizacao", nullable = false)
    private LocalDateTime ultimaAtualizacao;
}
