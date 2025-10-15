package com.example.lite_erp.entities.nfe;

import java.math.BigDecimal;

/**
 * DTO interno para representar um item extraído do XML da NFe
 * (antes do processamento de vinculação)
 */
public record ItemNfeDTO(
        Integer numeroItem,
        String codigoProduto,
        String descricao,
        String ncm,
        String ean,
        BigDecimal quantidade,
        String unidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal
) {}

