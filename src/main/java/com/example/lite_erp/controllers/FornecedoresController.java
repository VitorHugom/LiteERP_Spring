package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.fornecedores.*;
import com.example.lite_erp.services.FornecedoresService;
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
@RequestMapping("/fornecedores")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FornecedoresController {

    @Autowired
    private FornecedoresService fornecedoresService;

    @GetMapping
    public List<FornecedoresResponseDTO> listarTodos() {
        List<Fornecedores> fornecedores = fornecedoresService.listarTodos();
        return fornecedores.stream().map(FornecedoresResponseDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedoresResponseDTO> buscarPorId(@PathVariable Integer id) {
        Optional<Fornecedores> fornecedor = fornecedoresService.buscarPorId(id);
        return fornecedor.map(value -> ResponseEntity.ok(new FornecedoresResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FornecedoresResponseDTO> criarFornecedor(@RequestBody FornecedoresRequestDTO dto) {
        Fornecedores novoFornecedor = fornecedoresService.criarFornecedor(dto);
        return ResponseEntity.ok(new FornecedoresResponseDTO(novoFornecedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedoresResponseDTO> atualizarFornecedor(@PathVariable Integer id, @RequestBody FornecedoresRequestDTO dto) {
        Optional<Fornecedores> fornecedorAtualizado = fornecedoresService.atualizarFornecedor(id, dto);
        return fornecedorAtualizado.map(value -> ResponseEntity.ok(new FornecedoresResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable Integer id) {
        if (fornecedoresService.deletarFornecedor(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<FornecedoresResponseDTO>> getFornecedoresByName(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("razaoSocial").ascending());
        Page<Fornecedores> fornecedoresPage;

        if (nome != null && !nome.isEmpty()) {
            fornecedoresPage = fornecedoresService.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            fornecedoresPage = fornecedoresService.listarTodosPaginado(pageable);
        }

        List<FornecedoresResponseDTO> fornecedoresDTO = fornecedoresPage
                .getContent()
                .stream()
                .map(FornecedoresResponseDTO::new)
                .toList();

        return ResponseEntity.ok(fornecedoresDTO);
    }

    @GetMapping("/busca")
    public Page<FornecedoresBuscaResponseDTO> buscarFornecedores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return fornecedoresService.buscarFornecedores(pageRequest);
    }

    @GetMapping("/busca-por-razao-social")
    public ResponseEntity<Page<FornecedoresBuscaResponseDTO>> buscarFornecedoresPorRazaoSocial(
            @RequestParam String razaoSocial,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<FornecedoresBuscaResponseDTO> fornecedores = fornecedoresService.buscarFornecedoresPorRazaoSocial(razaoSocial, pageable);
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/busca/{id}")
    public ResponseEntity<FornecedoresBuscaResponseDTO> simplesBuscaPorId(@PathVariable Integer id) {
        return fornecedoresService.simplesBuscaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/relatorios")
    public ResponseEntity<List<FornecedoresResponseDTO>> relatorioFornecedores(
            @RequestBody FornecedoresFiltroDTO filtro
    ) {
        List<FornecedoresResponseDTO> resultado = fornecedoresService.filtrarFornecedores(filtro);
        return ResponseEntity.ok(resultado);
    }
}
