from pathlib import Path

import joblib
import pandas as pd
from sklearn.cluster import KMeans
from sklearn.ensemble import RandomForestClassifier
from sklearn.linear_model import LinearRegression
from sklearn.metrics import (
    accuracy_score,
    adjusted_rand_score,
    mean_absolute_error,
    mean_squared_error,
    r2_score,
    silhouette_score,
)
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler


BASE_DIR = Path(__file__).resolve().parent
ARQUIVO_DADOS = BASE_DIR / "dataset" / "soulup_dataset.csv"
ARQUIVO_MODELOS = BASE_DIR / "modelos_soulup.joblib"

FEATURES_CATEGORIAS = [
    "missoes_reciclagem",
    "missoes_mobilidade",
    "missoes_agua",
    "missoes_energia",
    "missoes_natureza",
    "missoes_consumo",
]
FEATURES_CLUSTER = FEATURES_CATEGORIAS + ["taxa_conclusao"]
FEATURES_PERFIL = FEATURES_CATEGORIAS
FEATURES_REGRESSAO = FEATURES_CLUSTER
COLUNA_ALVO = "perfil_referencia"
COLUNA_PONTOS = "pontos"


def main():
    if not ARQUIVO_DADOS.is_file():
        raise FileNotFoundError(f"Dataset não encontrado: {ARQUIVO_DADOS}")

    df = pd.read_csv(ARQUIVO_DADOS)
    colunas = FEATURES_CLUSTER + [COLUNA_ALVO, COLUNA_PONTOS]
    ausentes = [coluna for coluna in colunas if coluna not in df.columns]
    if ausentes:
        raise ValueError("Colunas ausentes no dataset: " + ", ".join(ausentes))
    if df[colunas].isnull().any().any():
        raise ValueError("O dataset contém valores ausentes nas colunas dos modelos.")
    if len(df) < 5:
        raise ValueError("O dataset precisa conter pelo menos cinco registros.")

    escalonador_cluster = StandardScaler()
    dados_cluster = escalonador_cluster.fit_transform(df[FEATURES_CLUSTER])
    modelo_cluster = KMeans(n_clusters=5, n_init=10, random_state=42)
    grupos_cluster = modelo_cluster.fit_predict(dados_cluster)
    silhueta = silhouette_score(dados_cluster, grupos_cluster)
    ari = adjusted_rand_score(df[COLUNA_ALVO], grupos_cluster)

    comparacao = pd.crosstab(
        pd.Series(grupos_cluster, name="grupo"),
        df[COLUNA_ALVO],
    )
    mapa_perfis_cluster = {
        int(grupo): str(comparacao.loc[grupo].idxmax())
        for grupo in comparacao.index
    }

    X_perfil = df[FEATURES_PERFIL]
    y_perfil = df[COLUNA_ALVO]
    X_treino, X_teste, y_treino, y_teste = train_test_split(
        X_perfil,
        y_perfil,
        test_size=0.2,
        random_state=42,
        stratify=y_perfil,
    )
    modelo_perfil = RandomForestClassifier(
        n_estimators=300,
        class_weight="balanced",
        random_state=42,
    )
    modelo_perfil.fit(X_treino, y_treino)
    acuracia = accuracy_score(y_teste, modelo_perfil.predict(X_teste))

    X_regressao = df[FEATURES_REGRESSAO]
    y_regressao = df[COLUNA_PONTOS]
    X_reg_treino, X_reg_teste, y_reg_treino, y_reg_teste = train_test_split(
        X_regressao,
        y_regressao,
        test_size=0.2,
        random_state=42,
    )
    modelo_regressao = LinearRegression()
    modelo_regressao.fit(X_reg_treino, y_reg_treino)
    previsoes = modelo_regressao.predict(X_reg_teste)

    pacote = {
        "modelo_cluster": modelo_cluster,
        "escalonador_cluster": escalonador_cluster,
        "mapa_perfis_cluster": mapa_perfis_cluster,
        "features_cluster": FEATURES_CLUSTER,
        "modelo_perfil": modelo_perfil,
        "modelo_regressao": modelo_regressao,
        "features_perfil": FEATURES_PERFIL,
        "features_regressao": FEATURES_REGRESSAO,
        "classes_perfil": [str(classe) for classe in modelo_perfil.classes_],
        "metrica_silhueta": float(silhueta),
        "metrica_ari": float(ari),
        "metrica_acuracia_teste": float(acuracia),
        "metrica_mae_regressao": float(mean_absolute_error(y_reg_teste, previsoes)),
        "metrica_rmse_regressao": float(mean_squared_error(y_reg_teste, previsoes) ** 0.5),
        "metrica_r2_regressao": float(r2_score(y_reg_teste, previsoes)),
    }
    joblib.dump(pacote, ARQUIVO_MODELOS)
    print(f"Modelos treinados e salvos em: {ARQUIVO_MODELOS}")
    print(f"Silhueta: {silhueta:.3f} | ARI: {ari:.3f} | Acurácia RF: {acuracia:.3f}")


if __name__ == "__main__":
    main()
