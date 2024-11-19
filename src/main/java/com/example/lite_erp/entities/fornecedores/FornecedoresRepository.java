package com.example.lite_erp.entities.fornecedores;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FornecedoresRepository extends JpaRepository<Fornecedores, Integer> {
    Page<Fornecedores> findByRazaoSocialContainingIgnoreCase(String nome, Pageable pageable);

    @Query("SELECT new com.example.lite_erp.entities.fornecedores.FornecedoresBuscaResponseDTO(" +
            "f.id, f.razaoSocial, f.nomeFantasia) " +
            "FROM Fornecedores f " +
            "ORDER BY f.id DESC")
    Page<FornecedoresBuscaResponseDTO> findFornecedoresForBusca(Pageable pageable);

    @Query("SELECT new com.example.lite_erp.entities.fornecedores.FornecedoresBuscaResponseDTO(" +
            "f.id, f.razaoSocial, f.nomeFantasia) " +
            "FROM Fornecedores f " +
            "WHERE LOWER(f.razaoSocial) LIKE LOWER(:razaoSocial) " +
            "ORDER BY f.id DESC")
    Page<FornecedoresBuscaResponseDTO> findFornecedoresForBuscaByRazaoSocial(@Param("razaoSocial") String razaoSocial, Pageable pageable);
}
