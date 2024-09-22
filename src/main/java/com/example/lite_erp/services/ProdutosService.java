package com.example.lite_erp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.lite_erp.entities.produtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutosService {

    @Autowired
    private ProdutosRepository produtosRepository;

    public List<Produtos> listarTodos() {
        return produtosRepository.findAll();
    }

    public Optional<Produtos> buscarPorId(Long id) {
        return produtosRepository.findById(id);
    }

    public Produtos criarProduto(ProdutosRequestDTO dto) {
        Produtos produto = new Produtos(
                null,
                dto.descricao(),
                dto.grupoProdutos(),
                dto.marca(),
                dto.dataUltimaCompra(),
                dto.precoCompra(),
                dto.precoVenda(),
                dto.peso(),
                dto.codEan(),
                dto.codNcm(),
                dto.codCest()
        );
        return produtosRepository.save(produto);
    }

    public Optional<Produtos> atualizarProduto(Long id, ProdutosRequestDTO dto) {
        return produtosRepository.findById(id).map(produto -> {
            produto.setDescricao(dto.descricao());
            produto.setGrupoProdutos(dto.grupoProdutos());
            produto.setMarca(dto.marca());
            produto.setDataUltimaCompra(dto.dataUltimaCompra());
            produto.setPrecoCompra(dto.precoCompra());
            produto.setPrecoVenda(dto.precoVenda());
            produto.setPeso(dto.peso());
            produto.setCodEan(dto.codEan());
            produto.setCodNcm(dto.codNcm());
            produto.setCodCest(dto.codCest());
            return produtosRepository.save(produto);
        });
    }

    public boolean deletarProduto(Long id) {
        if (produtosRepository.existsById(id)) {
            produtosRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Método de busca por nome (lazy load)
    public Page<Produtos> findByNomeContainingIgnoreCase(String nome, Pageable pageable) {
        return produtosRepository.findByDescricaoContainingIgnoreCase(nome, pageable);
    }

    // Listar todos com paginação
    public Page<Produtos> listarTodosPaginado(Pageable pageable) {
        return produtosRepository.findAll(pageable);
    }
}
