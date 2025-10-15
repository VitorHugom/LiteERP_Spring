package com.example.lite_erp.entities.nfe;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO interno para representar os dados extraídos do XML da NFe
 * (antes do processamento de vinculação)
 */
public record NfeDadosExtraidosDTO(
        String chaveAcesso,
        String numeroNota,
        String serie,
        LocalDate dataEmissao,
        BigDecimal valorTotal,
        String cnpjFornecedor,
        String razaoSocialFornecedor,
        String nomeFantasiaFornecedor,
        List<ItemNfeDTO> itens
) {}

