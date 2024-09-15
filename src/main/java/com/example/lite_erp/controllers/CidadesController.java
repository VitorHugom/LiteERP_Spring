package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.cidades.Cidades;
import com.example.lite_erp.entities.cidades.CidadesRepository;
import com.example.lite_erp.entities.cidades.CidadesResponseDTO;
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
@RequestMapping("/cidades")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CidadesController {

    private final CidadesRepository repository;

    public CidadesController(CidadesRepository repository) {
        this.repository = repository;
    }

    // GET: Listar cidades com lazy loading (autocomplete)
    @GetMapping("/search")
    public ResponseEntity<List<CidadesResponseDTO>> getCidadesByName(
            @RequestParam(required = false) String nome,  // termo de busca
            @RequestParam(defaultValue = "0") int page,   // página atual
            @RequestParam(defaultValue = "10") int size   // tamanho da página
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
        Page<Cidades> cidadesPage;

        if (nome != null && !nome.isEmpty()) {
            cidadesPage = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            cidadesPage = repository.findAll(pageable);
        }

        List<CidadesResponseDTO> cidadesDTO = cidadesPage
                .getContent()
                .stream()
                .map(CidadesResponseDTO::new)
                .toList();

        return ResponseEntity.ok(cidadesDTO);
    }

    // GET: Obter cidade por ID
    @GetMapping("/{id}")
    public ResponseEntity<CidadesResponseDTO> getCidadeById(@PathVariable Long id) {
        Optional<Cidades> optionalCidade = repository.findById(id);
        if (optionalCidade.isPresent()) {
            return ResponseEntity.ok(new CidadesResponseDTO(optionalCidade.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Endpoint para buscar cidade pelo código IBGE
    @GetMapping("/codigoIbge/{codigoIbge}")
    public ResponseEntity<Cidades> buscarPorCodigoIbge(@PathVariable String codigoIbge) {
        Optional<Cidades> cidade = repository.findByCodigoIbge(codigoIbge);

        return cidade.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
