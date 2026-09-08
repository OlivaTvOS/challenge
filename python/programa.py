import os

import subalgoritmos
from missoes import missoes_sistema


os.system('cls')

rodando = True

usuarios_sistema = {}


while rodando:

    print("\n===== MENU =====")
    print("0 - Sair")
    print("1 - Criar perfil")
    print("2 - Ver status do perfil")
    print("3 - Atualizar dados do perfil")
    print("4 - Deletar perfil")
    print("5 - Realizar missão")
    print("6 - Ver postagens")

    try:
        opcoes = int(input("Digite a opção desejada: "))
    except ValueError:
        print("Digite apenas números.")
        continue

    if opcoes == 0:
        print("Encerrando")
        rodando = False
        break

    match opcoes:

        case 1:
            print()

            nome_usuario = input("Digite seu nome de usuário: ").lower()
            nome_completo = input("\nDigite seu nome completo: ")
            email = input("\nDigite seu email: ")
            senha_usuario = input("\nDigite sua senha: ")

            nome_usuario_existe = False
            email_existe = False

            for usuario_cadastrado, dados in usuarios_sistema.items():

                if usuario_cadastrado == nome_usuario:
                    nome_usuario_existe = True
                    break

                if dados["email"] == email:
                    email_existe = True
                    break

            if nome_usuario_existe:
                print("Erro! Este nome de usuário está em uso.")

            elif email_existe:
                print("Erro! Este email já está cadastrado.")

            else:
                exibe_adicao = subalgoritmos.adicionar_usuario(usuarios_sistema, nome_usuario, nome_completo, email, senha_usuario)

                print(exibe_adicao)

        case 2:
            nome_usuario = input("Digite o nome do usuário que deseja encontrar: ").lower()

            if nome_usuario in usuarios_sistema:
                consulta = subalgoritmos.consultar_usuario(usuarios_sistema, nome_usuario)
                print(consulta)
            else:
                print("Erro! Usuário inexistente.")

        case 3:
            nome_usuario = input("Digite o nome do usuário que deseja atualizar: ").lower()

            if nome_usuario in usuarios_sistema:

                nome_completo = input("Digite o novo nome completo: ")
                email = input("Digite o novo email: ")
                senha_usuario = input("Digite a nova senha: ")

                atualizacao = subalgoritmos.atualizar_usuario(usuarios_sistema, nome_usuario, nome_completo, email, senha_usuario)

                print(atualizacao)

            else:
                print("Erro! Usuário inexistente.")

        case 4:
            nome_usuario = input("Digite o nome do usuário que deseja remover: ").lower()

            if nome_usuario in usuarios_sistema:
                remove_usuario = subalgoritmos.remover_usuario(usuarios_sistema, nome_usuario)

                print(remove_usuario)
            else:
                print("Usuário inexistente!")

        case 5:
            nome_usuario = input("Digite o nome do usuário: ").lower()

            if nome_usuario not in usuarios_sistema:
                print("Erro! Usuário inexistente.")
                continue

            usuario = usuarios_sistema[nome_usuario]

            missoes_em_andamento = subalgoritmos.listar_missoes_em_andamento(usuario, missoes_sistema)

            if missoes_em_andamento:
                print("\n===== MISSÕES EM ANDAMENTO =====")
                print(missoes_em_andamento)

                continuar = input("\nDeseja continuar uma missão em andamento? (S/N): ").upper()

                if continuar == "S":

                    try:
                        id_missao = int(input("Digite o ID da missão: "))
                    except ValueError:
                        print("Digite apenas números.")
                        continue

                    if id_missao in usuario["missoes"]:

                        print(subalgoritmos.exibir_missao(missoes_sistema, id_missao))

                        concluir = input("\nDeseja concluir esta missão? (S/N): ").upper()

                        if concluir == "S":
                            resultado = subalgoritmos.realizar_missao(usuarios_sistema, missoes_sistema, nome_usuario, id_missao, "CONCLUIR")

                            print(resultado)

                    else:
                        print("Essa missão não está em andamento.")

                    continue

            print("\n===== MISSÕES DISPONÍVEIS =====")
            print(subalgoritmos.listar_missoes(missoes_sistema))

            try:
                id_missao = int(input("\nDigite o ID da missão que deseja realizar: "))
            except ValueError:
                print("Digite apenas números.")
                continue

            if id_missao not in missoes_sistema:
                print("Missão inexistente.")
                continue

            print(subalgoritmos.exibir_missao(missoes_sistema, id_missao))

            iniciar = input("\nDeseja iniciar esta missão? (S/N): ").upper()

            if iniciar == "S":

                resultado = subalgoritmos.realizar_missao(usuarios_sistema, missoes_sistema, nome_usuario, id_missao, "INICIAR")

                print(resultado)

                if id_missao in usuario["missoes"]:

                    concluir = input("\nDeseja concluir a missão agora? (S/N): ").upper()

                    if concluir == "S":

                        resultado = subalgoritmos.realizar_missao(usuarios_sistema, missoes_sistema, nome_usuario, id_missao, "CONCLUIR")

                        print(resultado)

        case 6:
            print("Sistema de postagens ainda não implementado.")

        case _:
            print("Opção inexistente.")