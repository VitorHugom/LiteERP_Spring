-- V4__add_foreign_key_to_usuario.sql

-- Certificar que a coluna categoria_id está corretamente referenciada e criar a chave estrangeira
ALTER TABLE usuario
ADD CONSTRAINT fk_categoria_usuario
FOREIGN KEY (categoria_id)
REFERENCES categorias_usuario(id);

-- Adicionar um índice para a coluna categoria_id para melhorar a performance de consultas que usam essa coluna
CREATE INDEX idx_categoria_id ON usuario(categoria_id);
