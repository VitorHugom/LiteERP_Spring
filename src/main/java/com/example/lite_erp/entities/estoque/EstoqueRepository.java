package com.example.lite_erp.entities.estoque;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProdutoId(Long idProduto);

    @Query("SELECT e FROM Estoque e")
    Page<Estoque> findAllEstoque(Pageable pageable);

    @Query("SELECT e FROM Estoque e WHERE LOWER(e.produto.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    Page<Estoque> findByProdutoDescricaoContainingIgnoreCase(String descricao, Pageable pageable);
}
