package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.grupo_produto.GrupoProdutos;
import com.example.lite_erp.entities.grupo_produto.GrupoProdutosRepository;
import com.example.lite_erp.entities.grupo_produto.GrupoProdutosRequestDTO;
import com.example.lite_erp.entities.grupo_produto.GrupoProdutosResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/grupo_produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GrupoProdutosController {

    @Autowired
    private GrupoProdutosRepository repository;


    // GET: Listar todos os grupos de produtos
    @GetMapping
    public List<GrupoProdutosResponseDTO> getGrupoProdutos() {
        return repository.findAll().stream().map(GrupoProdutosResponseDTO::new).toList();
    }

    // GET: Obter grupo de produtos por ID
    @GetMapping("/{id}")
    public ResponseEntity<GrupoProdutosResponseDTO> getGrupoProdutosById(@PathVariable Long id) {
        Optional<GrupoProdutos> optionalGrupo = repository.findById(id);
        if (optionalGrupo.isPresent()) {
            return ResponseEntity.ok(new GrupoProdutosResponseDTO(optionalGrupo.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Busca todos os grupos de produtos com paginação
    @GetMapping("/busca")
    public Page<GrupoProdutosResponseDTO> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(GrupoProdutosResponseDTO::new);
    }

    // Busca paginada por nome (contém, ignorando maiúsculas e minúsculas)
    @GetMapping("/busca-por-descricao")
    public Page<GrupoProdutosResponseDTO> findByNomeContainingIgnoreCase(
            @RequestParam String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    // POST: Criar um novo grupo de produtos
    @PostMapping
    public ResponseEntity<GrupoProdutosResponseDTO> createGrupoProdutos(@RequestBody GrupoProdutosRequestDTO requestDTO) {
        GrupoProdutos grupoProdutos = new GrupoProdutos();
        grupoProdutos.setNome(requestDTO.nome());
        GrupoProdutos savedGrupo = repository.save(grupoProdutos);
        return ResponseEntity.status(HttpStatus.CREATED).body(new GrupoProdutosResponseDTO(savedGrupo));
    }

    // PUT: Atualizar um grupo de produtos existente
    @PutMapping("/{id}")
    public ResponseEntity<GrupoProdutosResponseDTO> updateGrupoProdutos(@PathVariable Long id, @RequestBody GrupoProdutosRequestDTO requestDTO) {
        Optional<GrupoProdutos> optionalGrupo = repository.findById(id);
        if (optionalGrupo.isPresent()) {
            GrupoProdutos grupoProdutos = optionalGrupo.get();
            grupoProdutos.setNome(requestDTO.nome());
            GrupoProdutos updatedGrupo = repository.save(grupoProdutos);
            return ResponseEntity.ok(new GrupoProdutosResponseDTO(updatedGrupo));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // DELETE: Excluir um grupo de produtos
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrupoProdutos(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
