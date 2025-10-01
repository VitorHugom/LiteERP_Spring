-- V31__insert_tipos_cobranca_padrao.sql
-- Migration para inserir tipos de cobrança padrão
-- Só executa se a tabela estiver vazia

DO $$
BEGIN
    -- Verifica se a tabela já tem tipos de cobrança
    IF (SELECT COUNT(*) FROM tipos_cobranca) = 0 THEN
        -- Inserir tipos de cobrança padrão
        INSERT INTO tipos_cobranca (id, descricao) VALUES
        (1, 'Dinheiro'),
        (2, 'PIX'),
        (3, 'C. Crédito'),
        (4, 'C. Débito');
        
        -- Atualizar a sequência para o próximo ID disponível
        SELECT setval('tipos_cobranca_id_seq', (SELECT MAX(id) FROM tipos_cobranca));
        
        RAISE NOTICE 'Inseridos % tipos de cobrança padrão', (SELECT COUNT(*) FROM tipos_cobranca);
    ELSE
        RAISE NOTICE 'Tabela tipos_cobranca já contém % registros. Migration não executada.', (SELECT COUNT(*) FROM tipos_cobranca);
    END IF;
END $$;

-- Comentário sobre a migration
COMMENT ON TABLE tipos_cobranca IS 'Tabela contendo os tipos de cobrança disponíveis no sistema';
