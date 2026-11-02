# SoulUp — Sprint 4 de Inteligência Artificial

O notebook `modelos_soulup.ipynb` treina e avalia três modelos: K-Means para agrupar usuários, Random Forest para classificar o perfil de referência e Regressão Linear para estimar pontos, usando `dataset/soulup_dataset.csv`. Ao executar todas as células, ele grava `modelos_soulup.joblib`, usado pela API REST em `dataset_code.py`.

## Executar

Instale `pandas`, `scikit-learn`, `joblib` e `Flask`. Execute o notebook até o fim e, na pasta `ia`, inicie:

```text
python dataset_code.py
```

A API local fica disponível em `http://127.0.0.1:5000`:

- `GET /` verifica se a API e os modelos estão disponíveis.
- `POST /predict/cluster` atribui um usuário a um grupo usando K-Means.
- `POST /predict/clusters` recebe uma lista de usuários e retorna a quantidade e os identificadores de usuários em cada grupo.
- `POST /predict/pontos` estima pontos usando Regressão Linear. É uma estimativa experimental e não altera a pontuação oficial.
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

## Agrupamento coletivo

O endpoint `POST /predict/clusters` recebe vários usuários no campo `usuarios`. Cada item deve conter as seis quantidades de missões e `taxa_conclusao`. Pode conter também `id_usuario`, usado para identificar o usuário no resultado.

Exemplo:

```json
{
  "usuarios": [
    {
      "id_usuario": 101,
      "missoes_reciclagem": 4,
      "missoes_mobilidade": 2,
      "missoes_agua": 1,
      "missoes_energia": 2,
      "missoes_natureza": 1,
      "missoes_consumo": 3,
      "taxa_conclusao": 0.8
    }
  ]
}
```

O resultado resume quantos usuários foram atribuídos a cada cluster. O K-Means atribui os registros aos grupos aprendidos no treinamento; ele não cria automaticamente novos perfis de negócio.

## Regressão Linear

O endpoint `POST /predict/pontos` recebe as seis quantidades por categoria e a taxa de conclusão. Retorna uma estimativa numérica baseada na coluna `pontos` do CSV. A pontuação real do SoulUp continua sendo definida pelo Java.

Execute todas as células do notebook para gerar `modelos_soulup.joblib` antes de iniciar a API. Use a versão de scikit-learn indicada em `ENTREGA_IA.txt`.
