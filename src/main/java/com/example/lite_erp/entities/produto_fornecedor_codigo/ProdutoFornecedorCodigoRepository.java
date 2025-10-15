package com.example.lite_erp.entities.produto_fornecedor_codigo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoFornecedorCodigoRepository extends JpaRepository<ProdutoFornecedorCodigo, Long> {

    /**
     * Busca todos os vínculos de um produto específico
     */
    List<ProdutoFornecedorCodigo> findByProdutoId(Long idProduto);

    /**
     * Busca todos os vínculos de um fornecedor específico
     */
    List<ProdutoFornecedorCodigo> findByFornecedorId(Integer idFornecedor);

    /**
     * Busca produto pelo código do fornecedor (usado na importação de NFe)
     * Retorna o vínculo ativo que corresponde ao código informado para aquele fornecedor
     */
    @Query("SELECT pfc FROM ProdutoFornecedorCodigo pfc " +
           "WHERE pfc.codigoFornecedor = :codigoFornecedor " +
           "AND pfc.fornecedor.id = :idFornecedor " +
           "AND pfc.ativo = true")
    Optional<ProdutoFornecedorCodigo> findByCodigoFornecedorAndFornecedorId(
            @Param("codigoFornecedor") String codigoFornecedor,
            @Param("idFornecedor") Integer idFornecedor
    );

    /**
     * Busca paginada com DTO simplificado para listagem
     */
    @Query("SELECT new com.example.lite_erp.entities.produto_fornecedor_codigo.ProdutoFornecedorCodigoBuscaResponseDTO(" +
           "pfc.id, p.id, p.descricao, f.id, f.razaoSocial, pfc.codigoFornecedor, pfc.ativo) " +
           "FROM ProdutoFornecedorCodigo pfc " +
           "JOIN pfc.produto p " +
           "JOIN pfc.fornecedor f " +
           "ORDER BY pfc.id DESC")
    Page<ProdutoFornecedorCodigoBuscaResponseDTO> findAllForBusca(Pageable pageable);

    /**
     * Busca paginada por produto com DTO simplificado
     */
    @Query("SELECT new com.example.lite_erp.entities.produto_fornecedor_codigo.ProdutoFornecedorCodigoBuscaResponseDTO(" +
           "pfc.id, p.id, p.descricao, f.id, f.razaoSocial, pfc.codigoFornecedor, pfc.ativo) " +
           "FROM ProdutoFornecedorCodigo pfc " +
           "JOIN pfc.produto p " +
           "JOIN pfc.fornecedor f " +
           "WHERE p.id = :idProduto " +
           "ORDER BY pfc.id DESC")
    Page<ProdutoFornecedorCodigoBuscaResponseDTO> findByProdutoIdForBusca(
            @Param("idProduto") Long idProduto,
            Pageable pageable
    );

    /**
     * Busca paginada por fornecedor com DTO simplificado
     */
    @Query("SELECT new com.example.lite_erp.entities.produto_fornecedor_codigo.ProdutoFornecedorCodigoBuscaResponseDTO(" +
           "pfc.id, p.id, p.descricao, f.id, f.razaoSocial, pfc.codigoFornecedor, pfc.ativo) " +
           "FROM ProdutoFornecedorCodigo pfc " +
           "JOIN pfc.produto p " +
           "JOIN pfc.fornecedor f " +
           "WHERE f.id = :idFornecedor " +
           "ORDER BY pfc.id DESC")
    Page<ProdutoFornecedorCodigoBuscaResponseDTO> findByFornecedorIdForBusca(
            @Param("idFornecedor") Integer idFornecedor,
            Pageable pageable
    );

    /**
     * Busca paginada com filtro por nome do produto ou fornecedor
     */
    @Query("SELECT new com.example.lite_erp.entities.produto_fornecedor_codigo.ProdutoFornecedorCodigoBuscaResponseDTO(" +
           "pfc.id, p.id, p.descricao, f.id, f.razaoSocial, pfc.codigoFornecedor, pfc.ativo) " +
           "FROM ProdutoFornecedorCodigo pfc " +
           "JOIN pfc.produto p " +
           "JOIN pfc.fornecedor f " +
           "WHERE LOWER(p.descricao) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
           "OR LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
           "OR LOWER(pfc.codigoFornecedor) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
           "ORDER BY pfc.id DESC")
    Page<ProdutoFornecedorCodigoBuscaResponseDTO> findByFiltro(
            @Param("filtro") String filtro,
            Pageable pageable
    );
}

