-- V34__fix-produto-fornecedor-codigo-constraints.sql
-- Migration para corrigir constraints da tabela produto_fornecedor_codigo
-- Permite que um mesmo produto tenha múltiplos códigos do mesmo fornecedor
-- Mas impede que o mesmo código do fornecedor seja duplicado

-- Remover o índice único que impedia múltiplos códigos para o mesmo produto-fornecedor
DROP INDEX IF EXISTS idx_produto_fornecedor_unico;

-- Remover o índice antigo de código_fornecedor (não era único)
DROP INDEX IF EXISTS idx_codigo_fornecedor;

-- Criar índice ÚNICO para código_fornecedor + id_fornecedor
-- Isso garante que o mesmo código do fornecedor não seja usado para produtos diferentes
-- Mas permite que o mesmo produto tenha vários códigos do mesmo fornecedor
CREATE UNIQUE INDEX idx_codigo_fornecedor_unico 
    ON produto_fornecedor_codigo(codigo_fornecedor, id_fornecedor);

-- Comentário explicativo
COMMENT ON INDEX idx_codigo_fornecedor_unico IS 'Garante que cada código do fornecedor seja único, mas permite múltiplos códigos para o mesmo produto';

