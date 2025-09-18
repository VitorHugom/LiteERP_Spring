-- Migration para criar contas de caixa padrão para usuários existentes

-- Criar conta de caixa padrão para todos os usuários existentes que não possuem conta
INSERT INTO conta_caixa (descricao, tipo, saldo_atual, usuario_responsavel_id, ativo, data_criacao)
SELECT 
    CONCAT('Caixa - ', u.nome_usuario) as descricao,
    'CAIXA_FISICO' as tipo,
    0.00 as saldo_atual,
    u.id as usuario_responsavel_id,
    true as ativo,
    CURRENT_TIMESTAMP as data_criacao
FROM usuario u
WHERE u.status = 'autorizado' 
  AND NOT EXISTS (
      SELECT 1 FROM conta_caixa cc WHERE cc.usuario_responsavel_id = u.id
  );

-- Comentário explicativo
COMMENT ON TABLE conta_caixa IS 'Cada usuário autorizado recebe automaticamente uma conta de caixa padrão';
