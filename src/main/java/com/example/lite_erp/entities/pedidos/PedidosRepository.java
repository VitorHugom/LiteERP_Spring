package com.example.lite_erp.entities.pedidos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidosRepository extends JpaRepository<Pedidos, Long> {
    List<Pedidos> findByStatusOrderByIdDesc(String status);

    @Query("SELECT new com.example.lite_erp.entities.pedidos.PedidosBuscaResponseDTO(" +
            "p.id, p.cliente.razaoSocial, p.vendedor.nome, p.dataEmissao, p.status) " +
            "FROM Pedidos p " +
            "JOIN p.cliente c " +
            "JOIN p.vendedor v " +
            "ORDER BY p.dataEmissao DESC")
    Page<PedidosBuscaResponseDTO> findPedidosForBusca(Pageable pageable);

    @Query("SELECT new com.example.lite_erp.entities.pedidos.PedidosBuscaResponseDTO(" +
            "p.id, c.razaoSocial, v.nome, p.dataEmissao, p.status) " +
            "FROM Pedidos p " +
            "JOIN p.cliente c " +
            "JOIN p.vendedor v " +
            "WHERE LOWER(c.razaoSocial) LIKE LOWER(:razaoSocial) " +
            "ORDER BY p.dataEmissao DESC")
    Page<PedidosBuscaResponseDTO> findPedidosForBuscaByClienteRazaoSocial(@Param("razaoSocial") String razaoSocial, Pageable pageable);

    @Query("""
        SELECT p 
        FROM Pedidos p
        JOIN p.cliente c
        JOIN p.vendedor v
        JOIN p.tipoCobranca t
        WHERE c.id       = COALESCE(:idCliente,      c.id)
          AND v.id       = COALESCE(:idVendedor,     v.id)
          AND p.dataEmissao >= COALESCE(:dataInicio, p.dataEmissao)
          AND p.dataEmissao <= COALESCE(:dataFim,    p.dataEmissao)
          AND p.valorTotal >= COALESCE(:valorInicial, p.valorTotal)
          AND p.valorTotal <= COALESCE(:valorFinal,   p.valorTotal)
          AND LOWER(p.status) = LOWER(COALESCE(:status, p.status))
          AND t.id       = COALESCE(:idTipoCobranca, t.id)
        ORDER BY p.dataEmissao DESC
    """)
    List<Pedidos> filterPedidos(
            @Param("idCliente")      Long idCliente,
            @Param("idVendedor")     Long idVendedor,
            @Param("dataInicio")     LocalDateTime dataInicio,
            @Param("dataFim")        LocalDateTime dataFim,
            @Param("valorInicial")   BigDecimal valorInicial,
            @Param("valorFinal")     BigDecimal valorFinal,
            @Param("status")         String status,
            @Param("idTipoCobranca") Long idTipoCobranca
    );
}
