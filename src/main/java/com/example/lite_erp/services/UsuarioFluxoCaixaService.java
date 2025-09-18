package com.example.lite_erp.services;

import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixa;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaRepository;
import com.example.lite_erp.entities.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UsuarioFluxoCaixaService {

    @Autowired
    private ContaCaixaRepository contaCaixaRepository;

    /**
     * Cria uma conta de caixa padrão para um usuário quando ele é autorizado
     */
    @Transactional
    public void criarContaCaixaPadrao(Usuario usuario) {
        // Verificar se o usuário já possui uma conta de caixa
        boolean jaTemConta = contaCaixaRepository.findByUsuarioResponsavelIdAndAtivoTrueOrderByDescricao(usuario.getId())
                .size() > 0;

        if (!jaTemConta) {
            ContaCaixa contaPadrao = new ContaCaixa();
            contaPadrao.setDescricao("Caixa - " + usuario.getNomeUsuario());
            contaPadrao.setTipo(ContaCaixa.TipoConta.CAIXA_FISICO);
            contaPadrao.setSaldoAtual(BigDecimal.ZERO);
            contaPadrao.setUsuarioResponsavel(usuario);
            contaPadrao.setAtivo(true);

            contaCaixaRepository.save(contaPadrao);
        }
    }

    /**
     * Verifica se um usuário possui conta de caixa
     */
    @Transactional(readOnly = true)
    public boolean usuarioPossuiContaCaixa(Long usuarioId) {
        return contaCaixaRepository.findByUsuarioResponsavelIdAndAtivoTrueOrderByDescricao(usuarioId)
                .size() > 0;
    }

    /**
     * Cria conta padrão para usuário se não existir
     */
    @Transactional
    public void garantirContaCaixaPadrao(Usuario usuario) {
        if (!usuarioPossuiContaCaixa(usuario.getId())) {
            criarContaCaixaPadrao(usuario);
        }
    }
}
