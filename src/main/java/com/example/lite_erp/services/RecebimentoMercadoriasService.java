package com.example.lite_erp.services;

import com.example.lite_erp.entities.fornecedores.FornecedoresRepository;
import com.example.lite_erp.entities.itens_recebimento_mercadorias.ItensRecebimentoMercadorias;
import com.example.lite_erp.entities.produtos.ProdutosRepository;
import com.example.lite_erp.entities.recebimento_mercadorias.*;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecebimentoMercadoriasService {

    @Autowired
    private RecebimentoMercadoriasRepository recebimentoMercadoriasRepository;

    @Autowired
    private FornecedoresRepository fornecedoresRepository;

    @Autowired
    private TiposCobrancaRepository tiposCobrancaRepository;

    @Autowired
    private ProdutosRepository produtosRepository;

    @Autowired
    private MovimentoEstoqueService movimentoEstoqueService;

    public List<RecebimentoMercadoriasResponseDTO> listarTodos() {
        return recebimentoMercadoriasRepository.findAll()
                .stream()
                .map(RecebimentoMercadoriasResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<RecebimentoMercadorias> buscarPorId(Integer id) {
        return recebimentoMercadoriasRepository.findById(id);
    }

    public Optional<RecebimentoMercadoriasBuscaResponseDTO> simplesBuscaPorId(Integer id) {
        return recebimentoMercadoriasRepository.findById(id)
                .map(RecebimentoMercadoriasBuscaResponseDTO::new);
    }

    @Transactional
    public RecebimentoMercadorias criarRecebimento(RecebimentoMercadoriasRequestDTO dto) {
        RecebimentoMercadorias recebimento = new RecebimentoMercadorias();
        recebimento.setFornecedor(fornecedoresRepository.findById(dto.idFornecedor()).orElseThrow());
        recebimento.setTipoCobranca(tiposCobrancaRepository.findById(dto.idTipoCobranca()).orElseThrow());
        recebimento.setDataRecebimento(dto.dataRecebimento());

        // Criar e associar os itens ao recebimento
        dto.itens().forEach(itemDto -> {
            ItensRecebimentoMercadorias item = new ItensRecebimentoMercadorias();
            item.setProduto(produtosRepository.findById(itemDto.idProduto()).orElseThrow());
            item.setQuantidade(itemDto.quantidade());
            item.setValorUnitario(itemDto.valorUnitario());
            item.setRecebimento(recebimento);
            recebimento.getItens().add(item);
        });

        // Salvar recebimento e seus itens
        RecebimentoMercadorias recebimentoSalvo = recebimentoMercadoriasRepository.save(recebimento);

        // Após salvar, registrar movimentos de estoque
        recebimentoSalvo.getItens().forEach(movimentoEstoqueService::registrarMovimentoPorRecebimento);

        return recebimentoSalvo;
    }

    @Transactional
    public Optional<RecebimentoMercadorias> atualizarRecebimento(Integer id, RecebimentoMercadoriasRequestDTO dto) {
        return recebimentoMercadoriasRepository.findById(id).map(recebimento -> {
            // Atualizar dados do recebimento
            recebimento.setFornecedor(fornecedoresRepository.findById(dto.idFornecedor()).orElseThrow());
            recebimento.setTipoCobranca(tiposCobrancaRepository.findById(dto.idTipoCobranca()).orElseThrow());
            recebimento.setDataRecebimento(dto.dataRecebimento());

            // Reverter movimentos antigos associados aos itens removidos
            recebimento.getItens().forEach(item ->
                    movimentoEstoqueService.reverterMovimentoPorRecebimento(item)
            );
            recebimento.getItens().clear();

            // Recria a lista de itens com os dados do DTO
            dto.itens().forEach(itemDto -> {
                ItensRecebimentoMercadorias item = new ItensRecebimentoMercadorias();
                item.setProduto(produtosRepository.findById(itemDto.idProduto()).orElseThrow());
                item.setQuantidade(itemDto.quantidade());
                item.setValorUnitario(itemDto.valorUnitario());
                item.setRecebimento(recebimento);
                recebimento.getItens().add(item);
            });

            // Salvar recebimento e seus itens
            RecebimentoMercadorias recebimentoSalvo = recebimentoMercadoriasRepository.save(recebimento);

            // Registrar os novos movimentos de estoque
            recebimentoSalvo.getItens().forEach(movimentoEstoqueService::registrarMovimentoPorRecebimento);

            return recebimentoSalvo;
        });
    }

    @Transactional
    public boolean deletarRecebimento(Integer id) {
        return recebimentoMercadoriasRepository.findById(id).map(recebimento -> {
            // Reverter movimentos de estoque para os itens associados
            recebimento.getItens().forEach(item ->
                    movimentoEstoqueService.reverterMovimentoPorRecebimento(item)
            );

            recebimentoMercadoriasRepository.delete(recebimento);
            return true;
        }).orElse(false);
    }

    public Page<RecebimentoMercadoriasBuscaResponseDTO> buscarRecebimentos(Pageable pageable) {
        return recebimentoMercadoriasRepository.findAllOrderedByDataRecebimento(pageable);
    }

    public Page<RecebimentoMercadoriasBuscaResponseDTO> buscarRecebimentosPorRazaoSocial(String razaoSocial, Pageable pageable) {
        return recebimentoMercadoriasRepository.findRecebimentosByFornecedorRazaoSocial(razaoSocial + "%", pageable);
    }
}
