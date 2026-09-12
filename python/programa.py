import os

import subalgoritmos
from missoes import missoes_sistema


usuarios_sistema = {}
funcionarios_sistema = {}
sessao_usuario = None
sessao_funcionario = None


def ler_numero(mensagem: str):
    """Lê um número inteiro e informa entradas inválidas."""
    try:
        return int(input(mensagem))
    except ValueError:
        print("Digite apenas números.")
        return None


def cadastrar_usuario():
    nome_usuario = input("Digite seu nome de usuário: ").lower()
    nome_completo = input("Digite seu nome completo: ")
    email = input("Digite seu e-mail: ")
    senha = input("Digite sua senha: ")
    if nome_usuario in usuarios_sistema:
        print("Erro! Este nome de usuário está em uso.")
    elif any(dados["email"].lower() == email.lower() for dados in usuarios_sistema.values()):
        print("Erro! Este e-mail já está cadastrado.")
    else:
        print(subalgoritmos.adicionar_usuario(usuarios_sistema, nome_usuario, nome_completo, email, senha))


def cadastrar_funcionario():
    if sessao_usuario or sessao_funcionario:
        print("Faça logout antes de cadastrar um funcionário.")
        return
    # Esta opção existe somente para demonstração acadêmica.
    # Em um sistema real, o cadastro administrativo não seria público.
    print("\n=========================================")
    print("MODO DE DEMONSTRAÇÃO ACADÊMICA")
    print("Esta opção demonstra a criação de uma")
    print("conta de funcionário. Em um sistema real,")
    print("ela não estaria disponível publicamente.")
    print("=========================================")
    nome = input("Nome: ")
    sobrenome = input("Último sobrenome: ")
    print("1 - ADMIN\n2 - TECH\n3 - CEO")
    cargo = {1: "ADMIN", 2: "TECH", 3: "CEO"}.get(ler_numero("Cargo: "))
    resultado = subalgoritmos.adicionar_funcionario(funcionarios_sistema, nome, sobrenome, cargo)
    print(resultado["mensagem"])
    if resultado["sucesso"]:
        print(f"\nID: {resultado['id']}\nE-mail: {resultado['email']}\nSenha: {resultado['senha']}")


def entrar_usuario():
    global sessao_usuario
    if sessao_funcionario:
        print("Um funcionário está logado. Faça logout antes de entrar como usuário.")
        return
    nome = subalgoritmos.autenticar_usuario(usuarios_sistema, input("E-mail: "), input("Senha: "))
    if nome is None:
        print("Credenciais inválidas.")
        return
    sessao_usuario = nome
    print("Login realizado com sucesso.")
    menu_usuario()


def entrar_funcionario():
    global sessao_funcionario
    if sessao_usuario:
        print("Um usuário está logado. Faça logout antes de entrar como funcionário.")
        return
    id_funcionario = ler_numero("ID: ")
    if id_funcionario is None:
        return
    funcionario = subalgoritmos.autenticar_funcionario(funcionarios_sistema, id_funcionario, input("E-mail: "), input("Senha: "))
    if funcionario is None:
        print("Credenciais inválidas.")
        return
    sessao_funcionario = funcionario
    print("Login de funcionário realizado com sucesso.")
    menu_funcionario()


def realizar_missao_usuario():
    usuario = usuarios_sistema[sessao_usuario]
    em_andamento = subalgoritmos.listar_missoes_em_andamento(usuario, missoes_sistema)
    if em_andamento:
        print("\n===== MISSÕES EM ANDAMENTO =====")
        print(em_andamento)
    print("\n===== MISSÕES DISPONÍVEIS =====")
    print(subalgoritmos.listar_missoes(missoes_sistema))
    id_missao = ler_numero("Digite o ID da missão: ")
    if id_missao is None or id_missao not in missoes_sistema:
        print("Missão inexistente.")
        return
    print(subalgoritmos.exibir_missao(missoes_sistema, id_missao))
    acao = input("Digite I para iniciar ou C para concluir: ").upper()
    if acao in ("I", "C"):
        comando = "INICIAR" if acao == "I" else "CONCLUIR"
        print(subalgoritmos.realizar_missao(usuarios_sistema, missoes_sistema, sessao_usuario, id_missao, comando))
    else:
        print("Ação inválida.")


