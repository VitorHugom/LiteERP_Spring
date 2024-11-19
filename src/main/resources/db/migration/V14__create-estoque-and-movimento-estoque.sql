-- Criação da tabela de movimentação de estoque
CREATE TABLE movimento_estoque (
    id BIGSERIAL PRIMARY KEY,
    id_itens_pedido BIGINT, -- Relacionamento com itens do pedido
    id_itens_recebimento_mercadoria BIGINT, -- Relacionamento com itens de recebimento de mercadorias
    id_produto BIGINT NOT NULL,
    qtd NUMERIC(10, 4) NOT NULL, -- Quantidade movimentada (positiva para entrada, negativa para saída)
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data da movimentação
);

-- Índices para melhorar a performance nas buscas
CREATE INDEX idx_movimento_estoque_itens_pedido ON movimento_estoque (id_itens_pedido);
CREATE INDEX idx_movimento_estoque_recebimento ON movimento_estoque (id_itens_recebimento_mercadoria);
CREATE INDEX idx_movimento_estoque_produto ON movimento_estoque (id_produto);

-- Criação da tabela de estoque
CREATE TABLE estoque (
    id BIGSERIAL PRIMARY KEY,
    id_produto BIGINT NOT NULL UNIQUE,
    qtd_estoque NUMERIC(10, 4) DEFAULT 0 -- Quantidade disponível em estoque
);

-- Índice para melhorar a performance na busca por produtos no estoque
CREATE INDEX idx_estoque_produto ON estoque (id_produto);

-- Constraints para garantir integridade referencial (opcional)
ALTER TABLE movimento_estoque
ADD CONSTRAINT fk_movimento_estoque_produto
FOREIGN KEY (id_produto) REFERENCES produtos(id) ON DELETE CASCADE;

ALTER TABLE estoque
ADD CONSTRAINT fk_estoque_produto
FOREIGN KEY (id_produto) REFERENCES produtos(id) ON DELETE CASCADE;

ALTER TABLE movimento_estoque
ADD CONSTRAINT fk_movimento_estoque_itens_pedido
FOREIGN KEY (id_itens_pedido) REFERENCES itens_pedido(id) ON DELETE SET NULL;

ALTER TABLE movimento_estoque
ADD CONSTRAINT fk_movimento_estoque_itens_recebimento
FOREIGN KEY (id_itens_recebimento_mercadoria) REFERENCES itens_recebimento_mercadoria(id) ON DELETE SET NULL;
