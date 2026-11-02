import os

import oracledb


def conectar_banco():
    """Abre uma conexão com o Oracle usando variáveis de ambiente."""
    usuario = os.getenv("SOULUP_ORACLE_USER")
    senha = os.getenv("SOULUP_ORACLE_PASSWORD")
    dsn = os.getenv("SOULUP_ORACLE_DSN")

    if not usuario or not senha or not dsn:
        raise RuntimeError(
            "Configure SOULUP_ORACLE_USER, SOULUP_ORACLE_PASSWORD "
            "e SOULUP_ORACLE_DSN antes de consultar o banco."
        )

    try:
        return oracledb.connect(user=usuario, password=senha, dsn=dsn)
    except oracledb.DatabaseError as erro:
        raise RuntimeError(f"Não foi possível conectar ao Oracle: {erro}") from erro


def consultar_banco(comando_sql):
    """Executa uma consulta SQL e devolve os registros em uma lista."""
    conexao = conectar_banco()
    try:
        cursor = conexao.cursor()
        cursor.execute(comando_sql)

        nomes_colunas = []
        for coluna in cursor.description:
            nomes_colunas.append(coluna[0].lower())

        registros = []
        for linha in cursor:
            registro = {}
            for indice in range(len(nomes_colunas)):
                valor = linha[indice]
                if valor is not None and hasattr(valor, "isoformat"):
                    valor = valor.isoformat()
                registro[nomes_colunas[indice]] = valor
            registros.append(registro)

        cursor.close()
        return registros
    except oracledb.DatabaseError as erro:
        raise RuntimeError(f"Erro ao consultar o Oracle: {erro}") from erro
    finally:
        conexao.close()


def consultar_ranking():
    """Consulta a posição e a pontuação dos usuários."""
    comando_sql = """
        SELECT R.POSICAO, U.ID_USUARIO, U.NOME_USUARIO, R.PONTOS, R.NIVEL
        FROM RANKING R
        INNER JOIN USUARIO U ON U.ID_USUARIO = R.ID_USUARIO
        WHERE U.TIPO_USUARIO = 'USUARIO'
        ORDER BY R.POSICAO NULLS LAST, R.PONTOS DESC
    """
    return consultar_banco(comando_sql)


def consultar_historico_missoes():
    """Consulta o histórico de missões e suas categorias."""
    comando_sql = """
        SELECT U.NOME_USUARIO, M.ID_MISSAO, M.TITULO, C.NOME AS CATEGORIA,
               UM.STATUS, UM.DATA_INICIO, UM.DATA_CONCLUSAO, UM.PONTOS_RECEBIDOS
        FROM USUARIO_MISSAO UM
        INNER JOIN USUARIO U ON U.ID_USUARIO = UM.ID_USUARIO
        INNER JOIN MISSAO M ON M.ID_MISSAO = UM.ID_MISSAO
        INNER JOIN CATEGORIA C ON C.ID_CATEGORIA = M.ID_CATEGORIA
        WHERE UM.STATUS IN ('EM_ANDAMENTO', 'CONCLUIDA')
        ORDER BY UM.DATA_INICIO DESC
    """
    return consultar_banco(comando_sql)


def consultar_publicacoes():
    """Consulta publicações e suas interações."""
    comando_sql = """
        SELECT P.ID_PUBLICACAO, U.NOME_USUARIO, P.CONTEUDO,
               P.STATUS_VERIFICACAO, I.TIPO_INTERACAO,
               I.CONTEUDO AS TEXTO_INTERACAO, I.DATA_INTERACAO
        FROM PUBLICACAO P
        INNER JOIN USUARIO U ON U.ID_USUARIO = P.ID_USUARIO
        LEFT JOIN INTERACAO I ON I.ID_PUBLICACAO = P.ID_PUBLICACAO
        WHERE P.STATUS_VERIFICACAO <> 'REJEITADA'
        ORDER BY P.DATA_PUBLICACAO DESC, P.ID_PUBLICACAO
    """
    return consultar_banco(comando_sql)


def consultar_estatisticas_categorias():
    """Consulta quantidade de missões e recompensas por categoria."""
    comando_sql = """
        SELECT C.NOME AS CATEGORIA, COUNT(M.ID_MISSAO) AS QUANTIDADE_MISSOES,
               SUM(M.PONTOS_RECOMPENSA) AS SOMA_PONTOS,
               AVG(M.PONTOS_RECOMPENSA) AS MEDIA_PONTOS,
               MIN(M.PONTOS_RECOMPENSA) AS MENOR_RECOMPENSA,
               MAX(M.PONTOS_RECOMPENSA) AS MAIOR_RECOMPENSA
        FROM CATEGORIA C
        LEFT JOIN MISSAO M
            ON M.ID_CATEGORIA = C.ID_CATEGORIA AND M.ATIVA = 'S'
        GROUP BY C.ID_CATEGORIA, C.NOME
        ORDER BY QUANTIDADE_MISSOES DESC, C.NOME
    """
    return consultar_banco(comando_sql)


def consultar_perfil_comportamental():
    """Consulta as categorias e os níveis de confiança dos perfis."""
    comando_sql = """
        SELECT U.NOME_USUARIO, C.NOME AS CATEGORIA,
               PU.NIVEL_CONFIANCA, PU.DATA_CLASSIFICACAO
        FROM PERFIL_USUARIO PU
        INNER JOIN USUARIO U ON U.ID_USUARIO = PU.ID_USUARIO
        INNER JOIN CATEGORIA C ON C.ID_CATEGORIA = PU.ID_CATEGORIA
        WHERE PU.NIVEL_CONFIANCA IS NOT NULL
        ORDER BY U.NOME_USUARIO, PU.NIVEL_CONFIANCA DESC
    """
    return consultar_banco(comando_sql)


def listar_consultas():
    """Executa as cinco consultas SQL documentadas para a Sprint 4."""
    consultas = []
    consultas.append(("Ranking geral", consultar_ranking))
    consultas.append(("Histórico de missões", consultar_historico_missoes))
    consultas.append(("Publicações e interações", consultar_publicacoes))
    consultas.append(("Estatísticas por categoria", consultar_estatisticas_categorias))
    consultas.append(("Perfil comportamental", consultar_perfil_comportamental))

    resultados = {}
    for titulo, funcao in consultas:
        resultados[titulo] = funcao()
    return resultados
