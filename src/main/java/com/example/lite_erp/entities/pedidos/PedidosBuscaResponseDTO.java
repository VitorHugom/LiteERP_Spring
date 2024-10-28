package com.example.lite_erp.entities.pedidos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidosBuscaResponseDTO(
        Long id,
        String nomeCliente,
        String nomeVendedor,
        LocalDateTime dataEmissao,
        String status
) {
    public PedidosBuscaResponseDTO(Long id, String nomeCliente, String nomeVendedor,
                                   LocalDateTime dataEmissao,  String status) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.nomeVendedor = nomeVendedor;
        this.dataEmissao = dataEmissao;
        this.status = status;
    }
}
