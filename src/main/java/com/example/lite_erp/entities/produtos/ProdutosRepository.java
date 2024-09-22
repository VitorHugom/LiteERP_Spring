package com.example.lite_erp.entities.produtos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {
    Page<Produtos> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);
}
