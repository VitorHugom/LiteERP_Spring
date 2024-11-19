-- Criação de índices na tabela 'produtos'
-- Índice para busca por descrição, que pode ser comum em pesquisas de produtos por nome
CREATE INDEX idx_produtos_descricao ON produtos (descricao);
-- Índice para busca por código EAN, usado em pesquisas por código de barras
CREATE INDEX idx_produtos_cod_ean ON produtos (cod_ean);
-- Índice para busca por código NCM, importante para categorias fiscais de produtos
CREATE INDEX idx_produtos_cod_ncm ON produtos (cod_ncm);

-- Criação de índices na tabela 'clientes'
-- Índice para busca por nome_fantasia, utilizado para clientes do tipo empresa
CREATE INDEX idx_clientes_nome_fantasia ON clientes (nome_fantasia);
-- Índice para busca por razão social, utilizado em pesquisas formais
CREATE INDEX idx_clientes_razao_social ON clientes (razao_social);
-- Índice para busca por CPF, comum em clientes do tipo pessoa física
CREATE INDEX idx_clientes_cpf ON clientes (cpf);
-- Índice para busca por CNPJ, comum em clientes do tipo pessoa jurídica
CREATE INDEX idx_clientes_cnpj ON clientes (cnpj);
-- Índice para busca por cidade_id, em pesquisas por localização de clientes
CREATE INDEX idx_clientes_cidade_id ON clientes (cidade_id);

-- Criação de índices na tabela 'vendedores'
-- Índice para busca por nome do vendedor, utilizado em pesquisas de vendas por vendedor
CREATE INDEX idx_vendedores_nome ON vendedores (nome);

-- Criação de índices na tabela 'pedidos'
-- Índice para busca por id_cliente, essencial em buscas de pedidos por cliente
CREATE INDEX idx_pedidos_id_cliente ON pedidos (id_cliente);
-- Índice para busca por id_vendedor, útil para relatórios de pedidos por vendedor
CREATE INDEX idx_pedidos_id_vendedor ON pedidos (id_vendedor);
-- Índice para busca por data_emissao, essencial para filtros por datas de pedidos
CREATE INDEX idx_pedidos_data_emissao ON pedidos (data_emissao);
-- Índice para status de pedidos, útil para consultas rápidas por estado do pedido
CREATE INDEX idx_pedidos_status ON pedidos (status);
-- Índice para id_tipo_cobranca, em buscas por tipo de cobrança dos pedidos
CREATE INDEX idx_pedidos_id_tipo_cobranca ON pedidos (id_tipo_cobranca);

-- Criação de índices na tabela 'itens_pedido'
-- Índice para busca por id_pedido, usado para consultar itens de um pedido específico
CREATE INDEX idx_itens_pedido_id_pedido ON itens_pedido (id_pedido);
-- Índice para busca por id_produto, utilizado em consultas de itens de pedidos por produto
CREATE INDEX idx_itens_pedido_id_produto ON itens_pedido (id_produto);
-- Índice para preço, útil para buscas por faixa de preços em relatórios de vendas
CREATE INDEX idx_itens_pedido_preco ON itens_pedido (preco);
-- Índice para quantidade, utilizado em relatórios de volume de vendas
CREATE INDEX idx_itens_pedido_quantidade ON itens_pedido (quantidade);

-- Criação de índices na tabela 'fornecedores'
-- Índice para busca por razão social, utilizado em pesquisas formais de fornecedores
CREATE INDEX idx_fornecedores_razao_social ON fornecedores (razao_social);
-- Índice para busca por nome fantasia, comum em operações cotidianas
CREATE INDEX idx_fornecedores_nome_fantasia ON fornecedores (nome_fantasia);
-- Índice para busca por CNPJ, importante em consultas fiscais
CREATE INDEX idx_fornecedores_cnpj ON fornecedores (cnpj);

-- Criação de índices na tabela 'recebimento_mercadorias'
-- Índice para busca por fornecedor_id, importante para buscas de recebimentos por fornecedor
CREATE INDEX idx_recebimento_mercadorias_fornecedor_id ON recebimento_mercadorias (fornecedor_id);
-- Índice para data de recebimento, essencial em relatórios de recebimentos por data
CREATE INDEX idx_recebimento_mercadorias_data_recebimento ON recebimento_mercadorias (data_recebimento);

-- Criação de índices na tabela 'itens_recebimento_mercadoria'
-- Índice para id_recebimento, utilizado para consultar itens de um recebimento específico
CREATE INDEX idx_itens_recebimento_mercadoria_id_recebimento ON itens_recebimento_mercadoria (id_recebimento);
-- Índice para id_produto, importante para buscas de itens recebidos por produto
CREATE INDEX idx_itens_recebimento_mercadoria_id_produto ON itens_recebimento_mercadoria (id_produto);
-- Índice para valor unitário, em relatórios de custo de mercadorias recebidas
CREATE INDEX idx_itens_recebimento_mercadoria_valor_unitario ON itens_recebimento_mercadoria (valor_unitario);

-- Criação de índices na tabela 'movimento_estoque'
-- Índice para busca por id_itens_pedido, essencial para rastreamento de movimentações de pedidos
CREATE INDEX idx_movimento_estoque_id_itens_pedido ON movimento_estoque (id_itens_pedido);
-- Índice para busca por id_itens_recebimento_mercadoria, importante para rastreamento de movimentações de recebimento
CREATE INDEX idx_movimento_estoque_id_itens_recebimento ON movimento_estoque (id_itens_recebimento_mercadoria);
-- Índice para busca por id_produto, para pesquisas rápidas de movimentações por produto
CREATE INDEX idx_movimento_estoque_id_produto ON movimento_estoque (id_produto);
-- Índice para data da movimentação, útil em relatórios históricos de estoque
CREATE INDEX idx_movimento_estoque_data_movimentacao ON movimento_estoque (data_movimentacao);
