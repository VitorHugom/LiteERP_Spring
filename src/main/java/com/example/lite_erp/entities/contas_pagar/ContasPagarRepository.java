package com.example.lite_erp.entities.contas_pagar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ContasPagarRepository extends JpaRepository<ContasPagar, Long> {
    List<ContasPagar> findByFornecedorId(Long fornecedorId);

    List<ContasPagar> findByNumeroDocumento(String numeroDocumento);

    @Query("SELECT c FROM ContasPagar c WHERE " +
            "(:razaoSocial IS NULL OR LOWER(c.fornecedor.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%'))) AND " +
            "(COALESCE(:dataInicio, c.dataVencimento) = c.dataVencimento OR c.dataVencimento >= :dataInicio) AND " +
            "(COALESCE(:dataFim, c.dataVencimento) = c.dataVencimento OR c.dataVencimento <= :dataFim)")
    Page<ContasPagar> buscarPorFiltro(
            @Param("razaoSocial") String razaoSocial,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);

    @Query("SELECT c FROM ContasPagar c WHERE " +
            "(COALESCE(:dataInicio, c.dataVencimento) = c.dataVencimento OR c.dataVencimento >= :dataInicio) AND " +
            "(COALESCE(:dataFim, c.dataVencimento) = c.dataVencimento OR c.dataVencimento <= :dataFim)")
    Page<ContasPagar> buscarPorIntervaloDeDatas(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);


}