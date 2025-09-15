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

    @GetMapping("/buscar")
    public Page<ContasPagarResponseDTO> buscarContasPagarComFiltro(
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Pageable pageable) {

        return contasPagarService.buscarContasPagarComFiltro(razaoSocial, dataInicio, dataFim, pageable);
    }

    @PutMapping("/{id}")
    public ContasPagarResponseDTO atualizarContaPagar(@PathVariable Long id, @RequestBody ContasPagarRequestDTO dto) {
        return contasPagarService.atualizarContaPagar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluirContaPagar(@PathVariable Long id) {
        contasPagarService.excluirContaPagar(id);
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
