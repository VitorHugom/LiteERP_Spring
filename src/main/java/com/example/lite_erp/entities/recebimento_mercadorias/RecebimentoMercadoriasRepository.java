package com.example.lite_erp.entities.recebimento_mercadorias;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecebimentoMercadoriasRepository extends JpaRepository<RecebimentoMercadorias, Integer> {

    @Query("SELECT new com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadoriasBuscaResponseDTO(" +
            "r.idRecebimento, f.razaoSocial, r.dataRecebimento) " +
            "FROM RecebimentoMercadorias r " +
            "JOIN r.fornecedor f " +
            "WHERE LOWER(f.razaoSocial) LIKE LOWER(CONCAT(:razaoSocial, '%')) " +
            "ORDER BY r.dataRecebimento DESC")
    Page<RecebimentoMercadoriasBuscaResponseDTO> findRecebimentosByFornecedorRazaoSocial(
            @Param("razaoSocial") String razaoSocial, Pageable pageable);

    @Query("SELECT new com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadoriasBuscaResponseDTO(" +
            "r.idRecebimento, f.razaoSocial, r.dataRecebimento) " +
            "FROM RecebimentoMercadorias r " +
            "JOIN r.fornecedor f " +
            "ORDER BY r.dataRecebimento DESC")
    Page<RecebimentoMercadoriasBuscaResponseDTO> findAllOrderedByDataRecebimento(Pageable pageable);
}
