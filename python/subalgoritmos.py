def adicionar_usuario(funcionamento_usuario: dict, nome_usuario: str, nome_completo: str, email: str, senha_usuario: str) -> str:

    if not nome_usuario.strip():
        return "Erro! Seu usuário não pode estar vazio!"

    if not nome_completo.strip():
        return "Erro! Você não pode deixar seu nome vazio"

    if not email.strip():
        return "Erro! Seu email não pode estar vazio"

    if not senha_usuario.strip():
        return "Erro! Você não pode deixar sua senha em branco"

    try:
        funcionamento_usuario[nome_usuario] = {
            "nome_completo": nome_completo,
            "email": email,
            "senha": senha_usuario,
            "pontos": 0,
            "missoes": {}
        }

    except Exception as erro:
        return f"Erro ao criar usuário: {erro}"

    else:
        return "Usuário criado com sucesso!"

    finally:
        print(f"[LOG] Tentativa de cadastro do usuário '{nome_usuario}' processada.")


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

        if not nome_completo.strip():
            return "Erro! Você não pode deixar seu nome vazio"

        if not email.strip():
            return "Erro! Seu email não pode estar vazio"

        if not senha_usuario.strip():
            return "Erro! Você não pode deixar sua senha em branco"

        try:
            funcionamento_usuario[nome_usuario]["nome_completo"] = nome_completo
            funcionamento_usuario[nome_usuario]["email"] = email
            funcionamento_usuario[nome_usuario]["senha"] = senha_usuario

        except Exception as erro:
            return f"Erro ao atualizar usuário: {erro}"

        else:
            return (
                "\nUsuário atualizado com sucesso!"
                f"\nNome de Usuário.....: {nome_usuario}"
                f"\nNome completo......: {nome_completo}"
                f"\nEmail..............: {email}"
                f"\nSenha..............: {senha_usuario}"
            )

        finally:
            print(f"[LOG] Tentativa de atualização do usuário '{nome_usuario}' processada.")

    return "Este usuário não existe."


def remover_usuario(funcionamento_usuario: dict, nome_usuario: str) -> str:

    if nome_usuario in funcionamento_usuario:

        try:
            funcionamento_usuario.pop(nome_usuario)

        except Exception as erro:
            return f"Erro ao remover usuário: {erro}"

        else:
            return "Usuário removido com sucesso!"

        finally:
            print(f"[LOG] Tentativa de remoção do usuário '{nome_usuario}' processada.")

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

        try:
            usuario["missoes"][id_missao] = {
                "status": "EM_ANDAMENTO"
            }

        except Exception as erro:
            return f"Erro ao iniciar missão: {erro}"

        else:
            return "Missão iniciada com sucesso!"

        finally:
            print(f"[LOG] Início de missão {id_missao} processado para '{nome_usuario}'.")

    if acao == "CONCLUIR":

        if id_missao not in usuario["missoes"]:
            return "Esta missão ainda não foi iniciada."

        if usuario["missoes"][id_missao]["status"] != "EM_ANDAMENTO":
            return "Esta missão não está em andamento."

        try:
            usuario["missoes"][id_missao]["status"] = "CONCLUIDA"
            usuario["pontos"] += missao["pontos"]

        except Exception as erro:
            return f"Erro ao concluir missão: {erro}"

        else:
            return (
                "Missão concluída com sucesso!"
                f"\nVocê recebeu {missao['pontos']} pontos."
                f"\nTotal de pontos: {usuario['pontos']}"
            )

        finally:
            print(f"[LOG] Conclusão de missão {id_missao} processada para '{nome_usuario}'.")

    return "Ação inválida."


def interagir_postagens():
    return "Sistema de postagens ainda não implementado."