package com.example.lite_erp.entities.vendedores;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendedoresRepository extends JpaRepository<Vendedores, Long> {
    Page<Vendedores> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
