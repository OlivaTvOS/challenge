def adicionar_usuario(funcionamento_usuario: dict, nome_usuario: str, nome_completo: str, email: str, senha_usuario: str) -> str:

    funcionamento_usuario[nome_usuario] = {
        "nome_completo": nome_completo,
        "email": email,
        "senha": senha_usuario,
        "pontos": 0,
        "missoes": {}
    }

    return "Usuário criado com sucesso!"


def consultar_usuario(funcionamento_usuario: dict, nome_usuario: str) -> str:

    if nome_usuario in funcionamento_usuario:

        dados = funcionamento_usuario[nome_usuario]

        return (
            f"\nNome de Usuário.....: {nome_usuario}"
            f"\nNome completo......: {dados['nome_completo']}"
            f"\nEmail..............: {dados['email']}"
            f"\nSenha..............: {dados['senha']}"
            f"\nPontos.............: {dados['pontos']}"
        )

    return "Este usuário não existe."


def atualizar_usuario(funcionamento_usuario: dict, nome_usuario: str, nome_completo: str, email: str, senha_usuario: str) -> str:

    if nome_usuario in funcionamento_usuario:

        funcionamento_usuario[nome_usuario]["nome_completo"] = nome_completo
        funcionamento_usuario[nome_usuario]["email"] = email
        funcionamento_usuario[nome_usuario]["senha"] = senha_usuario

        return (
            "\nUsuário atualizado com sucesso!"
            f"\nNome de Usuário.....: {nome_usuario}"
            f"\nNome completo......: {nome_completo}"
            f"\nEmail..............: {email}"
            f"\nSenha..............: {senha_usuario}"
        )

    return "Este usuário não existe."


def remover_usuario(funcionamento_usuario: dict, nome_usuario: str) -> str:

    if nome_usuario in funcionamento_usuario:

        funcionamento_usuario.pop(nome_usuario)

        return "Usuário removido com sucesso!"

    return "Usuário não foi removido."


def listar_missoes(missoes_sistema: dict) -> str:

    resultado = ""

    for id_missao, missao in missoes_sistema.items():

        resultado += (
            f"\nID: {id_missao}"
            f"\nTítulo: {missao['titulo']}"
            f"\nCategoria: {missao['categoria']}"
            f"\nPontos: {missao['pontos']}"
            "\n------------------------"
        )

    return resultado


def exibir_missao(missoes_sistema: dict, id_missao: int) -> str:

    missao = missoes_sistema[id_missao]

    return (
        "\n===== MISSÃO SELECIONADA ====="
        f"\nID: {id_missao}"
        f"\nTítulo: {missao['titulo']}"
        f"\nCategoria: {missao['categoria']}"
        f"\nDescrição: {missao['descricao']}"
        f"\nPontos: {missao['pontos']}"
    )


def listar_missoes_em_andamento(usuario: dict, missoes_sistema: dict) -> str:

    resultado = ""

    for id_missao, dados in usuario["missoes"].items():

        if dados["status"] == "EM_ANDAMENTO":

            missao = missoes_sistema[id_missao]

            resultado += (
                f"\nID: {id_missao}"
                f"\nTítulo: {missao['titulo']}"
                f"\nCategoria: {missao['categoria']}"
                f"\nPontos: {missao['pontos']}"
                "\nStatus: EM_ANDAMENTO"
                "\n------------------------"
            )

    return resultado


def realizar_missao(usuarios_sistema: dict, missoes_sistema: dict, nome_usuario: str, id_missao: int, acao: str) -> str:

    if nome_usuario not in usuarios_sistema:
        return "Usuário inexistente."

    if id_missao not in missoes_sistema:
        return "Missão inexistente."

    usuario = usuarios_sistema[nome_usuario]
    missao = missoes_sistema[id_missao]

    if acao == "INICIAR":

        if (
            id_missao in usuario["missoes"]
            and usuario["missoes"][id_missao]["status"] == "EM_ANDAMENTO"
        ):
            return "Esta missão já está em andamento."

        usuario["missoes"][id_missao] = {
            "status": "EM_ANDAMENTO"
        }

        return "Missão iniciada com sucesso!"

    if acao == "CONCLUIR":

        if id_missao not in usuario["missoes"]:
            return "Esta missão ainda não foi iniciada."

        if usuario["missoes"][id_missao]["status"] != "EM_ANDAMENTO":
            return "Esta missão não está em andamento."

        usuario["missoes"][id_missao]["status"] = "CONCLUIDA"
        usuario["pontos"] += missao["pontos"]

        return (
            "Missão concluída com sucesso!"
            f"\nVocê recebeu {missao['pontos']} pontos."
            f"\nTotal de pontos: {usuario['pontos']}"
        )

    return "Ação inválida."


def interagir_postagens():
    return "Sistema de postagens ainda não implementado."