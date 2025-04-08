package com.example.lite_erp.entities.produtos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {
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
}
