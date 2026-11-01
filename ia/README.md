# SoulUp — Sprint 4 de Inteligência Artificial

O notebook `modelos_soulup.ipynb` treina e avalia K-Means para agrupar usuários e Random Forest para classificar o perfil de referência, usando `dataset/soulup_dataset.csv`. Ao executar todas as células, ele grava `modelos_soulup.joblib`, usado pela API REST em `dataset_code.py`.

## Executar

Instale `pandas`, `scikit-learn`, `joblib` e `Flask`. Execute o notebook até o fim e, na pasta `ia`, inicie:

```text
python dataset_code.py
```

A API local fica disponível em `http://127.0.0.1:5000`:

- `GET /` verifica se a API e os modelos estão disponíveis.
- `POST /predict/cluster` agrupa o usuário por suas missões e taxa de conclusão usando K-Means.
- `POST /predict/perfil` classifica o perfil com a Random Forest.

O endpoint de agrupamento recebe as seis quantidades de missões e `taxa_conclusao`. O endpoint de classificação recebe as seis quantidades de missões.

Exemplo de corpo JSON para classificação:

```json
{
  "missoes_reciclagem": 9,
  "missoes_mobilidade": 2,
  "missoes_agua": 2,
  "missoes_energia": 3,
  "missoes_natureza": 3,
  "missoes_consumo": 3
}
```