package com.example.lite_erp.entities.clientes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientesRepository extends JpaRepository <Clientes, Long> {
    // Busca clientes pelo nome, ignorando letras maiúsculas e minúsculas, e com paginação
    Page<Clientes> findByRazaoSocialContainingIgnoreCase(String nome, Pageable pageable);
}
