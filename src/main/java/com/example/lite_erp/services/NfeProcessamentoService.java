package com.example.lite_erp.services;

import com.example.lite_erp.entities.fornecedores.Fornecedores;
import com.example.lite_erp.entities.fornecedores.FornecedoresRepository;
import com.example.lite_erp.entities.nfe.*;
import com.example.lite_erp.entities.produto_fornecedor_codigo.ProdutoFornecedorCodigoRepository;
import com.example.lite_erp.entities.produtos.Produtos;
import com.example.lite_erp.entities.produtos.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NfeProcessamentoService {

    @Autowired
    private FornecedoresRepository fornecedoresRepository;

    @Autowired
    private ProdutoFornecedorCodigoRepository produtoFornecedorCodigoRepository;

    @Autowired
    private ProdutosRepository produtosRepository;

    public NfeProcessadaResponseDTO processarNfe(NfeDadosExtraidosDTO dadosExtraidos) {
        DadosFornecedorNfeDTO fornecedor = buscarFornecedor(dadosExtraidos);

        List<ItemNfeProcessadoDTO> itensProcessados = new ArrayList<>();
        int itensVinculados = 0;

        for (ItemNfeDTO item : dadosExtraidos.itens()) {
            ItemNfeProcessadoDTO itemProcessado = processarItem(item, fornecedor.idFornecedor());
            itensProcessados.add(itemProcessado);
            
            if (itemProcessado.produtoVinculado()) {
                itensVinculados++;
            }
        }

        DadosNfeDTO dadosNfe = new DadosNfeDTO(
                dadosExtraidos.chaveAcesso(),
                dadosExtraidos.numeroNota(),
                dadosExtraidos.serie(),
                dadosExtraidos.dataEmissao(),
                dadosExtraidos.valorTotal()
        );

        int totalItens = dadosExtraidos.itens().size();
        int itensNaoVinculados = totalItens - itensVinculados;
        String mensagem = gerarMensagem(totalItens, itensVinculados, itensNaoVinculados, fornecedor.encontrado());

        return new NfeProcessadaResponseDTO(
                dadosNfe,
                fornecedor,
                itensProcessados,
                dadosExtraidos.valorTotal(),
                mensagem,
                totalItens,
                itensVinculados,
                itensNaoVinculados
        );
    }

    private DadosFornecedorNfeDTO buscarFornecedor(NfeDadosExtraidosDTO dadosExtraidos) {
        String cnpj = dadosExtraidos.cnpjFornecedor();
        
        String cnpjLimpo = cnpj != null ? cnpj.replaceAll("[^0-9]", "") : null;

        Optional<Fornecedores> fornecedorOpt = Optional.empty();
        
        if (cnpjLimpo != null && !cnpjLimpo.isEmpty()) {
            fornecedorOpt = fornecedoresRepository.findByCnpj(cnpjLimpo);
            
            if (fornecedorOpt.isEmpty()) {
                fornecedorOpt = fornecedoresRepository.findByCnpj(cnpj);
            }
        }

        if (fornecedorOpt.isPresent()) {
            Fornecedores fornecedor = fornecedorOpt.get();
            return new DadosFornecedorNfeDTO(
                    cnpj,
                    dadosExtraidos.razaoSocialFornecedor(),
                    dadosExtraidos.nomeFantasiaFornecedor(),
                    fornecedor.getId(),
                    true
            );
        } else {
            return new DadosFornecedorNfeDTO(
                    cnpj,
                    dadosExtraidos.razaoSocialFornecedor(),
                    dadosExtraidos.nomeFantasiaFornecedor(),
                    null,
                    false
            );
        }
    }

    private ItemNfeProcessadoDTO processarItem(ItemNfeDTO item, Integer idFornecedor) {
        Produtos produtoVinculado = null;

        if (idFornecedor != null && item.codigoProduto() != null) {
            var vinculoOpt = produtoFornecedorCodigoRepository
                    .findByCodigoFornecedorAndFornecedorId(item.codigoProduto(), idFornecedor);
            
            if (vinculoOpt.isPresent()) {
                produtoVinculado = vinculoOpt.get().getProduto();
            }
        }

        if (produtoVinculado == null && item.ean() != null && !item.ean().isEmpty()) {
            var produtoOpt = produtosRepository.findByCodEan(item.ean());
            if (produtoOpt.isPresent()) {
                produtoVinculado = produtoOpt.get();
            }
        }

        List<SugestaoProdutoDTO> sugestoes = new ArrayList<>();
        if (produtoVinculado == null) {
            sugestoes = gerarSugestoesProduto(item);
        }

        return new ItemNfeProcessadoDTO(
                item.numeroItem(),
                item.codigoProduto(),
                item.descricao(),
                item.ncm(),
                item.ean(),
                item.quantidade(),
                item.unidade(),
                item.valorUnitario(),
                item.valorTotal(),
                produtoVinculado != null,
                produtoVinculado != null ? produtoVinculado.getId() : null,
                produtoVinculado != null ? produtoVinculado.getDescricao() : null,
                sugestoes
        );
    }

    private List<SugestaoProdutoDTO> gerarSugestoesProduto(ItemNfeDTO item) {
        Map<Long, SugestaoProdutoDTO> sugestoesMap = new HashMap<>();

        if (item.ean() != null && !item.ean().isEmpty()) {
            var produtoOpt = produtosRepository.findByCodEan(item.ean());
            if (produtoOpt.isPresent()) {
                Produtos p = produtoOpt.get();
                sugestoesMap.put(p.getId(), criarSugestao(p, 100));
            }
        }

        if (item.ncm() != null && !item.ncm().isEmpty()) {
            List<Produtos> produtosPorNcm = produtosRepository.findByCodNcm(item.ncm());
            for (Produtos p : produtosPorNcm) {
                if (!sugestoesMap.containsKey(p.getId())) {
                    sugestoesMap.put(p.getId(), criarSugestao(p, 70));
                }
            }
        }

        if (item.descricao() != null && !item.descricao().isEmpty()) {
            String[] palavras = item.descricao().split("\\s+");
            Set<String> palavrasChave = Arrays.stream(palavras)
                    .filter(p -> p.length() > 3)
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());

            for (String palavra : palavrasChave) {
                List<Produtos> produtosPorDescricao = produtosRepository.findByDescricaoContaining(palavra);
                for (Produtos p : produtosPorDescricao) {
                    if (!sugestoesMap.containsKey(p.getId())) {
                        sugestoesMap.put(p.getId(), criarSugestao(p, 50));
                    }
                }
                
                if (sugestoesMap.size() >= 20) {
                    break;
                }
            }
        }

        return sugestoesMap.values().stream()
                .sorted((s1, s2) -> Integer.compare(s2.score(), s1.score()))
                .limit(10)
                .collect(Collectors.toList());
    }

    private SugestaoProdutoDTO criarSugestao(Produtos produto, Integer score) {
        return new SugestaoProdutoDTO(
                produto.getId(),
                produto.getDescricao(),
                produto.getMarca() != null ? produto.getMarca() : null,
                produto.getCodEan(),
                produto.getCodNcm(),
                score
        );
    }

    private String gerarMensagem(int totalItens, int itensVinculados, int itensNaoVinculados, boolean fornecedorEncontrado) {
        StringBuilder msg = new StringBuilder();

        if (!fornecedorEncontrado) {
            msg.append("⚠️ Fornecedor não encontrado no sistema. ");
        }

        msg.append("NFe processada com sucesso. ");

        if (itensVinculados == totalItens) {
            msg.append("✅ Todos os ").append(totalItens).append(" itens foram vinculados automaticamente!");
        } else if (itensVinculados > 0) {
            msg.append(itensVinculados).append(" de ").append(totalItens)
               .append(" itens vinculados automaticamente. ")
               .append(itensNaoVinculados).append(" item(ns) precisa(m) de vinculação manual.");
        } else {
            msg.append("⚠️ Nenhum item foi vinculado automaticamente. ")
               .append("Todos os ").append(totalItens).append(" itens precisam de vinculação manual.");
        }

        return msg.toString();
    }
}

