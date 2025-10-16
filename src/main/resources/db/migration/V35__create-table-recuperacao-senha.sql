-- V33__create-table-recuperacao-senha.sql
-- Migration para criar tabela de recuperação de senha

CREATE TABLE recuperacao_senha (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    codigo VARCHAR(6) NOT NULL,
    token_temporario VARCHAR(255),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_expiracao TIMESTAMP NOT NULL,
    tentativas_validacao INTEGER NOT NULL DEFAULT 0,
    validado BOOLEAN NOT NULL DEFAULT FALSE,
    usado BOOLEAN NOT NULL DEFAULT FALSE,
    ip_solicitacao VARCHAR(45),
    
    CONSTRAINT fk_recuperacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Índices para melhorar performance
CREATE INDEX idx_recuperacao_usuario_id ON recuperacao_senha(usuario_id);
CREATE INDEX idx_recuperacao_codigo ON recuperacao_senha(codigo);
CREATE INDEX idx_recuperacao_token ON recuperacao_senha(token_temporario);
CREATE INDEX idx_recuperacao_data_expiracao ON recuperacao_senha(data_expiracao);

-- Comentários
COMMENT ON TABLE recuperacao_senha IS 'Tabela para armazenar códigos de recuperação de senha';
COMMENT ON COLUMN recuperacao_senha.codigo IS 'Código de 6 dígitos enviado por email';
COMMENT ON COLUMN recuperacao_senha.token_temporario IS 'Token JWT temporário gerado após validação do código';
COMMENT ON COLUMN recuperacao_senha.tentativas_validacao IS 'Contador de tentativas de validação (máx 3)';
COMMENT ON COLUMN recuperacao_senha.validado IS 'Indica se o código foi validado com sucesso';
COMMENT ON COLUMN recuperacao_senha.usado IS 'Indica se o token foi usado para redefinir a senha';

