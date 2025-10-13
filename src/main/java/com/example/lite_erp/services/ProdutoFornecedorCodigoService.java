package com.example.lite_erp.services;

import com.example.lite_erp.entities.fornecedores.Fornecedores;
import com.example.lite_erp.entities.fornecedores.FornecedoresRepository;
import com.example.lite_erp.entities.produto_fornecedor_codigo.*;
import com.example.lite_erp.entities.produtos.Produtos;
import com.example.lite_erp.entities.produtos.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoFornecedorCodigoService {

    @Autowired
    private ProdutoFornecedorCodigoRepository repository;

    @Autowired
    private ProdutosRepository produtosRepository;

    @Autowired
    private FornecedoresRepository fornecedoresRepository;

    /**
     * Lista todos os vínculos
     */
    public List<ProdutoFornecedorCodigoResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(ProdutoFornecedorCodigoResponseDTO::new)
                .toList();
    }

    /**
     * Lista todos os vínculos com paginação
     */
    public Page<ProdutoFornecedorCodigoBuscaResponseDTO> listarTodosPaginado(Pageable pageable) {
        return repository.findAllForBusca(pageable);
    }

    /**
     * Busca vínculo por ID
     */
    public Optional<ProdutoFornecedorCodigo> buscarPorId(Long id) {
        return repository.findById(id);
    }

    /**
     * Cria um novo vínculo entre produto e código do fornecedor
     */
    @Transactional
    public ProdutoFornecedorCodigo criarVinculo(ProdutoFornecedorCodigoRequestDTO dto) {
        // Validar se produto existe
        Produtos produto = produtosRepository.findById(dto.idProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + dto.idProduto()));

        // Validar se fornecedor existe
        Fornecedores fornecedor = fornecedoresRepository.findById(dto.idFornecedor())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com ID: " + dto.idFornecedor()));

        // Validar se já existe vínculo entre produto e fornecedor
        Optional<ProdutoFornecedorCodigo> vinculoExistente = 
                repository.findByProdutoIdAndFornecedorId(dto.idProduto(), dto.idFornecedor());
        
        if (vinculoExistente.isPresent()) {
            throw new RuntimeException("Já existe um vínculo entre este produto e fornecedor. " +
                    "Use a atualização para modificar o código.");
        }

        // Criar novo vínculo
        ProdutoFornecedorCodigo vinculo = new ProdutoFornecedorCodigo();
        vinculo.setProduto(produto);
        vinculo.setFornecedor(fornecedor);
        vinculo.setCodigoFornecedor(dto.codigoFornecedor());
        vinculo.setAtivo(dto.ativo() != null ? dto.ativo() : true);
        vinculo.setDataCadastro(LocalDateTime.now());

        return repository.save(vinculo);
    }

    /**
     * Atualiza um vínculo existente
     */
    @Transactional
    public Optional<ProdutoFornecedorCodigo> atualizarVinculo(Long id, ProdutoFornecedorCodigoRequestDTO dto) {
        return repository.findById(id).map(vinculo -> {
            // Atualizar código do fornecedor
            if (dto.codigoFornecedor() != null) {
                vinculo.setCodigoFornecedor(dto.codigoFornecedor());
            }

            // Atualizar status ativo
            if (dto.ativo() != null) {
                vinculo.setAtivo(dto.ativo());
            }

            // Se mudou produto ou fornecedor, validar duplicidade
            if (dto.idProduto() != null && !dto.idProduto().equals(vinculo.getProduto().getId())) {
                Produtos novoProduto = produtosRepository.findById(dto.idProduto())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + dto.idProduto()));
                
                // Validar duplicidade com novo produto
                Integer idFornecedor = dto.idFornecedor() != null ? dto.idFornecedor() : vinculo.getFornecedor().getId();
                Optional<ProdutoFornecedorCodigo> vinculoExistente = 
                        repository.findByProdutoIdAndFornecedorId(dto.idProduto(), idFornecedor);
                
                if (vinculoExistente.isPresent() && !vinculoExistente.get().getId().equals(id)) {
                    throw new RuntimeException("Já existe um vínculo entre este produto e fornecedor.");
                }
                
                vinculo.setProduto(novoProduto);
            }

            if (dto.idFornecedor() != null && !dto.idFornecedor().equals(vinculo.getFornecedor().getId())) {
                Fornecedores novoFornecedor = fornecedoresRepository.findById(dto.idFornecedor())
                        .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com ID: " + dto.idFornecedor()));
                
                // Validar duplicidade com novo fornecedor
                Long idProduto = dto.idProduto() != null ? dto.idProduto() : vinculo.getProduto().getId();
                Optional<ProdutoFornecedorCodigo> vinculoExistente = 
                        repository.findByProdutoIdAndFornecedorId(idProduto, dto.idFornecedor());
                
                if (vinculoExistente.isPresent() && !vinculoExistente.get().getId().equals(id)) {
                    throw new RuntimeException("Já existe um vínculo entre este produto e fornecedor.");
                }
                
                vinculo.setFornecedor(novoFornecedor);
            }

            return repository.save(vinculo);
        });
    }

    /**
     * Deleta um vínculo
     */
    @Transactional
    public boolean deletarVinculo(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Busca todos os vínculos de um produto específico
     */
    public List<ProdutoFornecedorCodigoResponseDTO> buscarPorProduto(Long idProduto) {
        return repository.findByProdutoId(idProduto).stream()
                .map(ProdutoFornecedorCodigoResponseDTO::new)
                .toList();
    }

    /**
     * Busca todos os vínculos de um fornecedor específico
     */
    public List<ProdutoFornecedorCodigoResponseDTO> buscarPorFornecedor(Integer idFornecedor) {
        return repository.findByFornecedorId(idFornecedor).stream()
                .map(ProdutoFornecedorCodigoResponseDTO::new)
                .toList();
    }

    /**
     * Busca produto pelo código do fornecedor (método principal para importação de NFe)
     * Retorna o produto do sistema que corresponde ao código informado pelo fornecedor
     */
    public Optional<Produtos> buscarProdutoPorCodigoFornecedor(String codigoFornecedor, Integer idFornecedor) {
        return repository.findByCodigoFornecedorAndFornecedorId(codigoFornecedor, idFornecedor)
                .map(ProdutoFornecedorCodigo::getProduto);
    }

    /**
     * Busca produto DTO pelo código do fornecedor (para uso em endpoints)
     * Retorna o DTO do produto do sistema que corresponde ao código informado pelo fornecedor
     */
    public Optional<com.example.lite_erp.entities.produtos.ProdutosResponseDTO> buscarProdutoDTOPorCodigoFornecedor(String codigoFornecedor, Integer idFornecedor) {
        return repository.findByCodigoFornecedorAndFornecedorId(codigoFornecedor, idFornecedor)
                .map(ProdutoFornecedorCodigo::getProduto)
                .map(com.example.lite_erp.entities.produtos.ProdutosResponseDTO::new);
    }

    /**
     * Busca paginada por produto
     */
    public Page<ProdutoFornecedorCodigoBuscaResponseDTO> buscarPorProdutoPaginado(Long idProduto, Pageable pageable) {
        return repository.findByProdutoIdForBusca(idProduto, pageable);
    }

    /**
     * Busca paginada por fornecedor
     */
    public Page<ProdutoFornecedorCodigoBuscaResponseDTO> buscarPorFornecedorPaginado(Integer idFornecedor, Pageable pageable) {
        return repository.findByFornecedorIdForBusca(idFornecedor, pageable);
    }

    /**
     * Busca paginada com filtro
     */
    public Page<ProdutoFornecedorCodigoBuscaResponseDTO> buscarComFiltro(String filtro, Pageable pageable) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return repository.findAllForBusca(pageable);
        }
        return repository.findByFiltro(filtro, pageable);
    }

    /**
     * Busca DTO simplificado por ID
     */
    public Optional<ProdutoFornecedorCodigoBuscaResponseDTO> buscarDTOPorId(Long id) {
        return repository.findById(id).map(vinculo -> 
            new ProdutoFornecedorCodigoBuscaResponseDTO(
                vinculo.getId(),
                vinculo.getProduto().getId(),
                vinculo.getProduto().getDescricao(),
                vinculo.getFornecedor().getId(),
                vinculo.getFornecedor().getRazaoSocial(),
                vinculo.getCodigoFornecedor(),
                vinculo.getAtivo()
            )
        );
    }
}

