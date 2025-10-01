package com.example.lite_erp.entities.pedidos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PedidosRequestDTO(Long idCliente, Long idVendedor, LocalDateTime dataEmissao, BigDecimal valorTotal, String status, Long idTipoCobranca) {
}
