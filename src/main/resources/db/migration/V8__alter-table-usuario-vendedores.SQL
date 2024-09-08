-- Migration para adicionar a coluna telefone em usuario
ALTER TABLE usuario
ADD COLUMN telefone VARCHAR(20);

-- Migration para adicionar a coluna usuario_id em vendedores e criar o relacionamento com usuario
ALTER TABLE vendedores
ADD COLUMN usuario_id BIGINT;

-- Criar a foreign key constraint entre vendedor e usuario
ALTER TABLE vendedores
ADD CONSTRAINT fk_usuario_vendedor FOREIGN KEY (usuario_id) REFERENCES usuario(id);
