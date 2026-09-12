import random
import string


PERMISSOES_POR_CARGO = {
    "ADMIN": ["CONSULTAR_USUARIOS", "EDITAR_DADOS_BASICOS", "EXCLUIR_USUARIO", "CONSULTAR_FUNCIONARIOS", "CONSULTAR_RESUMO"],
    "TECH": ["CONSULTAR_USUARIOS", "EDITAR_DADOS_BASICOS", "CONSULTAR_MISSOES_USUARIO"],
    "CEO": ["CONSULTAR_USUARIOS", "CONSULTAR_RESUMO", "CONSULTAR_RANKING"],
}


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


def autenticar_usuario(usuarios_sistema: dict, email: str, senha: str):
    """Localiza um usuário pelo e-mail e pela senha informados."""
    for nome_usuario, dados in usuarios_sistema.items():
        if dados["email"].lower() == email.lower() and dados["senha"] == senha:
            return nome_usuario
    return None


def gerar_id_funcionario(funcionarios_sistema: dict):
    """Gera um ID aleatório disponível entre 1 e 200."""
    ids_disponiveis = list(range(1, 201))
    random.shuffle(ids_disponiveis)
    for id_funcionario in ids_disponiveis:
        if id_funcionario not in funcionarios_sistema:
            return id_funcionario
    return None


def gerar_email_funcionario(funcionarios_sistema: dict, nome: str, sobrenome: str) -> str:
    """Gera um e-mail @kairos.com que ainda não esteja em uso."""
    inicio_email = f"{nome.strip().lower()}.{sobrenome.strip().lower()}".replace(" ", "")
    emails_existentes = [dados["email"].lower() for dados in funcionarios_sistema.values()]
    email = f"{inicio_email}@kairos.com"
    numero = 1
    while email.lower() in emails_existentes:
        email = f"{inicio_email}{numero}@kairos.com"
        numero += 1
    return email


def gerar_senha_funcionario() -> str:
    """Gera uma senha de 10 caracteres com maiúscula, minúscula e número."""
    caracteres = [random.choice(string.ascii_uppercase), random.choice(string.ascii_lowercase), random.choice(string.digits)]
    opcoes = string.ascii_letters + string.digits
    for _ in range(7):
        caracteres.append(random.choice(opcoes))
    random.shuffle(caracteres)
    return "".join(caracteres)


def adicionar_funcionario(funcionarios_sistema: dict, nome: str, sobrenome: str, cargo: str):
    """Valida, gera as credenciais e armazena um funcionário em memória."""
    if not nome.strip():
        return {"sucesso": False, "mensagem": "O nome não pode estar vazio."}
    if not sobrenome.strip():
        return {"sucesso": False, "mensagem": "O sobrenome não pode estar vazio."}
    if cargo not in PERMISSOES_POR_CARGO:
        return {"sucesso": False, "mensagem": "Cargo inválido."}
    id_funcionario = gerar_id_funcionario(funcionarios_sistema)
    if id_funcionario is None:
        return {"sucesso": False, "mensagem": "Todos os IDs de funcionário estão ocupados."}
    email = gerar_email_funcionario(funcionarios_sistema, nome, sobrenome)
    senha = gerar_senha_funcionario()
    try:
        funcionarios_sistema[id_funcionario] = {"nome": nome.strip(), "sobrenome": sobrenome.strip(), "email": email, "senha": senha, "cargo": cargo}
    except Exception as erro:
        return {"sucesso": False, "mensagem": f"Erro ao criar funcionário: {erro}"}
    else:
        return {"sucesso": True, "mensagem": "Funcionário criado com sucesso.", "id": id_funcionario, "email": email, "senha": senha}
    finally:
        print("[LOG] Tentativa de cadastro de funcionário processada.")


def autenticar_funcionario(funcionarios_sistema: dict, id_funcionario: int, email: str, senha: str):
    """Autentica ID, e-mail e senha contra o mesmo registro."""
    funcionario = funcionarios_sistema.get(id_funcionario)
    if funcionario and funcionario["email"].lower() == email.lower() and funcionario["senha"] == senha:
        return {"id": id_funcionario, **funcionario}
    return None


def tem_permissao(funcionario: dict, permissao: str) -> bool:
    """Confirma novamente a permissão antes de uma operação."""
    return bool(funcionario) and permissao in PERMISSOES_POR_CARGO.get(funcionario.get("cargo"), [])


