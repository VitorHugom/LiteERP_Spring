package com.example.lite_erp.entities.pedidos;

import com.example.lite_erp.entities.clientes.Clientes;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;
import com.example.lite_erp.entities.vendedores.Vendedores;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidosResponseDTO(Long id, Clientes cliente, String clienteFinal, Vendedores vendedor, LocalDateTime dataEmissao, BigDecimal valorTotal, String status, TiposCobranca tipoCobranca, LocalDateTime ultimaAtualizacao) {
    public PedidosResponseDTO(Pedidos pedidos) {
        this(pedidos.getId(), pedidos.getCliente(), pedidos.getClienteFinal(), pedidos.getVendedor(), pedidos.getDataEmissao(), pedidos.getValorTotal(), pedidos.getStatus(), pedidos.getTipoCobranca(), pedidos.getUltimaAtualizacao());
    }
}
