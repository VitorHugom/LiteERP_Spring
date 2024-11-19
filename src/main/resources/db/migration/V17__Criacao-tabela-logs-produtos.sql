CREATE TABLE log_alteracoes_produto (
    id BIGSERIAL PRIMARY KEY,          -- Identificador único do log
    id_produto INTEGER NOT NULL,       -- ID do produto que sofreu a alteração
    campo_alterado VARCHAR(255) NOT NULL,   -- Nome do campo alterado
    valor_anterior TEXT,               -- Valor anterior do campo (antes da alteração)
    valor_atual TEXT,                  -- Valor atual do campo (após a alteração)
    data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Data e hora da alteração
    usuario_responsavel VARCHAR(100)  -- Usuário que realizou a alteração (se aplicável)
);

-- Índice para melhorar a performance nas consultas por produto
CREATE INDEX idx_log_produto_id ON log_alteracoes_produto (id_produto);
