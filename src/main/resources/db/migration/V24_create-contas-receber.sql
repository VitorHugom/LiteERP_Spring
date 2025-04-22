-- Criação da tabela contas_receber
CREATE TABLE contas_receber (
    id SERIAL PRIMARY KEY,
    id_cliente INT NOT NULL,
    numero_documento VARCHAR(50) NOT NULL,
    parcela INT NOT NULL,
    valor_parcela NUMERIC(10, 2) NOT NULL,
    valor_total NUMERIC(10, 2) NOT NULL,
    id_forma_pagamento INT NOT NULL,
    id_tipo_cobranca INT NOT NULL,
    data_vencimento DATE NOT NULL,
    FOREIGN KEY (id_forma_pagamento) REFERENCES forma_pagamento (id),
    FOREIGN KEY (id_tipo_cobranca) REFERENCES tipos_cobranca (id),
    FOREIGN KEY (id_cliente) REFERENCES clientes (id)
);