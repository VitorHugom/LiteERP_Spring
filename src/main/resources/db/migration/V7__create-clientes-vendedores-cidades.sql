-- V7__create_clientes_vendedores_cidades.sql

-- Criação da tabela cidades
CREATE TABLE cidades (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    codigo_ibge VARCHAR(10) NOT NULL
);

-- Criação da tabela vendedores
CREATE TABLE vendedores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    telefone VARCHAR(20)
);

-- Criação da tabela clientes
CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    tipo_pessoa VARCHAR(10) NOT NULL, -- 'fisica' ou 'juridica'
    cpf VARCHAR(11),
    cnpj VARCHAR(14),
    nome_fantasia VARCHAR(255),
    razao_social VARCHAR(255),
    cep VARCHAR(10),
    endereco VARCHAR(255),
    complemento VARCHAR(255),
    numero VARCHAR(10),
    bairro VARCHAR(255),
    cidade_id BIGINT REFERENCES cidades(id), -- FK para a tabela cidades
    celular VARCHAR(20),
    telefone VARCHAR(20),
    rg VARCHAR(20),
    data_nascimento DATE,
    email VARCHAR(255),
    estado_inscricao_estadual BOOLEAN, -- TRUE para tem inscrição estadual, FALSE para não tem
    inscricao_estadual VARCHAR(20),
    vendedor_id BIGINT REFERENCES vendedores(id), -- FK para a tabela vendedores
    observacao TEXT,
    status BOOLEAN DEFAULT TRUE, -- TRUE para liberado, FALSE para bloqueado
    data_cadastro TIMESTAMP DEFAULT NOW(),
    limite_credito NUMERIC(10, 2)
);
