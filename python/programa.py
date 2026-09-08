import os
import subalgoritmos
os.system('cls')

rodando = True

usuarios_sistema = {}

while rodando:

    print("\n===== MENU =====");
                
    print("0 - Sair")
    print("1 - Criar perfil")
    print("2 - Ver status do perfil")
    print("3 - Atualizar dados do perfil")
    print("4 - Deletar perfil")
    print("5 - Realizar missão")
    print("6 - Ver postagens")

    opcoes = int(input("Digite a opção desejada: "))

    if opcoes == 0:
        print("Encerrando")
        rodando = False
        break

    match opcoes:
        case 1:
            print()
            nome_usuario = input("Digite seu nome de usuário: ")
            nome_completo = input("\nDigite seu nome completo: ")
            email = input("\nDigite seu email: ")
            senha_usuario = input("\nDigite sua senha: ")

            nome_usuario_existe = False
            nome_usuario_procurar = email

            email_existe = False
            email_procurar = email

            for usuarios in usuarios_sistema:
                if usuarios_sistema['nome_usuario'] == nome_usuario_procurar:
                    nome_usuario_existe = True
                    break
            
                for emails in usuarios_sistema:
                    if emails['email'] == email_procurar:
                        email_existe = True
                        break
            if nome_usuario_existe:
                print("Erro! Este nome esta em uso")
            elif email_existe:
                print("Erro! Este email já está cadastrado")
            else:
                exibe_adicao = subalgoritmos.adicionar_usuario(usuarios_sistema, nome_usuario, nome_completo, email, senha_usuario)
                print("Usuário adicionado ao sistema")
                print(exibe_adicao)

        case 2:
            nome_usuario = input("Digite o nome do usuário que deseja encontrar: ")

            if nome_usuario in usuarios_sistema:
                consulta = subalgoritmos.consultar_usuario(usuarios_sistema, nome_usuario)
                print(consulta)
            else:
                print("Erro! Usuário inexistente")
        case 3:
            nome_usuario = input("Digite o nome do usuário que deseja atualizar: ")
            nome_completo = input("Digite o novo nome completo: ")
            email = input("Digite o novo email: ")
            senha_usuario = input("Digite a nova senha: ")

            if nome_usuario in usuarios_sistema:
                atualizacao = subalgoritmos.atualizar_usuario(usuarios_sistema, nome_usuario, nome_completo, email, senha_usuario)
                print("Usuário atualizado com sucesso")
                print(atualizacao)
            else:
                print("Erro! Usuário inexistente")
        case 4:
            nome_usuario = input("Digite o nome do usuário que deseja remover: ")

            if nome_usuario in usuarios_sistema:
                remove_usuario = subalgoritmos.remover_usuario(usuarios_sistema, nome_usuario)
                print(remove_usuario)
            else:
                print("Usuário inexistente!")
        case 5:
            print()
        case _: 
            print("Opção inexistente")