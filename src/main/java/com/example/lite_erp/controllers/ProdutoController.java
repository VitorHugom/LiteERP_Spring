package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.produtos.*;
import com.example.lite_erp.services.ProdutosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

    @Autowired
    private ProdutosService produtosService;

    @GetMapping
    public List<ProdutosResponseDTO> listarTodos() {
        List<Produtos> produtos = produtosService.listarTodos();
        return produtos.stream().map(ProdutosResponseDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutosResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<Produtos> produto = produtosService.buscarPorId(id);
        return produto.map(value -> ResponseEntity.ok(new ProdutosResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProdutosResponseDTO> criarProduto(@RequestBody ProdutosRequestDTO dto) {
        Produtos novoProduto = produtosService.criarProduto(dto);
        return ResponseEntity.ok(new ProdutosResponseDTO(novoProduto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutosResponseDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutosRequestDTO dto) {
        Optional<Produtos> produtoAtualizado = produtosService.atualizarProduto(id, dto);
        return produtoAtualizado.map(value -> ResponseEntity.ok(new ProdutosResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        if (produtosService.deletarProduto(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProdutosResponseDTO>> getProdutosByName(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("descricao").ascending());
        Page<Produtos> produtosPage;

        if (nome != null && !nome.isEmpty()) {
            produtosPage = produtosService.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            produtosPage = produtosService.listarTodosPaginado(pageable);
        }

        List<ProdutosResponseDTO> produtosDTO = produtosPage
                .getContent()
                .stream()
                .map(ProdutosResponseDTO::new)
                .toList();

        return ResponseEntity.ok(produtosDTO);
    }

    @GetMapping("/busca")
    public Page<ProdutosBuscaResponseDTO> buscarProdutos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return produtosService.buscarProdutos(pageRequest);
    }

    @GetMapping("/busca-por-descricao")
    public ResponseEntity<Page<ProdutosBuscaResponseDTO>> buscarProdutosPorDescricao(
            @RequestParam String descricao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProdutosBuscaResponseDTO> pedidos = produtosService.buscarProdutosPorDescricao(descricao, pageable);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/busca/{id}")
    public ResponseEntity<ProdutosBuscaResponseDTO> simplesBuscaPorId(@PathVariable Long id) {
        return produtosService.simplesBuscaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/busca-por-descricao-cod-ean")
    public ResponseEntity<Page<ProdutosBuscaResponseDTO>> buscarProdutosPorDescricaoCodEan(
            @RequestParam String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProdutosBuscaResponseDTO> pedidos = produtosService.buscarProdutosPorDescricaoCodEan(nome, pageable);
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping("/relatorios")
    public ResponseEntity<List<ProdutosResponseDTO>> gerarRelatorioProdutos(
            @RequestBody ProdutosFiltroDTO filtros
    ) {
        List<ProdutosResponseDTO> dtos = produtosService.filtrarProdutos(filtros);
        return ResponseEntity.ok(dtos);
    }
}
