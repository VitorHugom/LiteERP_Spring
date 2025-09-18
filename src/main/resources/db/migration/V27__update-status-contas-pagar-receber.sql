-- Migration para adicionar novos status às contas a pagar e receber

-- Primeiro, limpar dados problemáticos
UPDATE contas_pagar SET status = TRIM(status) WHERE status IS NOT NULL;
UPDATE contas_receber SET status = TRIM(status) WHERE status IS NOT NULL;

-- Converter 'paga' para 'paga' e outros ajustes se necessário
UPDATE contas_receber SET status = 'recebida' WHERE status = 'paga';

-- Atualizar constraint da tabela contas_pagar para incluir novos status
ALTER TABLE contas_pagar DROP CONSTRAINT IF EXISTS contas_pagar_status_check;
ALTER TABLE contas_pagar ADD CONSTRAINT contas_pagar_status_check
    CHECK (status IN ('aberta', 'paga', 'cancelada', 'vencida'));

-- Atualizar constraint da tabela contas_receber para incluir novos status
ALTER TABLE contas_receber DROP CONSTRAINT IF EXISTS contas_receber_status_check;
ALTER TABLE contas_receber ADD CONSTRAINT contas_receber_status_check
    CHECK (status IN ('aberta', 'recebida', 'cancelada', 'vencida'));

-- Criar índices para melhorar performance das consultas do fluxo de caixa
-- Verificar se as tabelas existem antes de criar índices
DO $$
BEGIN
    -- Índices para movimentacao_caixa
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'movimentacao_caixa') THEN
        CREATE INDEX IF NOT EXISTS idx_movimentacao_caixa_status ON movimentacao_caixa (status);
        CREATE INDEX IF NOT EXISTS idx_movimentacao_caixa_data_status ON movimentacao_caixa (data_movimentacao, status);
    END IF;

    -- Índices para conta_caixa
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'conta_caixa') THEN
        CREATE INDEX IF NOT EXISTS idx_conta_caixa_ativo ON conta_caixa (ativo);
    END IF;

    -- Índices para tipo_movimentacao
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tipo_movimentacao') THEN
        CREATE INDEX IF NOT EXISTS idx_tipo_movimentacao_categoria ON tipo_movimentacao (categoria, ativo);
    END IF;

    -- Índices para centro_custo
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'centro_custo') THEN
        CREATE INDEX IF NOT EXISTS idx_centro_custo_ativo ON centro_custo (ativo);
    END IF;
END $$;

-- Comentários nas tabelas para documentação
COMMENT ON TABLE conta_caixa IS 'Tabela para armazenar as contas de caixa (físico, bancário, digital)';
COMMENT ON TABLE movimentacao_caixa IS 'Tabela para armazenar todas as movimentações financeiras';
COMMENT ON TABLE tipo_movimentacao IS 'Tabela para categorizar os tipos de movimentação (receita, despesa, transferência)';
COMMENT ON TABLE centro_custo IS 'Tabela para controle de centros de custo/departamentos';

-- Comentários em colunas importantes
COMMENT ON COLUMN movimentacao_caixa.tipo_origem IS 'Origem da movimentação: MANUAL, CONTA_PAGAR, CONTA_RECEBER, TRANSFERENCIA';
COMMENT ON COLUMN movimentacao_caixa.valor IS 'Valor da movimentação (positivo=entrada, negativo=saída)';
COMMENT ON COLUMN movimentacao_caixa.referencia_id IS 'ID de referência para movimentações automáticas (conta a pagar/receber)';
COMMENT ON COLUMN conta_caixa.saldo_atual IS 'Saldo atual calculado baseado nas movimentações';
COMMENT ON COLUMN tipo_movimentacao.categoria IS 'Categoria: RECEITA, DESPESA ou TRANSFERENCIA';
COMMENT ON COLUMN centro_custo.codigo IS 'Código único identificador do centro de custo';