def listar_usuarios_funcionario(usuarios_sistema: dict, funcionario: dict) -> str:
    """Lista usuários sem revelar senhas, após verificar autorização."""
    if not tem_permissao(funcionario, "CONSULTAR_USUARIOS"):
        return "Acesso negado."
    if not usuarios_sistema:
        return "Nenhum usuário cadastrado."
    resultado = ""
    for nome_usuario, dados in usuarios_sistema.items():
        resultado += f"\nUsuário: {nome_usuario}\nNome: {dados['nome_completo']}\nE-mail: {dados['email']}\nPontos: {dados['pontos']}\n------------------------"
    return resultado


def atualizar_usuario_funcionario(usuarios_sistema: dict, funcionario: dict, nome_usuario: str, nome_completo: str, email: str) -> str:
    """Permite ao ADMIN ou TECH alterar somente nome completo e e-mail."""
    if not tem_permissao(funcionario, "EDITAR_DADOS_BASICOS"):
        return "Acesso negado."
    if nome_usuario not in usuarios_sistema:
        return "Usuário inexistente."
    if not nome_completo.strip() or not email.strip():
        return "Nome completo e e-mail são obrigatórios."
    for usuario_cadastrado, dados in usuarios_sistema.items():
        if usuario_cadastrado != nome_usuario and dados["email"].lower() == email.lower():
            return "Este e-mail já está cadastrado."
    try:
        usuarios_sistema[nome_usuario]["nome_completo"] = nome_completo
        usuarios_sistema[nome_usuario]["email"] = email
    except Exception as erro:
        return f"Erro ao atualizar usuário: {erro}"
    else:
        return "Dados básicos atualizados com sucesso."
    finally:
        print(f"[LOG] Alteração administrativa de '{nome_usuario}' processada.")


def remover_usuario_funcionario(usuarios_sistema: dict, funcionario: dict, nome_usuario: str) -> str:
    """Remove um usuário somente quando o funcionário é autorizado."""
    if not tem_permissao(funcionario, "EXCLUIR_USUARIO"):
        return "Acesso negado."
    if nome_usuario not in usuarios_sistema:
        return "Usuário inexistente."
    try:
        usuarios_sistema.pop(nome_usuario)
    except Exception as erro:
        return f"Erro ao remover usuário: {erro}"
    else:
        return "Usuário removido com sucesso."
    finally:
        print(f"[LOG] Remoção administrativa de '{nome_usuario}' processada.")


def listar_funcionarios(funcionarios_sistema: dict, funcionario: dict) -> str:
    """Lista funcionários para ADMIN sem revelar suas senhas."""
    if not tem_permissao(funcionario, "CONSULTAR_FUNCIONARIOS"):
        return "Acesso negado."
    if not funcionarios_sistema:
        return "Nenhum funcionário cadastrado."
    resultado = ""
    for id_funcionario, dados in funcionarios_sistema.items():
        resultado += f"\nID: {id_funcionario}\nNome: {dados['nome']} {dados['sobrenome']}\nE-mail: {dados['email']}\nCargo: {dados['cargo']}\n------------------------"
    return resultado


def consultar_missoes_usuario(usuarios_sistema: dict, funcionario: dict, nome_usuario: str) -> str:
    """Mostra ao TECH os IDs e estados das missões de um usuário."""
    if not tem_permissao(funcionario, "CONSULTAR_MISSOES_USUARIO"):
        return "Acesso negado."
    if nome_usuario not in usuarios_sistema:
        return "Usuário inexistente."
    missoes = usuarios_sistema[nome_usuario]["missoes"]
    if not missoes:
        return "O usuário ainda não iniciou missões."
    return "".join(f"\nMissão {id_missao}: {dados['status']}" for id_missao, dados in missoes.items())


def listar_ranking(usuarios_sistema: dict, funcionario: dict) -> str:
    """Monta o ranking de pontos para cargos autorizados."""
    if not tem_permissao(funcionario, "CONSULTAR_RANKING"):
        return "Acesso negado."
    if not usuarios_sistema:
        return "Nenhum usuário cadastrado."
    ordenados = sorted(usuarios_sistema.items(), key=lambda item: item[1]["pontos"], reverse=True)
    return "".join(f"\n{posicao}º - {nome}: {dados['pontos']} pontos" for posicao, (nome, dados) in enumerate(ordenados, start=1))


def obter_resumo(usuarios_sistema: dict, missoes_sistema: dict, funcionario: dict) -> str:
    """Calcula um resumo simples usando somente dados existentes."""
    if not tem_permissao(funcionario, "CONSULTAR_RESUMO"):
        return "Acesso negado."
    estados = [missao["status"] for usuario in usuarios_sistema.values() for missao in usuario["missoes"].values()]
    return f"\nUsuários cadastrados: {len(usuarios_sistema)}\nMissões disponíveis: {len(missoes_sistema)}\nMissões em andamento: {estados.count('EM_ANDAMENTO')}\nMissões concluídas: {estados.count('CONCLUIDA')}"
