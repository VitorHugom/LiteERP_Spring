package com.example.lite_erp.services;

import com.example.lite_erp.entities.estoque.*;
import com.example.lite_erp.entities.produtos.Produtos;
import com.example.lite_erp.entities.produtos.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutosRepository produtosRepository;

    @Transactional
    public EstoqueResponseDTO atualizarEstoque(EstoqueRequestDTO dto) {
        // Buscar o produto
        Produtos produto = produtosRepository.findById(dto.idProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Buscar o estoque existente ou criar um novo
        Estoque estoque = estoqueRepository.findByProdutoId(dto.idProduto())
                .orElseGet(() -> new Estoque(null, produto, BigDecimal.ZERO));

        // Atualizar a quantidade em estoque
        estoque.setQtdEstoque(estoque.getQtdEstoque().add(dto.qtdEstoque()));
        estoqueRepository.save(estoque);

        return new EstoqueResponseDTO(estoque);
    }

    // Método para buscar todos os produtos no estoque paginados
    public Page<EstoqueResponseDTO> buscarTodosProdutosNoEstoque(Pageable pageable) {
        return estoqueRepository.findAllEstoque(pageable).map(EstoqueResponseDTO::new);
    }

    // Método para buscar produtos no estoque pela descrição
    public Page<EstoqueResponseDTO> buscarProdutosNoEstoquePorDescricao(String descricao, Pageable pageable) {
        return estoqueRepository.findByProdutoDescricaoContainingIgnoreCase(descricao, pageable).map(EstoqueResponseDTO::new);
    }

    public EstoqueResponseDTO obterEstoquePorProduto(Long idProduto) {
        Estoque estoque = estoqueRepository.findByProdutoId(idProduto)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o produto"));
        return new EstoqueResponseDTO(estoque);
    }

    public List<EstoqueResponseDTO> filtrarEstoque(EstoqueFiltroDTO filtro) {
        Long grupoId = filtro.grupoId();

        List<Estoque> lista = estoqueRepository.filterEstoque(grupoId);

        return lista.stream()
                .map(EstoqueResponseDTO::new)
                .collect(Collectors.toList());
    }
}
