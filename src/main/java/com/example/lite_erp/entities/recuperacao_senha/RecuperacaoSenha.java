package com.example.lite_erp.entities.recuperacao_senha;

import com.example.lite_erp.entities.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidade para armazenar códigos de recuperação de senha
 */
@Entity
@Table(name = "recuperacao_senha")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecuperacaoSenha {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(name = "codigo", nullable = false, length = 6)
    private String codigo;
    
    @Column(name = "token_temporario", length = 255)
    private String tokenTemporario;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_expiracao", nullable = false)
    private LocalDateTime dataExpiracao;
    
    @Column(name = "tentativas_validacao", nullable = false)
    private Integer tentativasValidacao = 0;
    
    @Column(name = "validado", nullable = false)
    private Boolean validado = false;
    
    @Column(name = "usado", nullable = false)
    private Boolean usado = false;
    
    @Column(name = "ip_solicitacao", length = 45)
    private String ipSolicitacao;
    
    /**
     * Verifica se o código está expirado
     */
    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(dataExpiracao);
    }
    
    /**
     * Verifica se atingiu o limite de tentativas
     */
    public boolean atingiuLimiteTentativas() {
        return tentativasValidacao >= 3;
    }
    
    /**
     * Incrementa o contador de tentativas
     */
    public void incrementarTentativas() {
        this.tentativasValidacao++;
    }
    
    /**
     * Verifica se o código pode ser validado
     */
    public boolean podeSerValidado() {
        return !isExpirado() && !atingiuLimiteTentativas() && !validado && !usado;
    }
    
    /**
     * Verifica se o token pode ser usado para redefinir senha
     */
    public boolean podeRedefinirSenha() {
        return validado && !usado && !isExpirado() && tokenTemporario != null;
    }
}

