package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.produtos.*;
import com.example.lite_erp.services.ProdutosService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
