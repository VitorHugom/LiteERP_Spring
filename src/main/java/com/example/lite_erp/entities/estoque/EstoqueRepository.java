package com.example.lite_erp.entities.estoque;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProdutoId(Long idProduto);

    @Query("SELECT e FROM Estoque e")
    Page<Estoque> findAllEstoque(Pageable pageable);

    @Query("SELECT e FROM Estoque e WHERE LOWER(e.produto.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    Page<Estoque> findByProdutoDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

    @Query("SELECT e " +
            "FROM Estoque e " +
            "JOIN e.produto p " +
            "JOIN p.grupoProdutos g " +
            "WHERE g.id = COALESCE(:grupoId, g.id) " +
            "ORDER BY p.dataUltimaCompra DESC")  // ou outro campo de ordenação que fizer sentido
    List<Estoque> filterEstoque(
            @Param("grupoId") Long grupoId
    );
}
