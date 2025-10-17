-- Atualizar a view relatorio_pedidos para suportar pedidos sem cliente cadastrado
DROP VIEW IF EXISTS relatorio_pedidos;

CREATE VIEW relatorio_pedidos AS
SELECT
    p.id AS pedido_id,
    p.data_emissao,
    p.valor_total,
    p.status,
    p.ultima_atualizacao,

    -- Informações do cliente
    c.id AS cliente_id,
    COALESCE(c.nome_fantasia, c.razao_social, p.cliente_final) AS nome_cliente,
    c.cpf,
    c.cnpj,
    c.email AS email_cliente,
    c.telefone AS telefone_cliente,
    c.celular AS celular_cliente,
    c.endereco || ', ' || c.numero || ' ' || COALESCE(c.complemento, '') AS endereco_cliente,
    cid.nome AS cidade_cliente,
    cid.estado AS estado_cliente,

    -- Informações do vendedor
    v.id AS vendedor_id,
    v.nome AS nome_vendedor,
    v.email AS email_vendedor,
    v.telefone AS telefone_vendedor,

    -- Tipo de cobrança
    tc.descricao AS tipo_cobranca

FROM
    pedidos p
    LEFT JOIN clientes c ON p.id_cliente = c.id
    LEFT JOIN cidades cid ON c.cidade_id = cid.id
    LEFT JOIN vendedores v ON p.id_vendedor = v.id
    INNER JOIN tipos_cobranca tc ON p.id_tipo_cobranca = tc.id;

