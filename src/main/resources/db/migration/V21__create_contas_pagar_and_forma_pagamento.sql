-- Criação da tabela forma_pagamento
CREATE TABLE forma_pagamento (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL
);

-- Criação da tabela dias_forma_pagamento
CREATE TABLE dias_forma_pagamento (
    id SERIAL PRIMARY KEY,
    id_forma_pagamento INT NOT NULL,
    dias_para_vencimento INT NOT NULL,
    FOREIGN KEY (id_forma_pagamento) REFERENCES forma_pagamento (id) ON DELETE CASCADE
);

-- Criação da tabela contas_pagar
CREATE TABLE contas_pagar (
    id SERIAL PRIMARY KEY,
    id_fornecedor INT NOT NULL,
    numero_documento VARCHAR(50) NOT NULL,
    parcela INT NOT NULL,
    valor_parcela NUMERIC(10, 2) NOT NULL,
    valor_total NUMERIC(10, 2) NOT NULL,
    id_forma_pagamento INT NOT NULL,
    id_tipo_cobranca INT NOT NULL,
    data_vencimento DATE NOT NULL,
    FOREIGN KEY (id_forma_pagamento) REFERENCES forma_pagamento (id),
    FOREIGN KEY (id_tipo_cobranca) REFERENCES tipos_cobranca (id),
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedores (id)
);

-- Index para melhorar consultas por forma_pagamento
CREATE INDEX idx_contas_pagar_forma_pagamento ON contas_pagar (id_forma_pagamento);

-- Index para melhorar consultas por fornecedor
CREATE INDEX idx_contas_pagar_fornecedor ON contas_pagar (id_fornecedor);
