package com.example.lite_erp.entities.recebimento_mercadorias;

import java.time.LocalDate;

public record RecebimentoMercadoriasBuscaResponseDTO(Integer idRecebimento, String fornecedor, LocalDate dataRecebimento) {
    public RecebimentoMercadoriasBuscaResponseDTO(RecebimentoMercadorias recebimentoMercadorias){
        this(
                recebimentoMercadorias.getIdRecebimento(),
                recebimentoMercadorias.getFornecedor().getRazaoSocial(),
                recebimentoMercadorias.getDataRecebimento()
        );
    }
}
