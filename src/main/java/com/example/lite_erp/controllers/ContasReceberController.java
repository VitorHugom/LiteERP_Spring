package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.contas_receber.ContasReceberFiltroDTO;
import com.example.lite_erp.entities.contas_receber.ContasReceberRequestDTO;
import com.example.lite_erp.entities.contas_receber.ContasReceberResponseDTO;
import com.example.lite_erp.services.ContasReceberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contas-receber")
public class ContasReceberController {

    @Autowired
    private ContasReceberService contasReceberService;

    @PostMapping
    public ContasReceberResponseDTO criarContaReceber(@RequestBody ContasReceberRequestDTO dto) {
        return contasReceberService.salvarContaReceber(dto);
    }

    @GetMapping
    public List<ContasReceberResponseDTO> listarContasReceber() {
        return contasReceberService.listarContasReceber();
    }

    @GetMapping("/{id}")
    public ContasReceberResponseDTO buscarContaReceberPorId(@PathVariable Integer id) {
        return contasReceberService.buscarContaReceberPorId(id);
    }

    @Operation(
            summary = "Buscar contas a receber com filtros",
            description = "Busca contas a receber aplicando filtros opcionais por razão social do cliente, período de vencimento e cliente específico. Retorna resultados paginados.",
            tags = {"Contas a Receber"},
            parameters = {
                    @Parameter(
                            name = "razaoSocial",
                            description = "Filtro por razão social do cliente (busca parcial, ignora maiúsculas/minúsculas)",
                            example = "João Silva",
                            schema = @Schema(implementation = String.class)
                    ),
                    @Parameter(
                            name = "dataInicio",
                            description = "Data inicial do período para filtrar as contas a receber por data de vencimento",
                            example = "2024-01-01",
                            schema = @Schema(implementation = LocalDate.class)
                    ),
                    @Parameter(
                            name = "dataFim",
                            description = "Data final do período para filtrar as contas a receber por data de vencimento",
                            example = "2024-12-31",
                            schema = @Schema(implementation = LocalDate.class)
                    ),
                    @Parameter(
                            name = "idCliente",
                            description = "ID do cliente para filtrar todas as contas a receber deste cliente específico",
                            example = "123",
                            schema = @Schema(implementation = Integer.class)
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
                            name = "sort",
                            description = "Ordenação no formato atributo,ordem (asc|desc)<br/>" +
                                    "<br/><strong>Campos disponíveis para ordenação:</strong>" +
                                    "<ul>" +
                                    "<li><strong>id:</strong> Ordena por ID da conta</li>" +
                                    "<li><strong>dataVencimento:</strong> Ordena por data de vencimento</li>" +
                                    "<li><strong>valorTotal:</strong> Ordena por valor total</li>" +
                                    "<li><strong>numeroDocumento:</strong> Ordena por número do documento</li>" +
                                    "</ul>",
                            example = "dataVencimento,asc",
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping("/buscar")
    public Page<ContasReceberResponseDTO> buscarContasReceberComFiltro(
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) Integer idCliente,
            @Parameter(hidden = true) Pageable pageable) {
        return contasReceberService.buscarContasReceberComFiltro(razaoSocial, dataInicio, dataFim, idCliente, pageable);
    }

    @PutMapping("/{id}")
    public ContasReceberResponseDTO atualizarContaReceber(
            @PathVariable Integer id,
            @RequestBody ContasReceberRequestDTO dto) {
        return contasReceberService.atualizarContaReceber(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluirContaReceber(@PathVariable Integer id) {
        contasReceberService.excluirContaReceber(id);
    }

    @PostMapping("/relatorios")
    public ResponseEntity<List<ContasReceberResponseDTO>> gerarRelatorioContasReceber(
            @RequestBody ContasReceberFiltroDTO filtros
    ) {
        List<ContasReceberResponseDTO> dtos = contasReceberService.filtrarContasReceber(filtros);
        return ResponseEntity.ok(dtos);
    }
}