def menu_usuario():
    global sessao_usuario
    while sessao_usuario:
        print("\n===== MENU DO USUÁRIO =====")
        print("0 - Logout\n1 - Ver status do perfil\n2 - Atualizar dados do perfil\n3 - Excluir perfil\n4 - Realizar missão\n5 - Ver postagens")
        opcao = ler_numero("Escolha: ")
        if opcao is None:
            continue
        match opcao:
            case 0:
                sessao_usuario = None
                print("Logout realizado com sucesso.")
            case 1:
                print(subalgoritmos.consultar_usuario(usuarios_sistema, sessao_usuario))
            case 2:
                nome = input("Novo nome completo: ")
                email = input("Novo e-mail: ")
                senha = input("Nova senha: ")
                duplicado = any(usuario != sessao_usuario and dados["email"].lower() == email.lower() for usuario, dados in usuarios_sistema.items())
                print("Este e-mail já está cadastrado." if duplicado else subalgoritmos.atualizar_usuario(usuarios_sistema, sessao_usuario, nome, email, senha))
            case 3:
                if input("Digite sua senha para excluir o perfil: ") != usuarios_sistema[sessao_usuario]["senha"]:
                    print("Senha incorreta.")
                else:
                    print(subalgoritmos.remover_usuario(usuarios_sistema, sessao_usuario))
                    sessao_usuario = None
            case 4:
                realizar_missao_usuario()
            case 5:
                print(subalgoritmos.interagir_postagens())
            case _:
                print("Opção inexistente.")


def menu_funcionario():
    global sessao_funcionario
    while sessao_funcionario:
        cargo = sessao_funcionario["cargo"]
        print(f"\n===== MENU DO FUNCIONÁRIO - {cargo} =====")
        print("0 - Logout\n1 - Consultar usuários")
        if cargo in ("ADMIN", "TECH"):
            print("2 - Atualizar dados básicos de usuário")
        if cargo == "ADMIN":
            print("3 - Excluir usuário\n4 - Consultar funcionários\n5 - Visualizar resumo")
        elif cargo == "TECH":
            print("3 - Consultar missões de usuário")
        else:
            print("2 - Visualizar ranking\n3 - Visualizar resumo")
        opcao = ler_numero("Escolha: ")
        if opcao is None:
            continue
        if opcao == 0:
            sessao_funcionario = None
            print("Logout realizado com sucesso.")
        elif opcao == 1:
            print(subalgoritmos.listar_usuarios_funcionario(usuarios_sistema, sessao_funcionario))
        elif opcao == 2 and cargo in ("ADMIN", "TECH"):
            usuario = input("Nome do usuário: ").lower()
            print(subalgoritmos.atualizar_usuario_funcionario(usuarios_sistema, sessao_funcionario, usuario, input("Novo nome completo: "), input("Novo e-mail: ")))
        elif opcao == 3 and cargo == "ADMIN":
            print(subalgoritmos.remover_usuario_funcionario(usuarios_sistema, sessao_funcionario, input("Nome do usuário: ").lower()))
        elif opcao == 4 and cargo == "ADMIN":
            print(subalgoritmos.listar_funcionarios(funcionarios_sistema, sessao_funcionario))
        elif opcao == 5 and cargo == "ADMIN":
            print(subalgoritmos.obter_resumo(usuarios_sistema, missoes_sistema, sessao_funcionario))
        elif opcao == 3 and cargo == "TECH":
            print(subalgoritmos.consultar_missoes_usuario(usuarios_sistema, sessao_funcionario, input("Nome do usuário: ").lower()))
        elif opcao == 2 and cargo == "CEO":
            print(subalgoritmos.listar_ranking(usuarios_sistema, sessao_funcionario))
        elif opcao == 3 and cargo == "CEO":
            print(subalgoritmos.obter_resumo(usuarios_sistema, missoes_sistema, sessao_funcionario))
        else:
            print("Opção inexistente ou acesso negado.")


def menu_principal():
    os.system("cls")
    rodando = True
    while rodando:
        print("\n===== MENU INICIAL =====")
        print("0 - Sair\n1 - Criar perfil\n2 - Entrar como usuário\n3 - Entrar como funcionário\n4 - Cadastrar funcionário")
        opcao = ler_numero("Digite a opção desejada: ")
        if opcao is None:
            continue
        match opcao:
            case 0:
                print("Encerrando")
                rodando = False
            case 1:
                print("Faça logout do funcionário antes de criar um usuário.") if sessao_funcionario else cadastrar_usuario()
            case 2:
                entrar_usuario()
            case 3:
                entrar_funcionario()
            case 4:
                cadastrar_funcionario()
            case _:
                print("Opção inexistente.")


if __name__ == "__main__":
    menu_principal()
