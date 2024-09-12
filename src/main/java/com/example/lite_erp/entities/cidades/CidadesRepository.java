package com.example.lite_erp.entities.cidades;

import com.example.lite_erp.entities.cidades.Cidades;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadesRepository extends JpaRepository<Cidades, Long> {
    Page<Cidades> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
