-- Migration para corrigir o tipo da coluna data_cadastro de TIMESTAMP para DATE
-- e garantir que a data de nascimento seja do tipo DATE

-- Alterar o tipo da coluna data_cadastro de TIMESTAMP para DATE
ALTER TABLE clientes 
ALTER COLUMN data_cadastro TYPE DATE USING data_cadastro::DATE;

-- Garantir que o default seja CURRENT_DATE ao invés de NOW()
ALTER TABLE clientes 
ALTER COLUMN data_cadastro SET DEFAULT CURRENT_DATE;

-- Comentário explicativo
COMMENT ON COLUMN clientes.data_cadastro IS 'Data de cadastro do cliente (apenas data, sem hora)';
COMMENT ON COLUMN clientes.data_nascimento IS 'Data de nascimento do cliente';

