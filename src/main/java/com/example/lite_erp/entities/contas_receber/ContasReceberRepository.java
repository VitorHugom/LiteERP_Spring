package com.example.lite_erp.entities.contas_receber;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ContasReceberRepository extends JpaRepository<ContasReceber, Integer> {

    List<ContasReceber> findByClienteId(Integer clienteId);

    List<ContasReceber> findByNumeroDocumento(String numeroDocumento);

    @Query("SELECT c FROM ContasReceber c WHERE " +
            "(:razaoSocial IS NULL OR LOWER(c.cliente.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%'))) AND " +
            "(COALESCE(:dataInicio, c.dataVencimento) = c.dataVencimento OR c.dataVencimento >= :dataInicio) AND " +
            "(COALESCE(:dataFim, c.dataVencimento) = c.dataVencimento OR c.dataVencimento <= :dataFim)")
    Page<ContasReceber> buscarPorFiltro(
            @Param("razaoSocial") String razaoSocial,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);

    @Query("SELECT c FROM ContasReceber c WHERE " +
            "(COALESCE(:dataInicio, c.dataVencimento) = c.dataVencimento OR c.dataVencimento >= :dataInicio) AND " +
            "(COALESCE(:dataFim, c.dataVencimento) = c.dataVencimento OR c.dataVencimento <= :dataFim)")
    Page<ContasReceber> buscarPorIntervaloDeDatas(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);
}
