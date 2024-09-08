-- Criando a tabela grupo_produtos
CREATE TABLE grupo_produtos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- Alterando a tabela produtos para criar o relacionamento com grupo_produtos
ALTER TABLE produtos
    ADD CONSTRAINT fk_grupo_produtos
    FOREIGN KEY (grupo_produtos)
    REFERENCES grupo_produtos (id)
    ON DELETE SET NULL
    ON UPDATE CASCADE;
