package com.example.lite_erp.entities.contas_receber;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    @Query("SELECT c FROM ContasReceber c WHERE " +
            "c.cliente.id = :idCliente AND " +
            "(:razaoSocial IS NULL OR c.cliente.razaoSocial LIKE :razaoSocial) AND " +
            "(:dataInicio IS NULL OR c.dataVencimento >= :dataInicio) AND " +
            "(:dataFim IS NULL OR c.dataVencimento <= :dataFim)")
    Page<ContasReceber> buscarPorClienteComFiltro(
            @Param("idCliente") Integer idCliente,
            @Param("razaoSocial") String razaoSocial,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);

    @Query("""
        SELECT c
        FROM ContasReceber c
        JOIN c.cliente cl
        JOIN c.formaPagamento fp
        JOIN c.tiposCobranca tc
        WHERE c.dataVencimento >= COALESCE(:dataInicio, c.dataVencimento)
          AND c.dataVencimento <= COALESCE(:dataFim,    c.dataVencimento)
          AND cl.id             = COALESCE(:idCliente,         cl.id)
          AND tc.id             = COALESCE(:idTipoCobranca,     tc.id)
          AND fp.id             = COALESCE(:idFormaPagamento,   fp.id)
          AND c.valorTotal      >= COALESCE(:valorTotalInicial, c.valorTotal)
          AND c.valorTotal      <= COALESCE(:valorTotalFinal,   c.valorTotal)
        ORDER BY c.dataVencimento DESC
    """)
    List<ContasReceber> filterContasReceber(
            @Param("dataInicio")            LocalDate dataInicio,
            @Param("dataFim")               LocalDate dataFim,
            @Param("idCliente")             Integer idCliente,
            @Param("idTipoCobranca")        Integer idTipoCobranca,
            @Param("idFormaPagamento")      Integer idFormaPagamento,
            @Param("valorTotalInicial")     BigDecimal valorTotalInicial,
            @Param("valorTotalFinal")       BigDecimal valorTotalFinal
    );
}
