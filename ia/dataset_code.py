"""import numpy as np"""
import pandas as pd
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler

df = pd.read_csv('./dataset/soulup_dataset.csv')

features = [
    'missoes_reciclagem',
    'missoes_mobilidade',
    'missoes_agua',
    'missoes_energia',
    'missoes_natureza',
    'missoes_consumo',
    'taxa_conclusao'
]

comportamento = df[features]

scaler = StandardScaler()
scaler_tratado = scaler.fit_transform(comportamento)

interpretador = KMeans(n_clusters=5, n_init=10, random_state=42)
df['Cluster'] = interpretador.fit_predict(scaler_tratado)

print("Dados normalizados")
print(df)

print()
print("Reconhecimento/Qtd Atribuição a cada Cluster")
print(df['Cluster'].value_counts())

print()
centroides_originais = scaler.inverse_transform(interpretador.cluster_centers_)

df_centroides = pd.DataFrame(centroides_originais)

print(df_centroides)

perfis = {
    0: "MOBILIZADOR_VERDE",
    1: "GUARDIAO_RECICLAGEM",
    2: "GUARDIAO_RECURSOS",
    3: "CONSUMO_CONSCIENTE",
    4: "EXPLORADOR_SUSTENTAVEL"
}

print()
df['Cluster'] = interpretador.fit_predict(scaler_tratado)

df['perfil'] = df['Cluster'].map(perfis)

print(df)