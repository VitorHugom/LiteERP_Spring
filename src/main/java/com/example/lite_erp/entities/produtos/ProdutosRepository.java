package com.example.lite_erp.entities.produtos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

    Optional<Produtos> findByCodEan(String codEan);

    List<Produtos> findByCodNcm(String codNcm);

    @Query("SELECT p FROM Produtos p WHERE LOWER(p.descricao) LIKE LOWER(CONCAT('%', :termo, '%')) ORDER BY p.descricao")
    List<Produtos> findByDescricaoContaining(@Param("termo") String termo);
    Page<Produtos> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

    @Query("SELECT new com.example.lite_erp.entities.produtos.ProdutosBuscaResponseDTO(" +
            "p.id, p.descricao, p.precoVenda) " +
            "FROM Produtos p " +
            "ORDER BY p.id DESC")
    Page<ProdutosBuscaResponseDTO> findProdutosForBusca(Pageable pageable);

    @Query("SELECT new com.example.lite_erp.entities.produtos.ProdutosBuscaResponseDTO(" +
            "p.id, p.descricao, p.precoVenda) " +
            "FROM Produtos p " +
            "WHERE LOWER(p.descricao) LIKE LOWER(:descricao) " +
            "ORDER BY p.id DESC")
    Page<ProdutosBuscaResponseDTO> findProdutosForBuscaByDescricao(@Param("descricao") String descricao, Pageable pageable);

    @Query("SELECT new com.example.lite_erp.entities.produtos.ProdutosBuscaResponseDTO(" +
            "p.id, p.descricao, p.precoVenda) " +
            "FROM Produtos p " +
            "WHERE LOWER(p.descricao) LIKE LOWER(:busca) OR (p.codEan) LIKE (:busca)" +
            "ORDER BY p.id DESC")
    Page<ProdutosBuscaResponseDTO> findProdutosForBuscaByDescricaoCodEan(@Param("busca") String busca, Pageable pageable);

    @Query("""
        SELECT p
        FROM Produtos p
        JOIN p.grupoProdutos g
        WHERE p.dataUltimaCompra >= COALESCE(:dataInicio, p.dataUltimaCompra)
          AND p.dataUltimaCompra <= COALESCE(:dataFim,    p.dataUltimaCompra)
          AND g.id            = COALESCE(:grupoId,        g.id)
          AND p.precoVenda    >= COALESCE(:pvInicio,       p.precoVenda)
          AND p.precoVenda    <= COALESCE(:pvFim,          p.precoVenda)
          AND p.precoCompra   >= COALESCE(:pcInicio,       p.precoCompra)
          AND p.precoCompra   <= COALESCE(:pcFim,          p.precoCompra)
          AND p.peso          >= COALESCE(:pesoInicio,     p.peso)
          AND p.peso          <= COALESCE(:pesoFim,        p.peso)
        ORDER BY p.dataUltimaCompra DESC
    """)
    List<Produtos> filterProdutos(
            @Param("dataInicio")    LocalDate dataInicio,
            @Param("dataFim")       LocalDate dataFim,
            @Param("grupoId")       Long grupoId,
            @Param("pvInicio")      BigDecimal pvInicio,
            @Param("pvFim")         BigDecimal pvFim,
            @Param("pcInicio")      BigDecimal pcInicio,
            @Param("pcFim")         BigDecimal pcFim,
            @Param("pesoInicio")    BigDecimal pesoInicio,
            @Param("pesoFim")       BigDecimal pesoFim
    );
}
