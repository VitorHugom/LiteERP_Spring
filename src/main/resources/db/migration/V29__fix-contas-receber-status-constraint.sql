-- Migration para corrigir a constraint de status da tabela contas_receber
-- Aceitar 'paga' ao invés de 'recebida' para manter compatibilidade com o sistema existente

-- Primeiro, converter 'recebida' para 'paga' para manter consistência
UPDATE contas_receber
SET status = 'paga'
WHERE status = 'recebida';

-- Depois, corrigir qualquer outro status inválido para 'aberta'
UPDATE contas_receber
SET status = 'aberta'
WHERE status IS NULL
   OR status NOT IN ('aberta', 'paga', 'cancelada', 'vencida');

-- Remover a constraint atual que não aceita 'paga'
ALTER TABLE contas_receber DROP CONSTRAINT IF EXISTS contas_receber_status_check;

-- Criar nova constraint que aceita 'paga' ao invés de 'recebida'
ALTER TABLE contas_receber ADD CONSTRAINT contas_receber_status_check
    CHECK (status IN ('aberta', 'paga', 'cancelada', 'vencida'));

-- Comentário explicativo
COMMENT ON CONSTRAINT contas_receber_status_check ON contas_receber IS
    'Status válidos: aberta (pendente), paga (quitada), cancelada, vencida';

-- Comentário explicativo
COMMENT ON CONSTRAINT contas_receber_status_check ON contas_receber IS
    'Status válidos: aberta (pendente), paga (quitada), cancelada, vencida';
