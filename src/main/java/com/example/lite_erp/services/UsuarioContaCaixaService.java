package com.example.lite_erp.services;

import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixa;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaRepository;
import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.infra.security.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service para gerenciar contas de caixa relacionadas ao usuário logado
 */
@Service
public class UsuarioContaCaixaService {

    @Autowired
    private ContaCaixaRepository contaCaixaRepository;

    @Autowired
    private UsuarioFluxoCaixaService usuarioFluxoCaixaService;

    /**
     * Obtém a conta de caixa padrão do usuário logado
     * Se não existir, cria uma automaticamente
     * 
     * @return ID da conta de caixa padrão do usuário logado
     */
    @Transactional
    public Long obterContaCaixaPadraoUsuarioLogado() {
        Usuario usuarioLogado = AuthenticationUtils.getUsuarioLogado();
        return obterContaCaixaPadraoUsuario(usuarioLogado);
    }

    /**
     * Obtém a conta de caixa padrão de um usuário específico
     * Se não existir, cria uma automaticamente
     * 
     * @param usuario Usuario para obter a conta
     * @return ID da conta de caixa padrão do usuário
     */
    @Transactional
    public Long obterContaCaixaPadraoUsuario(Usuario usuario) {
        // Garantir que o usuário tenha uma conta de caixa
        usuarioFluxoCaixaService.garantirContaCaixaPadrao(usuario);
        
        // Buscar a primeira conta ativa do usuário
        List<ContaCaixa> contasUsuario = contaCaixaRepository
                .findByUsuarioResponsavelIdAndAtivoTrueOrderByDescricao(usuario.getId());
        
        if (contasUsuario.isEmpty()) {
            throw new RuntimeException("Erro ao criar conta de caixa para o usuário: " + usuario.getNomeUsuario());
        }
        
        return contasUsuario.get(0).getId();
    }

    /**
     * Obtém a conta de caixa padrão de um usuário pelo ID
     * Se não existir, cria uma automaticamente
     * 
     * @param usuarioId ID do usuário
     * @return ID da conta de caixa padrão do usuário
     */
    @Transactional
    public Long obterContaCaixaPadraoUsuarioPorId(Long usuarioId) {
        // Buscar a primeira conta ativa do usuário
        List<ContaCaixa> contasUsuario = contaCaixaRepository
                .findByUsuarioResponsavelIdAndAtivoTrueOrderByDescricao(usuarioId);
        
        if (!contasUsuario.isEmpty()) {
            return contasUsuario.get(0).getId();
        }
        
        // Se não encontrou, buscar conta de caixa padrão (primeira ativa do sistema)
        return contaCaixaRepository.findByAtivoTrueOrderByDescricao()
                .stream()
                .findFirst()
                .map(ContaCaixa::getId)
                .orElseThrow(() -> new RuntimeException("Nenhuma conta de caixa ativa encontrada no sistema"));
    }

    /**
     * Lista todas as contas de caixa do usuário logado
     * 
     * @return Lista de contas do usuário logado
     */
    @Transactional(readOnly = true)
    public List<ContaCaixa> listarContasUsuarioLogado() {
        Long usuarioId = AuthenticationUtils.getUsuarioLogadoId();
        return contaCaixaRepository.findByUsuarioResponsavelIdAndAtivoTrueOrderByDescricao(usuarioId);
    }

    /**
     * Verifica se o usuário logado possui contas de caixa
     * 
     * @return true se possui contas, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean usuarioLogadoPossuiContas() {
        Long usuarioId = AuthenticationUtils.getUsuarioLogadoId();
        return usuarioFluxoCaixaService.usuarioPossuiContaCaixa(usuarioId);
    }

    /**
     * Obtém o saldo total de todas as contas do usuário logado
     * 
     * @return Saldo total das contas do usuário
     */
    @Transactional(readOnly = true)
    public BigDecimal obterSaldoTotalUsuarioLogado() {
        List<ContaCaixa> contas = listarContasUsuarioLogado();
        return contas.stream()
                .map(ContaCaixa::getSaldoAtual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
