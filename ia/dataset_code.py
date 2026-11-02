import math
import os
from pathlib import Path

import joblib
import pandas as pd
from flask import Flask, jsonify, request


DIRETORIO = Path(__file__).resolve().parent
ARQUIVO_MODELOS = DIRETORIO / "modelos_soulup.joblib"

if not ARQUIVO_MODELOS.is_file():
    raise FileNotFoundError(
        "Arquivo dos modelos não encontrado. Execute o notebook "
        "modelos_soulup.ipynb antes de iniciar a API."
    )

MODELOS = joblib.load(ARQUIVO_MODELOS)
FEATURES_CLUSTER = MODELOS["features_cluster"]
FEATURES_PERFIL = MODELOS["features_perfil"]
FEATURES_REGRESSAO = MODELOS["features_regressao"]
MAPA_PERFIS_CLUSTER = MODELOS["mapa_perfis_cluster"]

app = Flask(__name__)


def validar_dados(dados, campos):
    """Valida e organiza os valores de entrada de um modelo."""
    if not isinstance(dados, dict):
        return None, "Envie um objeto JSON com os dados do usuário."

    ausentes = []
    valores = []
    for campo in campos:
        if campo not in dados:
            ausentes.append(campo)
            continue

        valor = dados[campo]
        if isinstance(valor, bool) or not isinstance(valor, (int, float)):
            return None, f"O campo '{campo}' deve ser numérico."
        try:
            valor_finito = math.isfinite(valor)
        except OverflowError:
            return None, f"O campo '{campo}' está fora do limite numérico."
        if not valor_finito:
            return None, f"O campo '{campo}' deve ser um número finito."
        if valor < 0:
            return None, f"O campo '{campo}' não pode ser negativo."
        if campo == "taxa_conclusao":
            if valor > 1:
                return None, "O campo 'taxa_conclusao' deve estar entre 0 e 1."
        elif isinstance(valor, float) and not valor.is_integer():
            return None, f"O campo '{campo}' deve ser um número inteiro."

        valores.append(valor)

    if ausentes:
        return None, "Campos obrigatórios ausentes: " + ", ".join(ausentes)

    return valores, None


def obter_json():
    """Retorna o objeto JSON enviado ou uma resposta de erro."""
    if not request.is_json:
        return None, (jsonify({"erro": "O conteúdo deve ser application/json."}), 400)

    dados = request.get_json(silent=True)
    if not isinstance(dados, dict):
        return None, (jsonify({"erro": "Envie um objeto JSON válido."}), 400)

    return dados, None


def receber_entrada(campos):
    """Valida o corpo JSON e retorna um registro com as características."""
    dados, erro_json = obter_json()
    if erro_json:
        return None, erro_json

    valores, erro_validacao = validar_dados(dados, campos)
    if erro_validacao:
        return None, (jsonify({"erro": erro_validacao}), 400)

    return pd.DataFrame([valores], columns=campos), None


@app.route("/", methods=["GET"])
def verificar_api():
    """Informa que a API e os modelos estão disponíveis."""
    return jsonify({"status": "disponível", "modelos": ["kmeans", "random_forest", "linear_regression"]})


@app.route("/predict/cluster", methods=["POST"])
def prever_cluster():
    """Agrupa o usuário e indica o perfil de referência mais frequente no grupo."""
    dados_usuario, erro = receber_entrada(FEATURES_CLUSTER)
    if erro:
        return erro

    modelo = MODELOS["modelo_cluster"]
    cluster = int(modelo.predict(MODELOS["escalonador_cluster"].transform(dados_usuario))[0])
    return jsonify(
        {
            "cluster_id": cluster,
            "perfil_recomendado": MAPA_PERFIS_CLUSTER[cluster],
            "modelo": "KMeans",
        }
    )



@app.route("/predict/clusters", methods=["POST"])
def agrupar_usuarios():
    """Agrupa vários usuários e resume o tamanho de cada grupo."""
    dados, erro_json = obter_json()
    if erro_json:
        return erro_json

    usuarios = dados.get("usuarios")
    if not isinstance(usuarios, list) or not usuarios:
        return jsonify({"erro": "Envie uma lista não vazia no campo 'usuarios'."}), 400

    registros = []
    identificadores = []
    for indice, usuario in enumerate(usuarios):
        valores, erro_validacao = validar_dados(usuario, FEATURES_CLUSTER)
        if erro_validacao:
            return jsonify(
                {"erro": f"Usuário na posição {indice}: {erro_validacao}"}
            ), 400
        registros.append(valores)
        identificadores.append(usuario.get("id_usuario", indice + 1))

    dados_usuarios = pd.DataFrame(registros, columns=FEATURES_CLUSTER)
    dados_escalonados = MODELOS["escalonador_cluster"].transform(dados_usuarios)
    grupos = MODELOS["modelo_cluster"].predict(dados_escalonados)

    quantidade_grupos = {}
    usuarios_por_grupo = {}
    for indice in range(len(grupos)):
        grupo = int(grupos[indice])
        quantidade_grupos[grupo] = quantidade_grupos.get(grupo, 0) + 1
        if grupo not in usuarios_por_grupo:
            usuarios_por_grupo[grupo] = []
        usuarios_por_grupo[grupo].append(identificadores[indice])

    resumo = []
    for grupo, quantidade in quantidade_grupos.items():
        resumo.append(
            {
                "cluster_id": grupo,
                "perfil_recomendado": MAPA_PERFIS_CLUSTER[grupo],
                "quantidade_usuarios": quantidade,
                "usuarios": usuarios_por_grupo[grupo],
            }
        )

    return jsonify(
        {
            "total_usuarios": len(usuarios),
            "grupos": resumo,
            "modelo": "KMeans",
        }
    )


@app.route("/predict/perfil", methods=["POST"])
def prever_perfil():
    """Classifica o perfil usando a floresta aleatória treinada."""
    dados_usuario, erro = receber_entrada(FEATURES_PERFIL)
    if erro:
        return erro

    classificador = MODELOS["modelo_perfil"]
    perfil = str(classificador.predict(dados_usuario)[0])
    probabilidades = classificador.predict_proba(dados_usuario)[0]
    probabilidades_por_perfil = {}
    for classe, probabilidade in zip(classificador.classes_, probabilidades):
        probabilidades_por_perfil[str(classe)] = round(float(probabilidade), 4)

    return jsonify(
        {
            "perfil_previsto": perfil,
            "probabilidades": probabilidades_por_perfil,
            "modelo": "RandomForestClassifier",
        }
    )


@app.route("/predict/pontos", methods=["POST"])
def prever_pontos():
    """Estima a pontuação com base no histórico de missões informado."""
    dados_usuario, erro = receber_entrada(FEATURES_REGRESSAO)
    if erro:
        return erro

    modelo = MODELOS["modelo_regressao"]
    pontos = float(modelo.predict(dados_usuario)[0])
    return jsonify(
        {
            "pontos_estimados": round(pontos, 2),
            "modelo": "LinearRegression",
            "aviso": "Estimativa experimental; não substitui a pontuação oficial do sistema.",
        }
    )


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=int(os.environ.get("PORT", 5000)), debug=False)
