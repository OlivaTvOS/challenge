-- Sprint 4 - Carga demonstrativa do SoulUp/Kairos
-- Executar depois de modelo.sql.
-- Insere cinco registros nas tabelas de negocio utilizadas pelo projeto.
-- Os dados sao ficticios e destinados somente a demonstracao academica.
-- As tabelas SPRING_SESSION e SPRING_SESSION_ATTRIBUTES sao estruturas tecnicas
-- e nao recebem dados nesta carga.
--
-- A carga utiliza as sequences e triggers do modelo para gerar as chaves.
-- Pode ser executada em schema que ja possua dados de usuarios, categorias e missoes:
-- os registros de demonstracao usam identificadores naturais exclusivos.
-- Execute uma unica vez para evitar duplicar os registros demonstrativos.

SET SERVEROUTPUT ON;

DECLARE
    v_categoria_1 NUMBER;
    v_categoria_2 NUMBER;
    v_categoria_3 NUMBER;
    v_categoria_4 NUMBER;
    v_categoria_5 NUMBER;
    v_categoria_6 NUMBER;
    v_categoria_6 NUMBER;

    v_usuario_1 NUMBER;
    v_usuario_2 NUMBER;
    v_usuario_3 NUMBER;
    v_usuario_4 NUMBER;
    v_usuario_5 NUMBER;

    v_missao_1 NUMBER;
    v_missao_2 NUMBER;
    v_missao_3 NUMBER;
    v_missao_4 NUMBER;
    v_missao_5 NUMBER;

    v_funcionario NUMBER;
