package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobrancaRepository;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobrancaRequestDTO;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobrancaResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tipos_cobranca")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TiposCobrancaController {

    private final TiposCobrancaRepository repository;

    public TiposCobrancaController(TiposCobrancaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TiposCobrancaResponseDTO> listarTodos() {
        List<TiposCobranca> tiposCobranca = repository.findAll();
        return tiposCobranca.stream().map(TiposCobrancaResponseDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TiposCobrancaResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<TiposCobranca> tiposCobranca = repository.findById(id);
        return tiposCobranca.map(value -> ResponseEntity.ok(new TiposCobrancaResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TiposCobrancaResponseDTO> criar(@RequestBody TiposCobrancaRequestDTO dto) {
        TiposCobranca novoTipoCobranca = new TiposCobranca();
        novoTipoCobranca.setDescricao(dto.descricao());
        TiposCobranca tipoCobrancaSalvo = repository.save(novoTipoCobranca);
        return ResponseEntity.ok(new TiposCobrancaResponseDTO(tipoCobrancaSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TiposCobrancaResponseDTO> atualizar(@PathVariable Long id, @RequestBody TiposCobrancaRequestDTO dto) {
        Optional<TiposCobranca> tipoCobrancaExistente = repository.findById(id);
        if (tipoCobrancaExistente.isPresent()) {
            TiposCobranca tipoCobranca = tipoCobrancaExistente.get();
            tipoCobranca.setDescricao(dto.descricao());
            repository.save(tipoCobranca);
            return ResponseEntity.ok(new TiposCobrancaResponseDTO(tipoCobranca));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
