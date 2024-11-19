CREATE OR REPLACE FUNCTION gerar_log_alteracoes_produto()
RETURNS TRIGGER AS $$
BEGIN
    -- Para cada campo que queremos monitorar, verificamos se houve alteração
    IF (NEW.descricao IS DISTINCT FROM OLD.descricao) THEN
        INSERT INTO log_alteracoes_produto (id_produto, campo_alterado, valor_anterior, valor_atual, data_alteracao)
        VALUES (OLD.id, 'descricao', OLD.descricao, NEW.descricao, NOW());
    END IF;

    IF (NEW.grupo_produtos IS DISTINCT FROM OLD.grupo_produtos) THEN
        INSERT INTO log_alteracoes_produto (id_produto, campo_alterado, valor_anterior, valor_atual, data_alteracao)
        VALUES (OLD.id, 'grupo_produtos', OLD.grupo_produtos::TEXT, NEW.grupo_produtos::TEXT, NOW());
    END IF;

    IF (NEW.marca IS DISTINCT FROM OLD.marca) THEN
        INSERT INTO log_alteracoes_produto (id_produto, campo_alterado, valor_anterior, valor_atual, data_alteracao)
        VALUES (OLD.id, 'marca', OLD.marca, NEW.marca, NOW());
    END IF;

    IF (NEW.data_ultima_compra IS DISTINCT FROM OLD.data_ultima_compra) THEN
        INSERT INTO log_alteracoes_produto (id_produto, campo_alterado, valor_anterior, valor_atual, data_alteracao)
        VALUES (OLD.id, 'data_ultima_compra', OLD.data_ultima_compra::TEXT, NEW.data_ultima_compra::TEXT, NOW());
    END IF;

    IF (NEW.preco_compra IS DISTINCT FROM OLD.preco_compra) THEN
        INSERT INTO log_alteracoes_produto (id_produto, campo_alterado, valor_anterior, valor_atual, data_alteracao)
        VALUES (OLD.id, 'preco_compra', OLD.preco_compra::TEXT, NEW.preco_compra::TEXT, NOW());
    END IF;

    IF (NEW.preco_venda IS DISTINCT FROM OLD.preco_venda) THEN
        INSERT INTO log_alteracoes_produto (id_produto, campo_alterado, valor_anterior, valor_atual, data_alteracao)
        VALUES (OLD.id, 'preco_venda', OLD.preco_venda::TEXT, NEW.preco_venda::TEXT, NOW());
    END IF;

    IF (NEW.peso IS DISTINCT FROM OLD.peso) THEN
        INSERT INTO log_alteracoes_produto (id_produto, campo_alterado, valor_anterior, valor_atual, data_alteracao)
        VALUES (OLD.id, 'peso', OLD.peso::TEXT, NEW.peso::TEXT, NOW());
    END IF;

    IF (NEW.cod_ean IS DISTINCT FROM OLD.cod_ean) THEN
        INSERT INTO log_alteracoes_produto (id_produto, campo_alterado, valor_anterior, valor_atual, data_alteracao)
        VALUES (OLD.id, 'cod_ean', OLD.cod_ean, NEW.cod_ean, NOW());
    END IF;

    IF (NEW.cod_ncm IS DISTINCT FROM OLD.cod_ncm) THEN
        INSERT INTO log_alteracoes_produto (id_produto, campo_alterado, valor_anterior, valor_atual, data_alteracao)
        VALUES (OLD.id, 'cod_ncm', OLD.cod_ncm, NEW.cod_ncm, NOW());
    END IF;

    IF (NEW.cod_cest IS DISTINCT FROM OLD.cod_cest) THEN
        INSERT INTO log_alteracoes_produto (id_produto, campo_alterado, valor_anterior, valor_atual, data_alteracao)
        VALUES (OLD.id, 'cod_cest', OLD.cod_cest, NEW.cod_cest, NOW());
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_gerar_log_alteracoes_produto
AFTER UPDATE ON produtos
FOR EACH ROW
EXECUTE FUNCTION gerar_log_alteracoes_produto();
