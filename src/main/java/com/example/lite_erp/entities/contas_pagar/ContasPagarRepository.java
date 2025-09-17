package com.example.lite_erp.entities.contas_pagar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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
            "(:razaoSocial IS NULL OR LOWER(c.fornecedor.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%'))) AND " +
            "(COALESCE(:dataInicio, c.dataVencimento) = c.dataVencimento OR c.dataVencimento >= :dataInicio) AND " +
            "(COALESCE(:dataFim, c.dataVencimento) = c.dataVencimento OR c.dataVencimento <= :dataFim) AND " +
            "c.status = 'aberta'")
    Page<ContasPagar> buscarPorFiltroSomentePagar(
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

    @Query("SELECT c FROM ContasPagar c WHERE " +
            "(COALESCE(:dataInicio, c.dataVencimento) = c.dataVencimento OR c.dataVencimento >= :dataInicio) AND " +
            "(COALESCE(:dataFim, c.dataVencimento) = c.dataVencimento OR c.dataVencimento <= :dataFim) AND " +
            "c.status = 'aberta'")
    Page<ContasPagar> buscarPorIntervaloDeDatasSomentePagar(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);

    @Query("SELECT c FROM ContasPagar c WHERE " +
            "c.fornecedor.id = :idFornecedor AND " +
            "(:razaoSocial IS NULL OR c.fornecedor.razaoSocial LIKE :razaoSocial) AND " +
            "(:dataInicio IS NULL OR c.dataVencimento >= :dataInicio) AND " +
            "(:dataFim IS NULL OR c.dataVencimento <= :dataFim)")
    Page<ContasPagar> buscarPorFornecedorComFiltro(
            @Param("idFornecedor") Long idFornecedor,
            @Param("razaoSocial") String razaoSocial,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);

    @Query("SELECT c FROM ContasPagar c WHERE " +
            "c.fornecedor.id = :idFornecedor AND " +
            "(:razaoSocial IS NULL OR c.fornecedor.razaoSocial LIKE :razaoSocial) AND " +
            "(:dataInicio IS NULL OR c.dataVencimento >= :dataInicio) AND " +
            "(:dataFim IS NULL OR c.dataVencimento <= :dataFim) AND " +
            "c.status = 'aberta'")
    Page<ContasPagar> buscarPorFornecedorComFiltroSomentePagar(
            @Param("idFornecedor") Long idFornecedor,
            @Param("razaoSocial") String razaoSocial,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);

    @Query("""
        SELECT c
        FROM ContasPagar c
        JOIN c.fornecedor f
        JOIN c.formaPagamento fp
        JOIN c.tipoCobranca tc
        WHERE c.dataVencimento >= COALESCE(:dataInicio, c.dataVencimento)
          AND c.dataVencimento <= COALESCE(:dataFim,    c.dataVencimento)
          AND f.id                = COALESCE(:idFornecedor,     f.id)
          AND tc.id               = COALESCE(:idTipoCobranca,   tc.id)
          AND fp.id               = COALESCE(:idFormaPagamento, fp.id)
          AND c.valorTotal        >= COALESCE(:valorTotalInicial, c.valorTotal)
          AND c.valorTotal        <= COALESCE(:valorTotalFinal,   c.valorTotal)
        ORDER BY c.dataVencimento DESC
    """)
    List<ContasPagar> filterContasPagar(
            @Param("dataInicio")          LocalDate dataInicio,
            @Param("dataFim")             LocalDate dataFim,
            @Param("idFornecedor")        Long idFornecedor,
            @Param("idTipoCobranca")      Integer idTipoCobranca,
            @Param("idFormaPagamento")    Integer idFormaPagamento,
            @Param("valorTotalInicial")   BigDecimal valorTotalInicial,
            @Param("valorTotalFinal")     BigDecimal valorTotalFinal
    );

    @Query(value = """
        SELECT c.data_vencimento as dataVencimento,
               SUM(c.valor_parcela) as valorTotalParcelas,
               COUNT(c.id) as qtdParcelas,
               STRING_AGG(c.id::text, ',') as idsParcelas
        FROM contas_pagar c
        WHERE (COALESCE(:status, '') = '' OR c.status = :status)
          AND (COALESCE(:dataInicio, c.data_vencimento) = c.data_vencimento OR c.data_vencimento >= :dataInicio)
          AND (COALESCE(:dataFim, c.data_vencimento) = c.data_vencimento OR c.data_vencimento <= :dataFim)
          AND (COALESCE(:idFornecedor, c.id_fornecedor) = c.id_fornecedor OR c.id_fornecedor = :idFornecedor)
          AND (COALESCE(:idFormaPagamento, c.id_forma_pagamento) = c.id_forma_pagamento OR c.id_forma_pagamento = :idFormaPagamento)
        GROUP BY c.data_vencimento
        ORDER BY c.data_vencimento ASC
    """,
    countQuery = """
        SELECT COUNT(DISTINCT c.data_vencimento)
        FROM contas_pagar c
        WHERE (COALESCE(:status, '') = '' OR c.status = :status)
          AND (COALESCE(:dataInicio, c.data_vencimento) = c.data_vencimento OR c.data_vencimento >= :dataInicio)
          AND (COALESCE(:dataFim, c.data_vencimento) = c.data_vencimento OR c.data_vencimento <= :dataFim)
          AND (COALESCE(:idFornecedor, c.id_fornecedor) = c.id_fornecedor OR c.id_fornecedor = :idFornecedor)
          AND (COALESCE(:idFormaPagamento, c.id_forma_pagamento) = c.id_forma_pagamento OR c.id_forma_pagamento = :idFormaPagamento)
    """,
    nativeQuery = true)
    Page<ContasPagarRelatorioProjection> buscarRelatorioContasPorData(
            @Param("status") String status,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("idFornecedor") Long idFornecedor,
            @Param("idFormaPagamento") Long idFormaPagamento,
            Pageable pageable
    );
}