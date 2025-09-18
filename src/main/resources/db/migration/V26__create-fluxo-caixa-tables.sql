-- Criação das tabelas para o módulo de Fluxo de Caixa

-- Tabela para tipos de movimentação
CREATE TABLE tipo_movimentacao (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    categoria VARCHAR(20) NOT NULL CHECK (categoria IN ('RECEITA', 'DESPESA', 'TRANSFERENCIA')),
    cor_hex VARCHAR(7),
    ativo BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela para centros de custo
CREATE TABLE centro_custo (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    ativo BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela para contas de caixa
CREATE TABLE conta_caixa (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('CAIXA_FISICO', 'CONTA_BANCARIA', 'CARTEIRA_DIGITAL')),
    banco VARCHAR(100),
    agencia VARCHAR(20),
    conta VARCHAR(30),
    saldo_atual NUMERIC(15, 2) DEFAULT 0.00,
    usuario_responsavel_id BIGINT NOT NULL,
    ativo BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_responsavel_id) REFERENCES usuario (id)
);

-- Tabela para movimentações de caixa
CREATE TABLE movimentacao_caixa (
    id SERIAL PRIMARY KEY,
    conta_caixa_id BIGINT NOT NULL,
    tipo_movimentacao_id BIGINT NOT NULL,
    centro_custo_id BIGINT,
    tipo_origem VARCHAR(20) NOT NULL CHECK (tipo_origem IN ('MANUAL', 'CONTA_PAGAR', 'CONTA_RECEBER', 'TRANSFERENCIA')),
    referencia_id BIGINT,
    numero_documento VARCHAR(50),
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(15, 2) NOT NULL,
    data_movimentacao DATE NOT NULL,
    data_lancamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_lancamento_id BIGINT NOT NULL,
    observacoes TEXT,
    status VARCHAR(20) DEFAULT 'CONFIRMADO' CHECK (status IN ('PENDENTE', 'CONFIRMADO', 'CANCELADO')),
    FOREIGN KEY (conta_caixa_id) REFERENCES conta_caixa (id),
    FOREIGN KEY (tipo_movimentacao_id) REFERENCES tipo_movimentacao (id),
    FOREIGN KEY (centro_custo_id) REFERENCES centro_custo (id),
    FOREIGN KEY (usuario_lancamento_id) REFERENCES usuario (id)
);

-- Índices para melhorar performance
CREATE INDEX idx_movimentacao_caixa_conta ON movimentacao_caixa (conta_caixa_id);
CREATE INDEX idx_movimentacao_caixa_data ON movimentacao_caixa (data_movimentacao);
CREATE INDEX idx_movimentacao_caixa_tipo_origem ON movimentacao_caixa (tipo_origem, referencia_id);
CREATE INDEX idx_movimentacao_caixa_usuario ON movimentacao_caixa (usuario_lancamento_id);
CREATE INDEX idx_conta_caixa_responsavel ON conta_caixa (usuario_responsavel_id);

-- Inserir tipos de movimentação padrão
INSERT INTO tipo_movimentacao (descricao, categoria, cor_hex) VALUES
('Venda à Vista', 'RECEITA', '#28a745'),
('Recebimento de Cliente', 'RECEITA', '#20c997'),
('Receita Financeira', 'RECEITA', '#17a2b8'),
('Outras Receitas', 'RECEITA', '#6f42c1'),
('Pagamento de Fornecedor', 'DESPESA', '#dc3545'),
('Despesas Operacionais', 'DESPESA', '#fd7e14'),
('Despesas Administrativas', 'DESPESA', '#e83e8c'),
('Impostos e Taxas', 'DESPESA', '#6c757d'),
('Transferência entre Contas', 'TRANSFERENCIA', '#007bff');

-- Inserir centros de custo padrão
INSERT INTO centro_custo (descricao, codigo) VALUES
('Vendas', 'VENDAS'),
('Administrativo', 'ADMIN'),
('Financeiro', 'FINANC'),
('Operacional', 'OPER'),
('Marketing', 'MARKET');