BEGIN
    -- 1. CATEGORIA
    MERGE INTO CATEGORIA destino
    USING (SELECT 1 ID_CATEGORIA, 'RECICLAGEM' NOME, 'Acoes de separacao e descarte correto de residuos reciclaveis.' DESCRICAO FROM DUAL) origem
    ON (destino.ID_CATEGORIA = origem.ID_CATEGORIA)
    WHEN MATCHED THEN UPDATE SET destino.NOME = origem.NOME, destino.DESCRICAO = origem.DESCRICAO
    WHEN NOT MATCHED THEN INSERT (ID_CATEGORIA, NOME, DESCRICAO)
    VALUES (origem.ID_CATEGORIA, origem.NOME, origem.DESCRICAO);

    MERGE INTO CATEGORIA destino
    USING (SELECT 2 ID_CATEGORIA, 'MOBILIDADE' NOME, 'Uso de transporte sustentavel como bicicleta e transporte publico.' DESCRICAO FROM DUAL) origem
    ON (destino.ID_CATEGORIA = origem.ID_CATEGORIA)
    WHEN MATCHED THEN UPDATE SET destino.NOME = origem.NOME, destino.DESCRICAO = origem.DESCRICAO
    WHEN NOT MATCHED THEN INSERT (ID_CATEGORIA, NOME, DESCRICAO)
    VALUES (origem.ID_CATEGORIA, origem.NOME, origem.DESCRICAO);

    MERGE INTO CATEGORIA destino
    USING (SELECT 3 ID_CATEGORIA, 'AGUA' NOME, 'Economia e reuso consciente de agua.' DESCRICAO FROM DUAL) origem
    ON (destino.ID_CATEGORIA = origem.ID_CATEGORIA)
    WHEN MATCHED THEN UPDATE SET destino.NOME = origem.NOME, destino.DESCRICAO = origem.DESCRICAO
    WHEN NOT MATCHED THEN INSERT (ID_CATEGORIA, NOME, DESCRICAO)
    VALUES (origem.ID_CATEGORIA, origem.NOME, origem.DESCRICAO);

    MERGE INTO CATEGORIA destino
    USING (SELECT 4 ID_CATEGORIA, 'ENERGIA' NOME, 'Reducao do consumo de energia eletrica e uso eficiente.' DESCRICAO FROM DUAL) origem
    ON (destino.ID_CATEGORIA = origem.ID_CATEGORIA)
    WHEN MATCHED THEN UPDATE SET destino.NOME = origem.NOME, destino.DESCRICAO = origem.DESCRICAO
    WHEN NOT MATCHED THEN INSERT (ID_CATEGORIA, NOME, DESCRICAO)
    VALUES (origem.ID_CATEGORIA, origem.NOME, origem.DESCRICAO);

    MERGE INTO CATEGORIA destino
    USING (SELECT 5 ID_CATEGORIA, 'NATUREZA' NOME, 'Contato, preservacao e plantio de arvores e areas verdes.' DESCRICAO FROM DUAL) origem
    ON (destino.ID_CATEGORIA = origem.ID_CATEGORIA)
    WHEN MATCHED THEN UPDATE SET destino.NOME = origem.NOME, destino.DESCRICAO = origem.DESCRICAO
    WHEN NOT MATCHED THEN INSERT (ID_CATEGORIA, NOME, DESCRICAO)
    VALUES (origem.ID_CATEGORIA, origem.NOME, origem.DESCRICAO);

    MERGE INTO CATEGORIA destino
    USING (SELECT 6 ID_CATEGORIA, 'CONSUMO' NOME, 'Consumo consciente e reducao de desperdicios.' DESCRICAO FROM DUAL) origem
    ON (destino.ID_CATEGORIA = origem.ID_CATEGORIA)
    WHEN MATCHED THEN UPDATE SET destino.NOME = origem.NOME, destino.DESCRICAO = origem.DESCRICAO
    WHEN NOT MATCHED THEN INSERT (ID_CATEGORIA, NOME, DESCRICAO)
    VALUES (origem.ID_CATEGORIA, origem.NOME, origem.DESCRICAO);

    SELECT ID_CATEGORIA INTO v_categoria_1 FROM CATEGORIA WHERE NOME = 'RECICLAGEM';
    SELECT ID_CATEGORIA INTO v_categoria_2 FROM CATEGORIA WHERE NOME = 'MOBILIDADE';
    SELECT ID_CATEGORIA INTO v_categoria_3 FROM CATEGORIA WHERE NOME = 'AGUA';
    SELECT ID_CATEGORIA INTO v_categoria_4 FROM CATEGORIA WHERE NOME = 'ENERGIA';
    SELECT ID_CATEGORIA INTO v_categoria_5 FROM CATEGORIA WHERE NOME = 'NATUREZA';
    SELECT ID_CATEGORIA INTO v_categoria_6 FROM CATEGORIA WHERE NOME = 'CONSUMO';

    -- 2. USUARIO
    INSERT INTO USUARIO (ID_USUARIO, NOME_USUARIO, NOME, EMAIL, SENHA, PONTOS, TIPO_USUARIO)
    VALUES (NULL, 'demo_sprint4_01', 'Ana Souza', 'demo.sprint4.01@example.invalid', 'demo123', 50, 'USUARIO')
    RETURNING ID_USUARIO INTO v_usuario_1;

    INSERT INTO USUARIO (ID_USUARIO, NOME_USUARIO, NOME, EMAIL, SENHA, PONTOS, TIPO_USUARIO)
    VALUES (NULL, 'demo_sprint4_02', 'Bruno Lima', 'demo.sprint4.02@example.invalid', 'demo123', 40, 'USUARIO')
    RETURNING ID_USUARIO INTO v_usuario_2;

    INSERT INTO USUARIO (ID_USUARIO, NOME_USUARIO, NOME, EMAIL, SENHA, PONTOS, TIPO_USUARIO)
    VALUES (NULL, 'demo_sprint4_03', 'Carla Mendes', 'demo.sprint4.03@example.invalid', 'demo123', 0, 'USUARIO')
    RETURNING ID_USUARIO INTO v_usuario_3;

    INSERT INTO USUARIO (ID_USUARIO, NOME_USUARIO, NOME, EMAIL, SENHA, PONTOS, TIPO_USUARIO)
    VALUES (NULL, 'demo_sprint4_04', 'Diego Alves', 'demo.sprint4.04@example.invalid', 'demo123', 60, 'USUARIO')
    RETURNING ID_USUARIO INTO v_usuario_4;

    INSERT INTO USUARIO (ID_USUARIO, NOME_USUARIO, NOME, EMAIL, SENHA, PONTOS, TIPO_USUARIO)
    VALUES (NULL, 'demo_sprint4_05', 'Elisa Costa', 'demo.sprint4.05@example.invalid', 'demo123', 0, 'USUARIO')
    RETURNING ID_USUARIO INTO v_usuario_5;

    -- 3. MISSAO
    INSERT INTO MISSAO (ID_MISSAO, ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA)
    VALUES (NULL, v_categoria_1, 'Demo - Separar reciclaveis', 'Separar papel, plastico, metal e vidro antes do descarte.', 50, 'S')
    RETURNING ID_MISSAO INTO v_missao_1;

    INSERT INTO MISSAO (ID_MISSAO, ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA)
    VALUES (NULL, v_categoria_2, 'Demo - Fazer trajeto a pe', 'Substituir um trajeto curto de carro por uma caminhada.', 40, 'S')
    RETURNING ID_MISSAO INTO v_missao_2;

    INSERT INTO MISSAO (ID_MISSAO, ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA)
    VALUES (NULL, v_categoria_6, 'Demo - Planejar compras', 'Planejar as compras da semana para evitar desperdicio.', 80, 'S')
    RETURNING ID_MISSAO INTO v_missao_3;

    INSERT INTO MISSAO (ID_MISSAO, ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA)
    VALUES (NULL, v_categoria_3, 'Demo - Reduzir banho', 'Reduzir em dois minutos o tempo habitual de banho.', 60, 'S')
    RETURNING ID_MISSAO INTO v_missao_4;

    INSERT INTO MISSAO (ID_MISSAO, ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA)
    VALUES (NULL, v_categoria_5, 'Demo - Plantar uma muda', 'Participar de atividade local de plantio e preservacao ambiental.', 120, 'S')
    RETURNING ID_MISSAO INTO v_missao_5;

    -- 4. USUARIO_MISSAO
    INSERT INTO USUARIO_MISSAO (ID_USUARIO_MISSAO, ID_USUARIO, ID_MISSAO, STATUS, PONTOS_RECEBIDOS)
    VALUES (NULL, v_usuario_1, v_missao_1, 'CONCLUIDA', 50);

    INSERT INTO USUARIO_MISSAO (ID_USUARIO_MISSAO, ID_USUARIO, ID_MISSAO, STATUS, PONTOS_RECEBIDOS)
    VALUES (NULL, v_usuario_2, v_missao_2, 'CONCLUIDA', 40);

    INSERT INTO USUARIO_MISSAO (ID_USUARIO_MISSAO, ID_USUARIO, ID_MISSAO, STATUS, PONTOS_RECEBIDOS)
    VALUES (NULL, v_usuario_3, v_missao_3, 'EM_ANDAMENTO', 0);

    INSERT INTO USUARIO_MISSAO (ID_USUARIO_MISSAO, ID_USUARIO, ID_MISSAO, STATUS, PONTOS_RECEBIDOS)
    VALUES (NULL, v_usuario_4, v_missao_4, 'CONCLUIDA', 60);

    INSERT INTO USUARIO_MISSAO (ID_USUARIO_MISSAO, ID_USUARIO, ID_MISSAO, STATUS, PONTOS_RECEBIDOS)
    VALUES (NULL, v_usuario_5, v_missao_5, 'CANCELADA', 0);

    -- 5. RANKING
    INSERT INTO RANKING (ID_RANKING, ID_USUARIO, PONTOS, NIVEL, POSICAO)
    VALUES (NULL, v_usuario_1, 50, 1, 2);

    INSERT INTO RANKING (ID_RANKING, ID_USUARIO, PONTOS, NIVEL, POSICAO)
    VALUES (NULL, v_usuario_2, 40, 1, 3);

    INSERT INTO RANKING (ID_RANKING, ID_USUARIO, PONTOS, NIVEL, POSICAO)
    VALUES (NULL, v_usuario_3, 0, 1, 4);

    INSERT INTO RANKING (ID_RANKING, ID_USUARIO, PONTOS, NIVEL, POSICAO)
    VALUES (NULL, v_usuario_4, 60, 1, 1);

    INSERT INTO RANKING (ID_RANKING, ID_USUARIO, PONTOS, NIVEL, POSICAO)
    VALUES (NULL, v_usuario_5, 0, 1, 5);

    -- 6. PERFIL_USUARIO
    INSERT INTO PERFIL_USUARIO (ID_PERFIL, ID_USUARIO, ID_CATEGORIA, NIVEL_CONFIANCA)
    VALUES (NULL, v_usuario_1, v_categoria_1, 0.91);

    INSERT INTO PERFIL_USUARIO (ID_PERFIL, ID_USUARIO, ID_CATEGORIA, NIVEL_CONFIANCA)
    VALUES (NULL, v_usuario_2, v_categoria_2, 0.87);

    INSERT INTO PERFIL_USUARIO (ID_PERFIL, ID_USUARIO, ID_CATEGORIA, NIVEL_CONFIANCA)
    VALUES (NULL, v_usuario_3, v_categoria_3, 0.74);

    INSERT INTO PERFIL_USUARIO (ID_PERFIL, ID_USUARIO, ID_CATEGORIA, NIVEL_CONFIANCA)
    VALUES (NULL, v_usuario_4, v_categoria_4, 0.82);

    INSERT INTO PERFIL_USUARIO (ID_PERFIL, ID_USUARIO, ID_CATEGORIA, NIVEL_CONFIANCA)
    VALUES (NULL, v_usuario_5, v_categoria_5, 0.69);

    -- 7. CREDENCIAIS
    SELECT NVL(MAX(ID_FUNCIONARIO), 0) + 1
    INTO v_funcionario
    FROM CREDENCIAIS;

    IF v_funcionario + 4 > 200 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Nao ha cinco IDs livres na tabela CREDENCIAIS (limite: 200).');
    END IF;

    INSERT INTO CREDENCIAIS (ID_FUNCIONARIO, NOME, SOBRENOME, EMAIL, SENHA, CARGO)
    VALUES (v_funcionario, 'Ana', 'Demo', 'admin.demo01@example.invalid', 'demo123', 'ADMIN');

    INSERT INTO CREDENCIAIS (ID_FUNCIONARIO, NOME, SOBRENOME, EMAIL, SENHA, CARGO)
    VALUES (v_funcionario + 1, 'Bruno', 'Demo', 'admin.demo02@example.invalid', 'demo123', 'TECH');

    INSERT INTO CREDENCIAIS (ID_FUNCIONARIO, NOME, SOBRENOME, EMAIL, SENHA, CARGO)
    VALUES (v_funcionario + 2, 'Carla', 'Demo', 'admin.demo03@example.invalid', 'demo123', 'TECH');

    INSERT INTO CREDENCIAIS (ID_FUNCIONARIO, NOME, SOBRENOME, EMAIL, SENHA, CARGO)
    VALUES (v_funcionario + 3, 'Diego', 'Demo', 'admin.demo04@example.invalid', 'demo123', 'CEO');

    INSERT INTO CREDENCIAIS (ID_FUNCIONARIO, NOME, SOBRENOME, EMAIL, SENHA, CARGO)
    VALUES (v_funcionario + 4, 'Elisa', 'Demo', 'admin.demo05@example.invalid', 'demo123', 'ADMIN');

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Carga demonstrativa concluida: cinco registros nas tabelas de negocio.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
