-- Description: Criação das tabelas recebimento_mercadorias e itens_recebimento_mercadoria

-- Criação da tabela recebimento_mercadorias
CREATE TABLE recebimento_mercadorias (
    id_recebimento SERIAL PRIMARY KEY,
    fornecedor_id INT NOT NULL,
    tipo_cobranca VARCHAR(50) NOT NULL,
    data_recebimento DATE NOT NULL,
    CONSTRAINT fk_fornecedor
        FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id)
        ON DELETE CASCADE
);

-- Criação da tabela itens_recebimento_mercadoria
CREATE TABLE itens_recebimento_mercadoria (
    id SERIAL PRIMARY KEY,
    id_recebimento INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade NUMERIC(10, 2) NOT NULL CHECK (quantidade > 0),
    valor_unitario NUMERIC(12, 2) NOT NULL CHECK (valor_unitario >= 0),
    CONSTRAINT fk_recebimento
        FOREIGN KEY (id_recebimento) REFERENCES recebimento_mercadorias(id_recebimento)
        ON DELETE CASCADE,
    CONSTRAINT fk_produto
        FOREIGN KEY (id_produto) REFERENCES produtos(id)
        ON DELETE CASCADE
);
