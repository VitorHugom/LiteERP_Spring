package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.estoque.*;
import com.example.lite_erp.services.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoque")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    // Endpoint para atualizar o estoque
    @PostMapping("/atualizar")
    public ResponseEntity<EstoqueResponseDTO> atualizarEstoque(@RequestBody EstoqueRequestDTO dto) {
        EstoqueResponseDTO response = estoqueService.atualizarEstoque(dto);
        return ResponseEntity.ok(response);
    }

    // Endpoint para obter o estoque de um produto
    @GetMapping("/produto/{idProduto}")
    public ResponseEntity<EstoqueResponseDTO> obterEstoquePorProduto(@PathVariable Long idProduto) {
        EstoqueResponseDTO response = estoqueService.obterEstoquePorProduto(idProduto);
        return ResponseEntity.ok(response);
    }

    // Endpoint para buscar todos os produtos no estoque paginados
    @GetMapping("/todos")
    public ResponseEntity<Page<EstoqueResponseDTO>> buscarTodosProdutosNoEstoque(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstoqueResponseDTO> response = estoqueService.buscarTodosProdutosNoEstoque(pageable);
        return ResponseEntity.ok(response);
    }

    // Endpoint para buscar produtos no estoque pela descrição
    @GetMapping("/buscar-por-descricao")
    public ResponseEntity<Page<EstoqueResponseDTO>> buscarProdutosNoEstoquePorDescricao(
            @RequestParam(value = "descricao") String descricao,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstoqueResponseDTO> response = estoqueService.buscarProdutosNoEstoquePorDescricao(descricao, pageable);
        return ResponseEntity.ok(response);
    }
}
