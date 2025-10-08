-- V32__insert_usuario_suporte.sql
-- Migration para inserir usuário de suporte

INSERT INTO usuarios (nome_usuario, email, senha, categoria_id, status, telefone, criado_em)
VALUES (
  'suporte',
  'lite-erp-enterprise@gmail.com',
  '$2a$10$7rha9ohG4X4kwIrErL8Mr.dUhVYE94gKhfAPF8.GlW5bbOWctcRkG',
  2,
  'autorizado',
  '62 99476-8955',
  now()
)
ON CONFLICT (email) DO NOTHING;

-- Atualiza sequência de id (ajuste o nome da sequência se for diferente)
SELECT setval('usuarios_id_seq', GREATEST((SELECT MAX(id) FROM usuarios), 1));

COMMENT ON TABLE usuarios IS 'Tabela de usuários do sistema';