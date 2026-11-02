import json
from urllib.error import HTTPError, URLError
from urllib.parse import urlencode
from urllib.request import urlopen


API_BASE_URL = "https://api.open-meteo.com/v1/forecast"


def consultar_clima(latitude, longitude):
    """Consulta temperatura e precipitação na API pública Open-Meteo."""
    parametros = {
        "latitude": latitude,
        "longitude": longitude,
        "current": "temperature_2m,precipitation",
        "timezone": "auto",
    }
    endereco = f"{API_BASE_URL}?{urlencode(parametros)}"

    try:
        with urlopen(endereco, timeout=10) as resposta:
            dados = json.loads(resposta.read().decode("utf-8"))
    except HTTPError as erro:
        raise RuntimeError(
            "A API pública não conseguiu consultar o clima."
        ) from erro
    except URLError as erro:
        raise RuntimeError(
            "Não foi possível conectar à API pública de clima."
        ) from erro
    except (json.JSONDecodeError, UnicodeDecodeError) as erro:
        raise RuntimeError(
            "A API pública retornou uma resposta inválida."
        ) from erro

    if dados is None or "current" not in dados:
        raise RuntimeError(
            "A API pública não retornou os dados atuais do clima."
        )

    clima_atual = dados["current"]
    if clima_atual is None:
        raise RuntimeError(
            "A API pública não retornou os dados atuais do clima."
        )
    if "temperature_2m" not in clima_atual or "precipitation" not in clima_atual:
        raise RuntimeError(
            "A API pública não retornou os dados atuais do clima."
        )

    return clima_atual
