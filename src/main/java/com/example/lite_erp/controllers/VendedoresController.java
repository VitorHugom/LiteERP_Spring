package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.vendedores.Vendedores;
import com.example.lite_erp.entities.vendedores.VendedoresRepository;
import com.example.lite_erp.entities.vendedores.VendedoresRequestDTO;
import com.example.lite_erp.entities.vendedores.VendedoresResponseDTO;
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
@RequestMapping("/vendedores")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VendedoresController {

    private final VendedoresRepository repository;

    public VendedoresController(VendedoresRepository repository) {
        this.repository = repository;
    }

    // GET: Retornar todos os vendedores
    @GetMapping
    public ResponseEntity<List<VendedoresResponseDTO>> getAllVendedores() {
        List<Vendedores> vendedoresList = repository.findAll();

        List<VendedoresResponseDTO> vendedoresDTO = vendedoresList
                .stream()
                .map(VendedoresResponseDTO::new)
                .toList();

        return ResponseEntity.ok(vendedoresDTO);
    }


    // GET: Listar vendedores com lazy loading (autocomplete)
    @GetMapping("/search")
    public ResponseEntity<List<VendedoresResponseDTO>> getVendedoresByName(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
        Page<Vendedores> vendedoresPage;

        if (nome != null && !nome.isEmpty()) {
            vendedoresPage = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            vendedoresPage = repository.findAll(pageable);
        }

        List<VendedoresResponseDTO> vendedoresDTO = vendedoresPage
                .getContent()
                .stream()
                .map(VendedoresResponseDTO::new)
                .toList();

        return ResponseEntity.ok(vendedoresDTO);
    }

    // GET: Obter vendedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<VendedoresResponseDTO> getVendedorById(@PathVariable Long id) {
        Optional<Vendedores> optionalVendedor = repository.findById(id);
        if (optionalVendedor.isPresent()) {
            return ResponseEntity.ok(new VendedoresResponseDTO(optionalVendedor.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // POST: Criar um novo vendedor
    @PostMapping
    public ResponseEntity<VendedoresResponseDTO> createVendedor(@RequestBody VendedoresRequestDTO requestDTO) {
        Vendedores vendedor = new Vendedores();
        vendedor.setNome(requestDTO.nome());
        vendedor.setEmail(requestDTO.email());
        vendedor.setTelefone(requestDTO.telefone());

        Vendedores savedVendedor = repository.save(vendedor);

        return ResponseEntity.status(HttpStatus.CREATED).body(new VendedoresResponseDTO(savedVendedor));
    }

    // PUT: Atualizar um vendedor existente
    @PutMapping("/{id}")
    public ResponseEntity<VendedoresResponseDTO> updateVendedor(
            @PathVariable Long id,
            @RequestBody VendedoresRequestDTO requestDTO
    ) {
        Optional<Vendedores> optionalVendedor = repository.findById(id);

        if (optionalVendedor.isPresent()) {
            Vendedores vendedor = optionalVendedor.get();
            vendedor.setNome(requestDTO.nome());
            vendedor.setEmail(requestDTO.email());
            vendedor.setTelefone(requestDTO.telefone());

            Vendedores updatedVendedor = repository.save(vendedor);

            return ResponseEntity.ok(new VendedoresResponseDTO(updatedVendedor));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // DELETE: Remover um vendedor por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendedor(@PathVariable Long id) {
        Optional<Vendedores> optionalVendedor = repository.findById(id);

        if (optionalVendedor.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
