package com.example.lite_erp.entities.movimento_estoque;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MovimentoEstoqueRepository extends JpaRepository <MovimentoEstoque, Long> {
    @Query("SELECT m FROM MovimentoEstoque m WHERE m.dataMovimentacao BETWEEN :startDate AND :endDate")
    Page<MovimentoEstoque> findByDataMovimentacaoBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
