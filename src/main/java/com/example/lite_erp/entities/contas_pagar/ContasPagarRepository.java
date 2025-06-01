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
            "(COALESCE(:dataInicio, c.dataVencimento) = c.dataVencimento OR c.dataVencimento >= :dataInicio) AND " +
            "(COALESCE(:dataFim, c.dataVencimento) = c.dataVencimento OR c.dataVencimento <= :dataFim)")
    Page<ContasPagar> buscarPorIntervaloDeDatas(
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
}