import json
import os
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen


API_BASE_URL = os.getenv("SOULUP_API_URL", "http://localhost:8080").rstrip("/")

CATEGORIAS = {
    1: "RECICLAGEM",
    2: "MOBILIDADE",
    3: "AGUA",
    4: "ENERGIA",
    5: "NATUREZA",
    6: "CONSUMO",
}


def fazer_requisicao(caminho, metodo="GET", dados=None):
    """Envia uma requisição à API Java e interpreta a resposta JSON."""
    corpo = None
    if dados is not None:
        corpo = json.dumps(dados).encode("utf-8")

    requisicao = Request(
        API_BASE_URL + caminho,
        data=corpo,
        headers={"Content-Type": "application/json"},
        method=metodo,
    )

    resposta = None
    try:
        resposta = urlopen(requisicao)
        conteudo = resposta.read().decode("utf-8").strip()
    except HTTPError as erro:
        mensagem = erro.read().decode("utf-8").strip()
        erro.close()
        if not mensagem:
            mensagem = "Não foi possível concluir a solicitação."
        raise RuntimeError(mensagem) from erro
    except URLError as erro:
        raise RuntimeError(
            "Não foi possível conectar à API Java. Verifique se o servidor está em execução."
        ) from erro
    except UnicodeDecodeError as erro:
        raise RuntimeError("A API Java retornou uma resposta inválida.") from erro
    else:
        if not conteudo:
            return None

        try:
            return json.loads(conteudo)
        except json.JSONDecodeError as erro:
            raise RuntimeError("A API Java retornou uma resposta inválida.") from erro
    finally:
        if resposta is not None:
            resposta.close()


def cadastrar_usuario(nome_usuario, nome_completo, email, senha):
    """Envia os dados do usuário para criação."""
    dados = {
        "nomeUsuario": nome_usuario,
        "nomeCompleto": nome_completo,
        "email": email,
        "senha": senha,
    }
    return fazer_requisicao("/usuarios/cadastrar", "POST", dados)


def entrar_usuario(email, senha):
    """Envia as credenciais para autenticação."""
    dados = {"email": email, "senha": senha}
    return fazer_requisicao("/usuarios/login", "POST", dados)


def buscar_perfil(id_usuario):
    """Consulta o perfil cadastrado com o identificador informado."""
    return fazer_requisicao(f"/usuarios/{id_usuario}")


def atualizar_perfil(
    id_usuario,
    nome_usuario,
    nome_completo,
    email,
    senha,
):
    """Envia os novos dados do perfil para atualização."""
    dados = {
        "nomeUsuario": nome_usuario,
        "nomeCompleto": nome_completo,
        "email": email,
        "senha": senha,
    }
    return fazer_requisicao(f"/usuarios/atualizar/{id_usuario}", "PATCH", dados)


def remover_perfil(id_usuario, senha):
    """Solicita a exclusão do perfil após validação da senha."""
    return fazer_requisicao(
        f"/usuarios/remover/{id_usuario}",
        "DELETE",
        {"senha": senha},
    )


def listar_missoes():
    """Retorna a lista de missões cadastradas."""
    return fazer_requisicao("/missoes")


def listar_missoes_em_andamento(id_usuario):
    """Retorna a lista de missões iniciadas pelo usuário."""
    caminho = f"/missoes/usuario/{id_usuario}/em-andamento"
    return fazer_requisicao(caminho)


def listar_missoes_concluidas(id_usuario):
    """Retorna a lista de missões concluídas pelo usuário."""
    caminho = f"/missoes/usuario/{id_usuario}/concluidas"
    return fazer_requisicao(caminho)


def iniciar_missao(id_usuario, id_missao):
    """Solicita o início de uma missão para o usuário."""
    return fazer_requisicao(
        f"/missoes/{id_missao}/iniciar",
        "POST",
        id_usuario,
    )


def concluir_missao(id_usuario, id_missao):
    """Solicita a conclusão de uma missão pelo usuário."""
    caminho = f"/missoes/{id_missao}/concluir?idUsuario={id_usuario}"
    return fazer_requisicao(caminho, "PUT")


def listar_ranking_global():
    """Retorna a sequência de usuários do ranking global."""
    return fazer_requisicao("/usuarios/ranking")
