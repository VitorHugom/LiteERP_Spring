package com.example.lite_erp.entities.clientes;

import java.time.LocalDate;

public record ClientesFiltroDTO(
        LocalDate dataNascimentoInicial,
        LocalDate dataNascimentoFinal,
        Long vendedorId,
        Long cidadeId
) {}
