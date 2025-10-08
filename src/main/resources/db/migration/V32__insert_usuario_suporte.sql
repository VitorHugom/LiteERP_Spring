-- V32__insert_usuario_suporte.sql
-- Migration para inserir usu�rio de suporte

INSERT INTO usuario (nome_usuario, email, senha, categoria_id, status, telefone)
VALUES (
  'suporte',
  'lite-erp-enterprise@gmail.com',
  '$2a$10$7rha9ohG4X4kwIrErL8Mr.dUhVYE94gKhfAPF8.GlW5bbOWctcRkG',
  2,
  'autorizado',
  '62 99476-8955'
)
ON CONFLICT (email) DO NOTHING;

-- Atualiza sequência de id (ajuste o nome da sequência se for diferente)
SELECT setval('usuario_id_seq', GREATEST((SELECT MAX(id) FROM usuario), 1));

COMMENT ON TABLE usuario IS 'Tabela de usu�rios do sistema';