package com.example.lite_erp.services;

import com.example.lite_erp.entities.fornecedores.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedoresService {

    @Autowired
    private FornecedoresRepository fornecedoresRepository;

    public List<Fornecedores> listarTodos() {
        return fornecedoresRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Optional<Fornecedores> buscarPorId(Integer id) {
        return fornecedoresRepository.findById(id);
    }

    public Optional<FornecedoresBuscaResponseDTO> simplesBuscaPorId(Integer id) {
        return fornecedoresRepository.findById(id)
                .map(fornecedor -> new FornecedoresBuscaResponseDTO(
                        fornecedor.getId().intValue(),
                        fornecedor.getRazaoSocial(),
                        fornecedor.getNomeFantasia()
                ));
    }

    public Fornecedores criarFornecedor(FornecedoresRequestDTO dto) {
        Fornecedores fornecedor = new Fornecedores(
                null,
                dto.tipoPessoa(),
                dto.cpf(),
                dto.cnpj(),
                dto.nomeFantasia(),
                dto.razaoSocial(),
                dto.cep(),
                dto.endereco(),
                dto.complemento(),
                dto.numero(),
                dto.bairro(),
                dto.cidade(),
                dto.celular(),
                dto.telefone(),
                dto.email(),
                dto.estadoInscricaoEstadual(),
                dto.inscricaoEstadual(),
                dto.observacao(),
                dto.status(),
                dto.dataCadastro()
        );
        return fornecedoresRepository.save(fornecedor);
    }

    public Optional<Fornecedores> atualizarFornecedor(Integer id, FornecedoresRequestDTO dto) {
        return fornecedoresRepository.findById(id).map(fornecedor -> {
            fornecedor.setTipoPessoa(dto.tipoPessoa());
            fornecedor.setCpf(dto.cpf());
            fornecedor.setCnpj(dto.cnpj());
            fornecedor.setNomeFantasia(dto.nomeFantasia());
            fornecedor.setRazaoSocial(dto.razaoSocial());
            fornecedor.setCep(dto.cep());
            fornecedor.setEndereco(dto.endereco());
            fornecedor.setComplemento(dto.complemento());
            fornecedor.setNumero(dto.numero());
            fornecedor.setBairro(dto.bairro());
            fornecedor.setCidade(dto.cidade());
            fornecedor.setCelular(dto.celular());
            fornecedor.setTelefone(dto.telefone());
            fornecedor.setEmail(dto.email());
            fornecedor.setEstadoInscricaoEstadual(dto.estadoInscricaoEstadual());
            fornecedor.setInscricaoEstadual(dto.inscricaoEstadual());
            fornecedor.setObservacao(dto.observacao());
            fornecedor.setStatus(dto.status());
            fornecedor.setDataCadastro(dto.dataCadastro());
            return fornecedoresRepository.save(fornecedor);
        });
    }

    public boolean deletarFornecedor(Integer id) {
        if (fornecedoresRepository.existsById(id)) {
            fornecedoresRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Listar todos os fornecedores de forma paginada
    public Page<Fornecedores> listarTodosPaginado(Pageable pageable) {
        return fornecedoresRepository.findAll(pageable);
    }

    // Busca paginada por nome
    public Page<Fornecedores> findByNomeContainingIgnoreCase(String nome, Pageable pageable) {
        return fornecedoresRepository.findByRazaoSocialContainingIgnoreCase(nome, pageable);
    }

    public Page<FornecedoresBuscaResponseDTO> buscarFornecedores(Pageable pageable) {
        return fornecedoresRepository.findFornecedoresForBusca(pageable);
    }

    public Page<FornecedoresBuscaResponseDTO> buscarFornecedoresPorRazaoSocial(String razaoSocial, Pageable pageable) {
        return fornecedoresRepository.findFornecedoresForBuscaByRazaoSocial(razaoSocial + "%", pageable);
    }
}
