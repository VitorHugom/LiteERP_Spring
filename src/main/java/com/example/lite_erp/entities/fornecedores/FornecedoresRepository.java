package com.example.lite_erp.entities.fornecedores;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FornecedoresRepository extends JpaRepository<Fornecedores, Integer> {

    Optional<Fornecedores> findByCnpj(String cnpj);
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

    @Query("""
        SELECT f
        FROM Fornecedores f
        LEFT JOIN f.cidade c
        WHERE f.dataCadastro >= COALESCE(:dataInicial, f.dataCadastro)
          AND f.dataCadastro <= COALESCE(:dataFinal,    f.dataCadastro)
          AND c.id           = COALESCE(:cidadeId,      c.id)
        ORDER BY f.id DESC
    """)
    List<Fornecedores> filterFornecedoresEntities(
            @Param("dataInicial") LocalDate dataInicial,
            @Param("dataFinal")   LocalDate dataFinal,
            @Param("cidadeId")    Integer cidadeId
    );
}
