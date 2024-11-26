-- Alteração da tabela recebimento_mercadorias para adicionar o id_forma_pagamento
ALTER TABLE recebimento_mercadorias
ADD COLUMN id_forma_pagamento INT NOT NULL,
ADD CONSTRAINT fk_recebimento_mercadorias_forma_pagamento
    FOREIGN KEY (id_forma_pagamento)
    REFERENCES forma_pagamento (id)
    ON DELETE RESTRICT;

-- Opcional: criar índice para otimizar consultas
CREATE INDEX idx_recebimento_mercadorias_forma_pagamento ON recebimento_mercadorias (id_forma_pagamento);
