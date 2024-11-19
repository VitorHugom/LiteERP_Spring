-- V12__corrigir_tipo_cobranca_em_recebimento_mercadorias.sql

-- Remove o campo antigo
ALTER TABLE recebimento_mercadorias
DROP COLUMN tipo_cobranca;

-- Adiciona a nova coluna como referência
ALTER TABLE recebimento_mercadorias
ADD COLUMN tipo_cobranca_id INT NOT NULL;

-- Adiciona a restrição de chave estrangeira
ALTER TABLE recebimento_mercadorias
ADD CONSTRAINT fk_tipo_cobranca
FOREIGN KEY (tipo_cobranca_id) REFERENCES tipos_cobranca (id);
