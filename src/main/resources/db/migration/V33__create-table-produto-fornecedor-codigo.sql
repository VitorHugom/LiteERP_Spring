-- V33__create-table-produto-fornecedor-codigo.sql
-- Migration para criar tabela de vínculo entre produtos e códigos de fornecedores
-- Esta tabela será utilizada para mapear códigos de produtos dos fornecedores
-- com os produtos cadastrados no sistema durante importação de NFe

-- Criação da tabela produto_fornecedor_codigo
CREATE TABLE produto_fornecedor_codigo (
    id BIGSERIAL PRIMARY KEY,
    id_produto BIGINT NOT NULL,
    id_fornecedor INT NOT NULL,
    codigo_fornecedor VARCHAR(100) NOT NULL,
    ativo BOOLEAN DEFAULT true,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_produto_fornecedor_codigo_produto
        FOREIGN KEY (id_produto) REFERENCES produtos(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_produto_fornecedor_codigo_fornecedor
        FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id)
        ON DELETE CASCADE
);

-- Criar índice único composto para evitar duplicatas de vínculo produto-fornecedor
CREATE UNIQUE INDEX idx_produto_fornecedor_unico 
    ON produto_fornecedor_codigo(id_produto, id_fornecedor);

-- Criar índice para busca por código do fornecedor (usado na importação de NFe)
CREATE INDEX idx_codigo_fornecedor 
    ON produto_fornecedor_codigo(codigo_fornecedor, id_fornecedor);

-- Criar índice para busca por produto
CREATE INDEX idx_produto 
    ON produto_fornecedor_codigo(id_produto);

-- Criar índice para busca por fornecedor
CREATE INDEX idx_fornecedor 
    ON produto_fornecedor_codigo(id_fornecedor);

-- Comentários para documentação
COMMENT ON TABLE produto_fornecedor_codigo IS 'Tabela de vínculo entre produtos do sistema e códigos de produtos dos fornecedores, utilizada para importação de NFe';
COMMENT ON COLUMN produto_fornecedor_codigo.id IS 'Identificador único do vínculo';
COMMENT ON COLUMN produto_fornecedor_codigo.id_produto IS 'Referência ao produto no sistema';
COMMENT ON COLUMN produto_fornecedor_codigo.id_fornecedor IS 'Referência ao fornecedor';
COMMENT ON COLUMN produto_fornecedor_codigo.codigo_fornecedor IS 'Código do produto utilizado pelo fornecedor (aparece no XML da NFe)';
COMMENT ON COLUMN produto_fornecedor_codigo.ativo IS 'Indica se o vínculo está ativo';
COMMENT ON COLUMN produto_fornecedor_codigo.data_cadastro IS 'Data e hora do cadastro do vínculo';

