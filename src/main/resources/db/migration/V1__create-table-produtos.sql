CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    grupo_produtos VARCHAR(100),
    marca VARCHAR(100),
    data_ultima_compra DATE,
    preco_compra DECIMAL(10, 2),
    preco_venda DECIMAL(10, 2),
    peso DECIMAL(10, 3),
    cod_ean VARCHAR(13),
    cod_ncm VARCHAR(8),
    cod_cest VARCHAR(7)
);
