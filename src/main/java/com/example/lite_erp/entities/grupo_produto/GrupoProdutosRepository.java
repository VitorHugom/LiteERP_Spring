package com.example.lite_erp.entities.grupo_produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoProdutosRepository extends JpaRepository<GrupoProdutos, Long> {
    Page<GrupoProdutosResponseDTO> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
