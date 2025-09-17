package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.contas_pagar.ContasPagarFiltroDTO;
import com.example.lite_erp.entities.contas_pagar.ContasPagarRelatorioFiltroDTO;
import com.example.lite_erp.entities.contas_pagar.ContasPagarRelatorioResponseDTO;
import com.example.lite_erp.entities.contas_pagar.ContasPagarRequestDTO;
import com.example.lite_erp.entities.contas_pagar.ContasPagarResponseDTO;
import com.example.lite_erp.services.ContasPagarService;
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
@RequestMapping("/contas-pagar")
public class ContasPagarController {

    @Autowired
    private ContasPagarService contasPagarService;

    @PostMapping
    public ContasPagarResponseDTO criarContaPagar(@RequestBody ContasPagarRequestDTO dto) {
        return contasPagarService.salvarContaPagar(dto);
    }

    @GetMapping
    public List<ContasPagarResponseDTO> listarContasPagar() {
        return contasPagarService.listarContasPagar();
    }

    @GetMapping("/{id}")
    public ContasPagarResponseDTO buscarContaPagarPorId(@PathVariable Long id) {
        return contasPagarService.buscarContaPagarPorId(id);
    }

    @Operation(
            summary = "Buscar contas a pagar com filtros",
            description = "Busca contas a pagar aplicando filtros opcionais por razão social do fornecedor, período de vencimento e fornecedor específico. Retorna resultados paginados.",
            tags = {"Contas a Pagar"},
            parameters = {
                    @Parameter(
                            name = "razaoSocial",
                            description = "Filtro por razão social do fornecedor (busca parcial, ignora maiúsculas/minúsculas)",
                            example = "Fornecedor ABC Ltda",
                            schema = @Schema(implementation = String.class)
                    ),
                    @Parameter(
                            name = "dataInicio",
                            description = "Data inicial do período para filtrar as contas a pagar por data de vencimento",
                            example = "2024-01-01",
                            schema = @Schema(implementation = LocalDate.class)
                    ),
                    @Parameter(
                            name = "dataFim",
                            description = "Data final do período para filtrar as contas a pagar por data de vencimento",
                            example = "2024-12-31",
                            schema = @Schema(implementation = LocalDate.class)
                    ),
                    @Parameter(
                            name = "idFornecedor",
                            description = "ID do fornecedor para filtrar todas as contas a pagar deste fornecedor específico",
                            example = "123",
                            schema = @Schema(implementation = Long.class)
                    ),
                    @Parameter(
                            name = "somenteReceber",
                            description = "Se true, retorna apenas contas com status 'aberta' (não pagas). Se false ou não informado, retorna todas as contas independente do status",
                            example = "true",
                            schema = @Schema(implementation = Boolean.class)
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Busca realizada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Page.class)
                    )
            )
    })
    @GetMapping("/buscar")
    public Page<ContasPagarResponseDTO> buscarContasPagarComFiltro(
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) Long idFornecedor,
            @RequestParam(required = false) Boolean somentePagar,
            @Parameter(hidden = true) Pageable pageable) {

        return contasPagarService.buscarContasPagarComFiltro(razaoSocial, dataInicio, dataFim, idFornecedor, somentePagar, pageable);
    }

    @PutMapping("/{id}")
    public ContasPagarResponseDTO atualizarContaPagar(@PathVariable Long id, @RequestBody ContasPagarRequestDTO dto) {
        return contasPagarService.atualizarContaPagar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluirContaPagar(@PathVariable Long id) {
        contasPagarService.excluirContaPagar(id);
    }

    @Operation(
            summary = "Realizar pagamento de conta a pagar",
            description = "Realiza o pagamento de uma conta a pagar, alterando seu status de 'aberta' para 'paga'.",
            tags = {"Contas a Pagar"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagamento realizado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ContasPagarResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conta a pagar não encontrada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Conta já foi paga"
            )
    })
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<ContasPagarResponseDTO> realizarPagamento(
            @Parameter(description = "ID da conta a pagar", example = "1")
            @PathVariable Long id) {
        ContasPagarResponseDTO contaPaga = contasPagarService.realizarPagamento(id);
        return ResponseEntity.ok(contaPaga);
    }


    @PostMapping("/relatorios")
    public ResponseEntity<List<ContasPagarResponseDTO>> gerarRelatorioContasPagar(
            @RequestBody ContasPagarFiltroDTO filtros
    ) {
        List<ContasPagarResponseDTO> dtos = contasPagarService.filtrarContasPagar(filtros);
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Gerar relatório de contas a pagar para gráfico",
            description = "Retorna dados agregados de contas a pagar agrupados por data de vencimento, com valor total, quantidade e lista de IDs das parcelas por dia. Ideal para geração de gráficos no frontend.",
            tags = {"Contas a Pagar"},
            parameters = {
                    @Parameter(
                            name = "dataInicio",
                            description = "Data inicial do período para filtrar as contas a pagar",
                            example = "2024-01-01",
                            schema = @Schema(implementation = LocalDate.class)
                    ),
                    @Parameter(
                            name = "dataFim",
                            description = "Data final do período para filtrar as contas a pagar",
                            example = "2024-12-31",
                            schema = @Schema(implementation = LocalDate.class)
                    ),
                    @Parameter(
                            name = "status",
                            description = "Status das contas a incluir no relatório. Se não informado, retorna todas as contas",
                            example = "em_aberto",
                            schema = @Schema(implementation = String.class)
                    ),
                    @Parameter(
                            name = "idFornecedor",
                            description = "ID do fornecedor para filtrar as contas a pagar",
                            example = "1",
                            schema = @Schema(implementation = Long.class)
                    ),
                    @Parameter(
                            name = "idFormaPagamento",
                            description = "ID da forma de pagamento para filtrar as contas a pagar",
                            example = "2",
                            schema = @Schema(implementation = Long.class)
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
                                    "<li><strong>dataVencimento:</strong> Ordena por data de vencimento</li>" +
                                    "<li><strong>valorTotalParcelas:</strong> Ordena por valor total das parcelas</li>" +
                                    "<li><strong>qtdParcelas:</strong> Ordena por quantidade de parcelas</li>" +
                                    "</ul>",
                            example = "dataVencimento,asc",
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping("/relatorio-grafico")
    public ResponseEntity<Page<ContasPagarRelatorioResponseDTO>> gerarRelatorioGrafico(
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long idFornecedor,
            @RequestParam(required = false) Long idFormaPagamento,
            @Parameter(hidden = true) Pageable pageable
    ) {
        ContasPagarRelatorioFiltroDTO filtro = new ContasPagarRelatorioFiltroDTO(
                dataInicio,
                dataFim,
                status,
                idFornecedor,
                idFormaPagamento
        );

        Page<ContasPagarRelatorioResponseDTO> relatorio = contasPagarService.gerarRelatorioContasPorData(filtro, pageable);
        return ResponseEntity.ok(relatorio);
    }
}
