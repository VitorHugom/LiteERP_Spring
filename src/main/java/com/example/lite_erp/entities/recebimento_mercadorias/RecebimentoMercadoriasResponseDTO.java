package com.example.lite_erp.entities.recebimento_mercadorias;

import com.example.lite_erp.entities.fornecedores.Fornecedores;
import com.example.lite_erp.entities.itens_recebimento_mercadorias.ItensRecebimentoMercadoriasResponseDTO;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;

import java.time.LocalDate;
import java.util.List;

public record RecebimentoMercadoriasResponseDTO(Integer id,
                                                Fornecedores fornecedor,
                                                TiposCobranca tipoCobranca,
                                                LocalDate dataRecebimento,
                                                List<ItensRecebimentoMercadoriasResponseDTO> itensRecebimento) {

    public RecebimentoMercadoriasResponseDTO(RecebimentoMercadorias recebimentoMercadorias) {
        this(
                recebimentoMercadorias.getIdRecebimento(),
                recebimentoMercadorias.getFornecedor(),
                recebimentoMercadorias.getTipoCobranca(),
                recebimentoMercadorias.getDataRecebimento(),
                recebimentoMercadorias.getItens().stream()
                        .map(ItensRecebimentoMercadoriasResponseDTO::new)
                        .toList()
        );
    }
}