package com.example.lite_erp.services;

import com.example.lite_erp.entities.fluxo_caixa.*;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixa;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaRepository;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaRequestDTO;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaResponseDTO;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixaRepository;
import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.entities.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ContaCaixaService {

    @Autowired
    private ContaCaixaRepository contaCaixaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MovimentacaoCaixaRepository movimentacaoCaixaRepository;

    @Transactional(readOnly = true)
    public List<ContaCaixaResponseDTO> listarTodas() {
        return contaCaixaRepository.findByAtivoTrueOrderByDescricao()
                .stream()
                .map(ContaCaixaResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ContaCaixaResponseDTO> listarPorUsuario(Long usuarioId) {
        return contaCaixaRepository.findByUsuarioResponsavelIdAndAtivoTrueOrderByDescricao(usuarioId)
                .stream()
                .map(ContaCaixaResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ContaCaixaResponseDTO> listarContasAcessiveis(Long usuarioId) {
        return contaCaixaRepository.findContasAcessiveisPorUsuario(usuarioId)
                .stream()
                .map(ContaCaixaResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ContaCaixaResponseDTO> buscarPorId(Long id) {
        return contaCaixaRepository.findById(id)
                .map(ContaCaixaResponseDTO::new);
    }

    @Transactional
    public ContaCaixaResponseDTO salvar(ContaCaixaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioResponsavelId().toString())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        ContaCaixa conta = new ContaCaixa();
        conta.setDescricao(dto.descricao());
        conta.setTipo(dto.tipo());
        conta.setBanco(dto.banco());
        conta.setAgencia(dto.agencia());
        conta.setConta(dto.conta());
        conta.setSaldoAtual(dto.saldoInicial() != null ? dto.saldoInicial() : BigDecimal.ZERO);
        conta.setUsuarioResponsavel(usuario);
        conta.setAtivo(true);

        ContaCaixa contaSalva = contaCaixaRepository.save(conta);
        return new ContaCaixaResponseDTO(contaSalva);
    }

    @Transactional
    public Optional<ContaCaixaResponseDTO> atualizar(Long id, ContaCaixaRequestDTO dto) {
        return contaCaixaRepository.findById(id)
                .map(conta -> {
                    Usuario usuario = usuarioRepository.findById(dto.usuarioResponsavelId().toString())
                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                    conta.setDescricao(dto.descricao());
                    conta.setTipo(dto.tipo());
                    conta.setBanco(dto.banco());
                    conta.setAgencia(dto.agencia());
                    conta.setConta(dto.conta());
                    conta.setUsuarioResponsavel(usuario);
                    
                    ContaCaixa contaAtualizada = contaCaixaRepository.save(conta);
                    return new ContaCaixaResponseDTO(contaAtualizada);
                });
    }

    @Transactional
    public boolean inativar(Long id) {
        return contaCaixaRepository.findById(id)
                .map(conta -> {
                    conta.setAtivo(false);
                    contaCaixaRepository.save(conta);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean ativar(Long id) {
        return contaCaixaRepository.findById(id)
                .map(conta -> {
                    conta.setAtivo(true);
                    contaCaixaRepository.save(conta);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public void atualizarSaldo(Long contaCaixaId) {
        BigDecimal saldoCalculado = movimentacaoCaixaRepository.calcularSaldoContaCaixa(contaCaixaId);
        final BigDecimal saldoFinal = (saldoCalculado != null) ? saldoCalculado : BigDecimal.ZERO;

        contaCaixaRepository.findById(contaCaixaId)
                .ifPresent(conta -> {
                    conta.setSaldoAtual(saldoFinal);
                    contaCaixaRepository.save(conta);
                });
    }

    @Transactional(readOnly = true)
    public List<SaldoContaResponseDTO> obterSaldosContas(Long usuarioId) {
        List<ContaCaixa> contas = contaCaixaRepository.findContasAcessiveisPorUsuario(usuarioId);
        
        return contas.stream()
                .map(conta -> {
                    BigDecimal saldoAtual = conta.getSaldoAtual();
                    // Aqui poderia calcular entradas e saídas separadamente se necessário
                    return new SaldoContaResponseDTO(
                            conta.getId(),
                            conta.getDescricao(),
                            saldoAtual,
                            BigDecimal.ZERO, // totalEntradas - implementar se necessário
                            BigDecimal.ZERO  // totalSaidas - implementar se necessário
                    );
                })
                .toList();
    }
}
