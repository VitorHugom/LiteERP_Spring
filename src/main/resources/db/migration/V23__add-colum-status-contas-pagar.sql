-- Migration para adicionar a coluna 'status' na tabela 'contas_pagar'

ALTER TABLE contas_pagar
ADD COLUMN status VARCHAR(20) DEFAULT 'aberta';

