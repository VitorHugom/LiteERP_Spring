package com.example.lite_erp.services;

import com.example.lite_erp.entities.clientes.Clientes;
import com.example.lite_erp.entities.clientes.ClientesRepository;
import com.example.lite_erp.entities.clientes.ClientesRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientesService {

    @Autowired
    private ClientesRepository clientesRepository;

    public List<Clientes> listarTodos() {
        return clientesRepository.findAll();
    }

    public Optional<Clientes> buscarPorId(Long id) {
        return clientesRepository.findById(id);
    }

    public Clientes criarCliente(ClientesRequestDTO dto) {
        Clientes cliente = new Clientes(
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
                dto.rg(),
                dto.dataNascimento(),
                dto.email(),
                dto.estadoInscricaoEstadual(),
                dto.inscricaoEstadual(),
                dto.vendedor(),
                dto.observacao(),
                dto.status(),
                dto.dataCadastro(),
                dto.limiteCredito()
        );
        return clientesRepository.save(cliente);
    }

    public Optional<Clientes> atualizarCliente(Long id, ClientesRequestDTO dto) {
        return clientesRepository.findById(id).map(cliente -> {
            cliente.setTipoPessoa(dto.tipoPessoa());
            cliente.setCpf(dto.cpf());
            cliente.setCnpj(dto.cnpj());
            cliente.setNomeFantasia(dto.nomeFantasia());
            cliente.setRazaoSocial(dto.razaoSocial());
            cliente.setCep(dto.cep());
            cliente.setEndereco(dto.endereco());
            cliente.setComplemento(dto.complemento());
            cliente.setNumero(dto.numero());
            cliente.setBairro(dto.bairro());
            cliente.setCidade(dto.cidade());
            cliente.setCelular(dto.celular());
            cliente.setTelefone(dto.telefone());
            cliente.setRg(dto.rg());
            cliente.setDataNascimento(dto.dataNascimento());
            cliente.setEmail(dto.email());
            cliente.setEstadoInscricaoEstadual(dto.estadoInscricaoEstadual());
            cliente.setInscricaoEstadual(dto.inscricaoEstadual());
            cliente.setVendedor(dto.vendedor());
            cliente.setObservacao(dto.observacao());
            cliente.setStatus(dto.status());
            cliente.setDataCadastro(dto.dataCadastro());
            cliente.setLimiteCredito(dto.limiteCredito());
            return clientesRepository.save(cliente);
        });
    }

    public boolean deletarCliente(Long id) {
        if (clientesRepository.existsById(id)) {
            clientesRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
