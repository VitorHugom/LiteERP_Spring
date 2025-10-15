package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.nfe.NfeDadosExtraidosDTO;
import com.example.lite_erp.entities.nfe.NfeProcessadaResponseDTO;
import com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadorias;
import com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadoriasBuscaResponseDTO;
import com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadoriasRequestDTO;
import com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadoriasResponseDTO;
import com.example.lite_erp.services.NfeProcessamentoService;
import com.example.lite_erp.services.NfeXmlParserService;
import com.example.lite_erp.services.RecebimentoMercadoriasService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recebimento_mercadorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RecebimentoMercadoriasController {

    private static final String TAG_RECEBIMENTO = "Recebimento de Mercadorias";
    @Autowired
    private RecebimentoMercadoriasService recebimentoMercadoriasService;

    @Autowired
    private NfeXmlParserService nfeXmlParserService;

    @Autowired
    private NfeProcessamentoService nfeProcessamentoService;

    @GetMapping
    public List<RecebimentoMercadoriasResponseDTO> listarTodos(){
        return recebimentoMercadoriasService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecebimentoMercadoriasResponseDTO> buscarPorId(@PathVariable Integer id) {
        Optional<RecebimentoMercadorias> recebimento = recebimentoMercadoriasService.buscarPorId(id);
        return recebimento.map(value -> ResponseEntity.ok(new RecebimentoMercadoriasResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RecebimentoMercadoriasResponseDTO> criarRecebimento(@RequestBody RecebimentoMercadoriasRequestDTO dto) {
        RecebimentoMercadorias novoRecebimento = recebimentoMercadoriasService.criarRecebimento(dto);
        return ResponseEntity.ok(new RecebimentoMercadoriasResponseDTO(novoRecebimento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecebimentoMercadoriasResponseDTO> atualizarRecebimento(@PathVariable Integer id, @RequestBody RecebimentoMercadoriasRequestDTO dto) {
        Optional<RecebimentoMercadorias> recebimentoAtualizado = recebimentoMercadoriasService.atualizarRecebimento(id, dto);
        return recebimentoAtualizado.map(value -> ResponseEntity.ok(new RecebimentoMercadoriasResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRecebimento(@PathVariable Integer id) {
        if (recebimentoMercadoriasService.deletarRecebimento(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/busca")
    public Page<RecebimentoMercadoriasBuscaResponseDTO> buscarRecebimentos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dataRecebimento").descending());
        return recebimentoMercadoriasService.buscarRecebimentos(pageRequest);
    }

    @GetMapping("/busca/{id}")
    public ResponseEntity<RecebimentoMercadoriasBuscaResponseDTO> simplesBuscaPorId(@PathVariable Integer id) {
        return recebimentoMercadoriasService.simplesBuscaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/busca-por-razao-social")
    public ResponseEntity<Page<RecebimentoMercadoriasBuscaResponseDTO>> buscarRecebimentosPorRazaoSocial(
            @RequestParam String razaoSocial,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RecebimentoMercadoriasBuscaResponseDTO> recebimentos = recebimentoMercadoriasService.buscarRecebimentosPorRazaoSocial(razaoSocial, pageable);
        return ResponseEntity.ok(recebimentos);
    }

    @Operation(
            summary = "Fazer upload e processar XML de NFe",
            description = "Recebe um arquivo XML de Nota Fiscal Eletrônica (NFe), extrai os dados da nota e dos itens, " +
                    "realiza vinculação automática de produtos através da tabela de códigos de produto no fornecedor, " +
                    "e retorna os dados processados com status de vinculação de cada item. " +
                    "Itens vinculados automaticamente aparecem com o produto correspondente. " +
                    "Itens não vinculados retornam com sugestões de produtos baseadas em EAN, NCM e descrição.",
            tags = {TAG_RECEBIMENTO}
    )
    @PostMapping("/upload-xml")
    public ResponseEntity<?> uploadXmlNfe(
            @Parameter(
                    description = "Arquivo XML da NFe (Nota Fiscal Eletrônica). Tamanho máximo: 10MB",
                    required = true,
                    schema = @Schema(type = "string", format = "binary")
            )
            @RequestParam("arquivo") MultipartFile arquivo) {
        try {
            NfeDadosExtraidosDTO dadosExtraidos = nfeXmlParserService.parseXmlNfe(arquivo);

            NfeProcessadaResponseDTO nfeProcessada = nfeProcessamentoService.processarNfe(dadosExtraidos);

            return ResponseEntity.ok(nfeProcessada);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao processar XML da NFe: " + e.getMessage()));
        }
    }
    private record ErrorResponse(String mensagem) {}
}
