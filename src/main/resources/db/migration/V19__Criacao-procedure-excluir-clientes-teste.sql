CREATE PROCEDURE limpar_clientes_de_teste()
LANGUAGE plpgsql
AS $$
BEGIN
        -- Exemplo: removendo clientes de teste (usando convenção de nome)
    DELETE FROM clientes WHERE nome_fantasia LIKE 'TESTE%';

    COMMIT;
END;
$$;
