package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.clientes.Clientes;
import com.example.lite_erp.entities.clientes.ClientesBuscaResponseDTO;
import com.example.lite_erp.entities.clientes.ClientesRequestDTO;
import com.example.lite_erp.entities.clientes.ClientesResponseDTO;
import com.example.lite_erp.services.ClientesService;
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
@RequestMapping("/clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientesController {

    @Autowired
    private ClientesService clientesService;

    @GetMapping
    public List<ClientesResponseDTO> listarTodos() {
        List<Clientes> clientes = clientesService.listarTodos();
        return clientes.stream().map(ClientesResponseDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientesResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<Clientes> cliente = clientesService.buscarPorId(id);
        return cliente.map(value -> ResponseEntity.ok(new ClientesResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClientesResponseDTO> criarCliente(@RequestBody ClientesRequestDTO dto) {
        Clientes novoCliente = clientesService.criarCliente(dto);
        return ResponseEntity.ok(new ClientesResponseDTO(novoCliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientesResponseDTO> atualizarCliente(@PathVariable Long id, @RequestBody ClientesRequestDTO dto) {
        Optional<Clientes> clienteAtualizado = clientesService.atualizarCliente(id, dto);
        return clienteAtualizado.map(value -> ResponseEntity.ok(new ClientesResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        if (clientesService.deletarCliente(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClientesResponseDTO>> getClientesByName(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("razaoSocial").ascending());
        Page<Clientes> clientesPage;

        if (nome != null && !nome.isEmpty()) {
            clientesPage = clientesService.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            clientesPage = clientesService.listarTodosPaginado(pageable);
        }

        List<ClientesResponseDTO> clientesDTO = clientesPage
                .getContent()
                .stream()
                .map(ClientesResponseDTO::new)
                .toList();

        return ResponseEntity.ok(clientesDTO);
    }

    @GetMapping("/busca")
    public Page<ClientesBuscaResponseDTO> buscarClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return clientesService.buscarClientes(pageRequest);
    }

    @GetMapping("/busca-por-razao-social")
    public ResponseEntity<Page<ClientesBuscaResponseDTO>> buscarClientesPorRazaoSocial(
            @RequestParam String razaoSocial,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ClientesBuscaResponseDTO> clientes = clientesService.buscarClientesPorRazaoSocial(razaoSocial, pageable);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/busca/{id}")
    public ResponseEntity<ClientesBuscaResponseDTO> simplesBuscaPorId(@PathVariable Long id) {
        return clientesService.simplesBuscaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
