import pandas as pd
import random

dados = []

for usuario in range(1, 501):
    dados.append({
        "usuario_id": usuario,
        "reciclagem": random.randint(0, 15),
        "agua": random.randint(0, 15),
        "energia": random.randint(0, 15),
        "mobilidade": random.randint(0, 15),
        "natureza": random.randint(0, 15)
    })

df = pd.DataFrame(dados)

df.to_csv("dataset_usuarios.csv", index=False)