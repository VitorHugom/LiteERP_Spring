package com.example.lite_erp.services;

import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacaoRepository;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacaoRequestDTO;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacaoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TipoMovimentacaoService {

    @Autowired
    private TipoMovimentacaoRepository tipoMovimentacaoRepository;

    @Transactional(readOnly = true)
    public List<TipoMovimentacaoResponseDTO> listarTodos() {
        return tipoMovimentacaoRepository.findByAtivoTrueOrderByDescricao()
                .stream()
                .map(TipoMovimentacaoResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TipoMovimentacaoResponseDTO> listarPorCategoria(TipoMovimentacao.CategoriaMovimentacao categoria) {
        return tipoMovimentacaoRepository.findByCategoriaAndAtivoTrueOrderByDescricao(categoria)
                .stream()
                .map(TipoMovimentacaoResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<TipoMovimentacaoResponseDTO> buscarPorId(Long id) {
        return tipoMovimentacaoRepository.findById(id)
                .map(TipoMovimentacaoResponseDTO::new);
    }

    @Transactional
    public TipoMovimentacaoResponseDTO salvar(TipoMovimentacaoRequestDTO dto) {
        TipoMovimentacao tipo = new TipoMovimentacao();
        tipo.setDescricao(dto.descricao());
        tipo.setCategoria(dto.categoria());
        tipo.setCorHex(dto.corHex());
        tipo.setAtivo(true);

        TipoMovimentacao tipoSalvo = tipoMovimentacaoRepository.save(tipo);
        return new TipoMovimentacaoResponseDTO(tipoSalvo);
    }

    @Transactional
    public Optional<TipoMovimentacaoResponseDTO> atualizar(Long id, TipoMovimentacaoRequestDTO dto) {
        return tipoMovimentacaoRepository.findById(id)
                .map(tipo -> {
                    tipo.setDescricao(dto.descricao());
                    tipo.setCategoria(dto.categoria());
                    tipo.setCorHex(dto.corHex());
                    
                    TipoMovimentacao tipoAtualizado = tipoMovimentacaoRepository.save(tipo);
                    return new TipoMovimentacaoResponseDTO(tipoAtualizado);
                });
    }

    @Transactional
    public boolean inativar(Long id) {
        return tipoMovimentacaoRepository.findById(id)
                .map(tipo -> {
                    tipo.setAtivo(false);
                    tipoMovimentacaoRepository.save(tipo);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean ativar(Long id) {
        return tipoMovimentacaoRepository.findById(id)
                .map(tipo -> {
                    tipo.setAtivo(true);
                    tipoMovimentacaoRepository.save(tipo);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<TipoMovimentacaoResponseDTO> filtrar(TipoMovimentacao.CategoriaMovimentacao categoria, Boolean ativo) {
        return tipoMovimentacaoRepository.findByFiltros(categoria, ativo)
                .stream()
                .map(TipoMovimentacaoResponseDTO::new)
                .toList();
    }
}
