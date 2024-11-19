-- Adicionando a nova coluna 'cliente_final' e tornando o campo 'id_cliente' opcional
ALTER TABLE pedidos
    ADD COLUMN cliente_final VARCHAR(255);

-- Modificando a coluna 'id_cliente' para ser opcional
ALTER TABLE pedidos
    ALTER COLUMN id_cliente DROP NOT NULL;
