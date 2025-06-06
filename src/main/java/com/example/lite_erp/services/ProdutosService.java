package com.example.lite_erp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.lite_erp.entities.produtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutosService {

    @Autowired
    private ProdutosRepository produtosRepository;

    public List<Produtos> listarTodos() {
        return produtosRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Optional<Produtos> buscarPorId(Long id) {
        return produtosRepository.findById(id);
    }

    public Optional<ProdutosBuscaResponseDTO> simplesBuscaPorId(Long id) {
        return produtosRepository.findById(id)
                .map(produto -> new ProdutosBuscaResponseDTO(
                        produto.getId(),
                        produto.getDescricao(),
                        produto.getPrecoVenda()
                ));
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

    public Page<ProdutosBuscaResponseDTO> buscarProdutos(Pageable pageable) {
        return produtosRepository.findProdutosForBusca(pageable);
    }

    public Page<ProdutosBuscaResponseDTO> buscarProdutosPorDescricao(String descricao, Pageable pageable) {
        return produtosRepository.findProdutosForBuscaByDescricao(descricao + "%", pageable);
    }

    public Page<ProdutosBuscaResponseDTO> buscarProdutosPorDescricaoCodEan(String busca, Pageable pageable){
        return produtosRepository.findProdutosForBuscaByDescricaoCodEan(busca+"%", pageable);
    }

    public List<ProdutosResponseDTO> filtrarProdutos(ProdutosFiltroDTO filtro) {
        var dataInicio         = filtro.dataCompraInicio();
        var dataFim            = filtro.dataCompraFim();
        var grupoId            = filtro.grupoId();
        var precoVendaInicio   = filtro.precoVendaInicio();
        var precoVendaFim      = filtro.precoVendaFim();
        var precoCompraInicio  = filtro.precoCompraInicio();
        var precoCompraFim     = filtro.precoCompraFim();
        var pesoInicio         = filtro.pesoInicio();
        var pesoFim            = filtro.pesoFim();

        List<Produtos> lista = produtosRepository.filterProdutos(
                dataInicio,
                dataFim,
                grupoId,
                precoVendaInicio,
                precoVendaFim,
                precoCompraInicio,
                precoCompraFim,
                pesoInicio,
                pesoFim
        );

        return lista.stream()
                .map(ProdutosResponseDTO::new)
                .collect(Collectors.toList());
    }
}
