-- Criar tabela para armazenar as categorias de usuário
CREATE TABLE categorias_usuario (
    id SERIAL PRIMARY KEY,
    nome_categoria VARCHAR(50) UNIQUE NOT NULL
);

-- Inserir categorias iniciais
INSERT INTO categorias_usuario (nome_categoria) VALUES
('vendas'),
('gerencial'),
('caixa');

-- Criar tabela de usuários
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome_usuario VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    categoria_id INTEGER REFERENCES categorias_usuario(id) NOT NULL
);
