-- V9__Create_pedidos_schema.sql

-- Tabela para armazenar tipos de cobrança
CREATE TABLE tipos_cobranca (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL -- Ex: "Cartão de Crédito", "Boleto"
);

-- Tabela de pedidos de vendas
CREATE TABLE pedidos (
    id BIGSERIAL PRIMARY KEY,
    id_cliente BIGINT NOT NULL REFERENCES clientes(id) ON DELETE CASCADE, -- Chave estrangeira para a tabela de clientes
    id_vendedor BIGINT NOT NULL REFERENCES vendedores(id) ON DELETE SET NULL, -- Chave estrangeira para a tabela de vendedores
    data_emissao TIMESTAMP NOT NULL, -- Data e hora de emissão do pedido
    valor_total DECIMAL(10, 2) NOT NULL, -- Valor total do pedido
    status VARCHAR(50) NOT NULL CHECK (status IN ('aguardando', 'em_producao', 'entregue')), -- Status do pedido
    id_tipo_cobranca BIGINT NOT NULL REFERENCES tipos_cobranca(id) ON DELETE RESTRICT, -- Tipo de cobrança
    ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data de atualização
);

-- Tabela de itens do pedido, N para N entre pedidos e produtos
CREATE TABLE itens_pedido (
    id BIGSERIAL PRIMARY KEY,
    id_pedido BIGINT NOT NULL REFERENCES pedidos(id) ON DELETE CASCADE, -- Chave estrangeira para a tabela de pedidos
    id_produto BIGINT NOT NULL REFERENCES produtos(id) ON DELETE CASCADE, -- Chave estrangeira para a tabela de produtos
    preco DECIMAL(10, 2) NOT NULL, -- Preço do produto no pedido
    quantidade INTEGER NOT NULL CHECK (quantidade > 0) -- Quantidade do produto no pedido
);
