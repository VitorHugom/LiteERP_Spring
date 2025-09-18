package com.example.lite_erp.services;

import com.example.lite_erp.entities.fluxo_caixa.centro_custo.CentroCusto;
import com.example.lite_erp.entities.fluxo_caixa.centro_custo.CentroCustoRepository;
import com.example.lite_erp.entities.fluxo_caixa.centro_custo.CentroCustoRequestDTO;
import com.example.lite_erp.entities.fluxo_caixa.centro_custo.CentroCustoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CentroCustoService {

    @Autowired
    private CentroCustoRepository centroCustoRepository;

    @Transactional(readOnly = true)
    public List<CentroCustoResponseDTO> listarTodos() {
        return centroCustoRepository.findByAtivoTrueOrderByDescricao()
                .stream()
                .map(CentroCustoResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<CentroCustoResponseDTO> buscarPorId(Long id) {
        return centroCustoRepository.findById(id)
                .map(CentroCustoResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<CentroCustoResponseDTO> buscarPorCodigo(String codigo) {
        return centroCustoRepository.findByCodigo(codigo)
                .map(CentroCustoResponseDTO::new);
    }

    @Transactional
    public CentroCustoResponseDTO salvar(CentroCustoRequestDTO dto) {
        if (centroCustoRepository.existsByCodigo(dto.codigo())) {
            throw new RuntimeException("Já existe um centro de custo com o código: " + dto.codigo());
        }

        CentroCusto centroCusto = new CentroCusto();
        centroCusto.setDescricao(dto.descricao());
        centroCusto.setCodigo(dto.codigo().toUpperCase());
        centroCusto.setAtivo(true);

        CentroCusto centroCustoSalvo = centroCustoRepository.save(centroCusto);
        return new CentroCustoResponseDTO(centroCustoSalvo);
    }

    @Transactional
    public Optional<CentroCustoResponseDTO> atualizar(Long id, CentroCustoRequestDTO dto) {
        return centroCustoRepository.findById(id)
                .map(centroCusto -> {
                    if (centroCustoRepository.existsByCodigoAndIdNot(dto.codigo(), id)) {
                        throw new RuntimeException("Já existe um centro de custo com o código: " + dto.codigo());
                    }

                    centroCusto.setDescricao(dto.descricao());
                    centroCusto.setCodigo(dto.codigo().toUpperCase());
                    
                    CentroCusto centroCustoAtualizado = centroCustoRepository.save(centroCusto);
                    return new CentroCustoResponseDTO(centroCustoAtualizado);
                });
    }

    @Transactional
    public boolean inativar(Long id) {
        return centroCustoRepository.findById(id)
                .map(centroCusto -> {
                    centroCusto.setAtivo(false);
                    centroCustoRepository.save(centroCusto);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean ativar(Long id) {
        return centroCustoRepository.findById(id)
                .map(centroCusto -> {
                    centroCusto.setAtivo(true);
                    centroCustoRepository.save(centroCusto);
                    return true;
                })
                .orElse(false);
    }
}
