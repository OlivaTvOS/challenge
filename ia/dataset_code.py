import joblib
import numpy as np
import pandas as pd
from flask import Flask, jsonify, request
from sklearn.cluster import KMeans
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

# ==========================================
# 1. CARREGAMENTO E PREPARAÇÃO DOS DADOS
# ==========================================
# Carrega a base de dados consolidada do SoulUp
df = pd.read_csv("./dataset/soulup_dataset.csv")

# ==========================================
# 2. MODELO 1: CLUSTERING (Agrupamento de Perfis)
# ==========================================
# Seleciona características comportamentais para identificar estereótipos de missões

features_cluster = [
    "missoes_reciclagem",
    "missoes_mobilidade",
    "missoes_agua",
    "missoes_energia",
    "missoes_natureza",
    "missoes_consumo",
    "taxa_conclusao",
]

scaler = StandardScaler()
scaled_data = scaler.fit_transform(df[features_cluster])

# Aplicação do K-Means para 5 perfis distintos de engajamento sustentável
kmeans = KMeans(n_clusters=5, n_init=10, random_state=42)
df["Cluster"] = kmeans.fit_predict(scaled_data)

perfis_map = {
    0: "MOBILIZADOR_VERDE",
    1: "GUARDIAO_RECICLAGEM",
    2: "GUARDIAO_RECURSOS",
    3: "CONSUMO_CONSCIENTE",
    4: "EXPLORADOR_SUSTENTAVEL",
}
df["perfil"] = df["Cluster"].map(perfis_map)

# ==========================================
# 3. MODELO 2: CLASSIFICAÇÃO (Aprovação de Missões)
# ==========================================
# Criação do rótulo supervisionado com base em critérios de consistência e engajamento
df["status_aprovacao"] = (
    (df["taxa_conclusao"] >= 0.75) & (df["interacoes"] >= 30)
).astype(int)

features_clf = [
    "missoes_concluidas",
    "missoes_abandonadas",
    "taxa_conclusao",
    "interacoes",
    "pontos",
]
X = df[features_clf]
y = df["status_aprovacao"]

# Divisão de treino e teste para validação do classificador
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)
clf_model = RandomForestClassifier(random_state=42)
clf_model.fit(X_train, y_train)

# ==========================================
# 4. SERIALIZAÇÃO DOS MODELOS TREINADOS
# ==========================================
joblib.dump(kmeans, "kmeans_model.joblib")
joblib.dump(scaler, "scaler_model.joblib")
joblib.dump(clf_model, "classifier_model.joblib")

# ==========================================
# 5. API REST (FLASK) - COMUNICAÇÃO COM O PRODUTO
# ==========================================
app = Flask(__name__)


@app.route("/predict/cluster", methods=["POST"])
def predict_cluster():
  """Endpoint para identificar o perfil/estereótipo atual do usuário."""
  data = request.get_json()
  values = [
      data["missoes_reciclagem"],
      data["missoes_mobilidade"],
      data["missoes_agua"],
      data["missoes_energia"],
      data["missoes_natureza"],
      data["missoes_consumo"],
      data["taxa_conclusao"],
  ]
  scaled = scaler.transform([values])
  cluster_id = int(kmeans.predict(scaled)[0])
  return jsonify({
      "cluster_id": cluster_id,
      "perfil_recomendado": perfis_map[cluster_id],
  })


@app.route("/predict/aprovacao", methods=["POST"])
def predict_aprovacao():
  """Endpoint para verificar a aprovação automática de missões do usuário."""
  data = request.get_json()
  values = [
      data["missoes_concluidas"],
      data["missoes_abandonadas"],
      data["taxa_conclusao"],
      data["interacoes"],
      data["pontos"],
  ]
  pred = int(clf_model.predict([values])[0])
  status = "Aprovado" if pred == 1 else "Revisar/Reprovado"
  return jsonify({"predicao": pred, "status_missao": status})


if __name__ == "__main__":
  app.run(debug=True, port=5000)