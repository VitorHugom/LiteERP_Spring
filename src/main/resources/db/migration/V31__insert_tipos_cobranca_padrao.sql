-- V31__insert_tipos_cobranca_padrao.sql
-- Migration para inserir tipos de cobrança padrão
-- Versão simplificada usando INSERT com ON CONFLICT

-- Inserir tipos de cobrança padrão
INSERT INTO tipos_cobranca (id, descricao) VALUES
(1, 'Dinheiro'),
(2, 'PIX'),
(3, 'C. Crédito'),
(4, 'C. Débito')
ON CONFLICT (id) DO NOTHING;

-- Atualizar a sequência para o próximo ID disponível
SELECT setval('tipos_cobranca_id_seq', GREATEST((SELECT MAX(id) FROM tipos_cobranca), 4));

-- Comentário sobre a migration
COMMENT ON TABLE tipos_cobranca IS 'Tabela contendo os tipos de cobrança disponíveis no sistema';
