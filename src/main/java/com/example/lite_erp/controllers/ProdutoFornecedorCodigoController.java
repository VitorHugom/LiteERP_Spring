package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.produto_fornecedor_codigo.*;
import com.example.lite_erp.entities.produtos.ProdutosResponseDTO;
import com.example.lite_erp.services.ProdutoFornecedorCodigoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/produto-fornecedor-codigo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoFornecedorCodigoController {

    private static final String TAG_PRODUTO_FORNECEDOR = "Produto Fornecedor Código";

    @Autowired
    private ProdutoFornecedorCodigoService service;

    @Operation(
            summary = "Listar todos os vínculos de código de produto no fornecedor",
            description = "Retorna todos os vínculos cadastrados entre produtos e códigos de fornecedores",
            tags = {TAG_PRODUTO_FORNECEDOR}
    )
    @GetMapping
    public List<ProdutoFornecedorCodigoResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @Operation(
            summary = "Buscar vínculo por ID",
            description = "Retorna os dados completos de um vínculo específico entre produto e código do fornecedor",
            tags = {TAG_PRODUTO_FORNECEDOR}
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoFornecedorCodigoResponseDTO> buscarPorId(
            @Parameter(description = "ID do vínculo", example = "1") @PathVariable Long id) {
        Optional<ProdutoFornecedorCodigo> vinculo = service.buscarPorId(id);
        return vinculo.map(value -> ResponseEntity.ok(new ProdutoFornecedorCodigoResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Criar novo vínculo",
            description = "Cria um novo vínculo entre produto e código do fornecedor. Valida duplicidade de vínculo produto-fornecedor.",
            tags = {TAG_PRODUTO_FORNECEDOR}
    )
    @PostMapping
    public ResponseEntity<ProdutoFornecedorCodigoResponseDTO> criarVinculo(@RequestBody ProdutoFornecedorCodigoRequestDTO dto) {
        try {
            ProdutoFornecedorCodigo novoVinculo = service.criarVinculo(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ProdutoFornecedorCodigoResponseDTO(novoVinculo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Atualizar vínculo",
            description = "Atualiza os dados de um vínculo existente entre produto e código do fornecedor",
            tags = {TAG_PRODUTO_FORNECEDOR}
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoFornecedorCodigoResponseDTO> atualizarVinculo(
            @Parameter(description = "ID do vínculo", example = "1") @PathVariable Long id,
            @RequestBody ProdutoFornecedorCodigoRequestDTO dto) {
        try {
            Optional<ProdutoFornecedorCodigo> vinculoAtualizado = service.atualizarVinculo(id, dto);
            return vinculoAtualizado.map(value -> ResponseEntity.ok(new ProdutoFornecedorCodigoResponseDTO(value)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Deletar vínculo",
            description = "Remove um vínculo entre produto e código do fornecedor",
            tags = {TAG_PRODUTO_FORNECEDOR}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVinculo(
            @Parameter(description = "ID do vínculo", example = "1") @PathVariable Long id) {
        if (service.deletarVinculo(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Buscar vínculos com paginação",
            description = "Retorna uma lista paginada de vínculos, com opção de filtro por nome do produto, fornecedor ou código",
            tags = {TAG_PRODUTO_FORNECEDOR},
            parameters = {
                    @Parameter(
                            name = "filtro",
                            description = "Filtro de busca por nome do produto, fornecedor ou código",
                            example = "Dell",
                            schema = @Schema(implementation = String.class)
                    ),
                    @Parameter(
                            name = "page",
                            description = "Número da página",
                            example = "0",
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            description = "Quantidade de itens por página",
                            example = "10",
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sortBy",
                            description = "Campo para ordenação",
                            example = "id",
                            schema = @Schema(implementation = String.class)
                    ),
                    @Parameter(
                            name = "direction",
                            description = "Direção da ordenação (ASC ou DESC)",
                            example = "DESC",
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping("/busca")
    public Page<ProdutoFornecedorCodigoBuscaResponseDTO> buscarVinculos(
            @RequestParam(required = false) String filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        if (filtro != null && !filtro.trim().isEmpty()) {
            return service.buscarComFiltro(filtro, pageable);
        }
        return service.listarTodosPaginado(pageable);
    }

    @Operation(
            summary = "Listar vínculos por produto",
            description = "Retorna todos os vínculos de códigos de fornecedores para um produto específico",
            tags = {TAG_PRODUTO_FORNECEDOR}
    )
    @GetMapping("/produto/{idProduto}")
    public ResponseEntity<List<ProdutoFornecedorCodigoResponseDTO>> buscarPorProduto(
            @Parameter(description = "ID do produto", example = "15") @PathVariable Long idProduto) {
        List<ProdutoFornecedorCodigoResponseDTO> vinculos = service.buscarPorProduto(idProduto);
        return ResponseEntity.ok(vinculos);
    }

    @Operation(
            summary = "Buscar vínculos por produto com paginação",
            description = "Retorna uma lista paginada de vínculos de códigos de fornecedores para um produto específico",
            tags = {TAG_PRODUTO_FORNECEDOR},
            parameters = {
                    @Parameter(
                            name = "page",
                            description = "Número da página",
                            example = "0",
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            description = "Quantidade de itens por página",
                            example = "10",
                            schema = @Schema(implementation = Integer.class)
                    )
            }
    )
    @GetMapping("/produto/{idProduto}/paginado")
    public Page<ProdutoFornecedorCodigoBuscaResponseDTO> buscarPorProdutoPaginado(
            @Parameter(description = "ID do produto", example = "15") @PathVariable Long idProduto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return service.buscarPorProdutoPaginado(idProduto, pageable);
    }

    @Operation(
            summary = "Listar vínculos por fornecedor",
            description = "Retorna todos os vínculos de produtos para um fornecedor específico",
            tags = {TAG_PRODUTO_FORNECEDOR}
    )
    @GetMapping("/fornecedor/{idFornecedor}")
    public ResponseEntity<List<ProdutoFornecedorCodigoResponseDTO>> buscarPorFornecedor(
            @Parameter(description = "ID do fornecedor", example = "3") @PathVariable Integer idFornecedor) {
        List<ProdutoFornecedorCodigoResponseDTO> vinculos = service.buscarPorFornecedor(idFornecedor);
        return ResponseEntity.ok(vinculos);
    }

    @Operation(
            summary = "Buscar vínculos por fornecedor com paginação",
            description = "Retorna uma lista paginada de vínculos de produtos para um fornecedor específico",
            tags = {TAG_PRODUTO_FORNECEDOR},
            parameters = {
                    @Parameter(
                            name = "page",
                            description = "Número da página",
                            example = "0",
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            description = "Quantidade de itens por página",
                            example = "10",
                            schema = @Schema(implementation = Integer.class)
                    )
            }
    )
    @GetMapping("/fornecedor/{idFornecedor}/paginado")
    public Page<ProdutoFornecedorCodigoBuscaResponseDTO> buscarPorFornecedorPaginado(
            @Parameter(description = "ID do fornecedor", example = "3") @PathVariable Integer idFornecedor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return service.buscarPorFornecedorPaginado(idFornecedor, pageable);
    }

    @Operation(
            summary = "Buscar produto pelo código do fornecedor",
            description = "Retorna o produto do sistema que corresponde ao código informado pelo fornecedor. Utilizado principalmente na importação de NFe para vincular automaticamente os itens aos produtos cadastrados.",
            tags = {TAG_PRODUTO_FORNECEDOR},
            parameters = {
                    @Parameter(
                            name = "codigoFornecedor",
                            description = "Código do produto utilizado pelo fornecedor",
                            example = "DELL-NB-INS15-2024",
                            schema = @Schema(implementation = String.class)
                    ),
                    @Parameter(
                            name = "idFornecedor",
                            description = "ID do fornecedor",
                            example = "3",
                            schema = @Schema(implementation = Integer.class)
                    )
            }
    )
    @GetMapping("/buscar-produto")
    public ResponseEntity<ProdutosResponseDTO> buscarProdutoPorCodigoFornecedor(
            @RequestParam String codigoFornecedor,
            @RequestParam Integer idFornecedor) {

        Optional<ProdutosResponseDTO> produto = service.buscarProdutoDTOPorCodigoFornecedor(codigoFornecedor, idFornecedor);
        return produto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Buscar vínculo simplificado por ID",
            description = "Retorna os dados simplificados de um vínculo específico",
            tags = {TAG_PRODUTO_FORNECEDOR}
    )
    @GetMapping("/busca/{id}")
    public ResponseEntity<ProdutoFornecedorCodigoBuscaResponseDTO> buscarDTOPorId(
            @Parameter(description = "ID do vínculo", example = "1") @PathVariable Long id) {
        return service.buscarDTOPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

