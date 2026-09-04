def adicionar_usuario(funcionamento_usuario: dict, nome_usuario: str, nome_completo: str, email: str, senhaUsuario):
    funcionamento_usuario[nome_usuario] = {"nome_completo":nome_completo, "email": email, "senha": senhaUsuario}

    return "Usuário criado com sucesso!"


def consultar_usuario(funcionamento_usuario: dict, nome_usuario: str) -> str:
    if nome_usuario in funcionamento_usuario:
        dados = funcionamento_usuario[nome_usuario]
        return (
            f"\nNome de Usuário.....: {nome_usuario}"
            f"\nNome completo.....: {dados['nome_completo']} "
            f"\nEmail.....: {dados['email']}"
            f"\nSenha.....: {dados['senha']}"                    
            )

    """ return "Este usuário não existe" """


def atualizar_usuario(funcionamento_usuario: dict, nome_usuario: str, nome_completo: str, email: str, senhaUsuario) -> str:
    if nome_usuario in funcionamento_usuario:
        funcionamento_usuario[nome_usuario] = {"nome_completo":nome_completo, "email": email, "senha": senhaUsuario}
        return (
            f"\nNome de Usuário.....: {nome_usuario}"
            f"\nNome completo.....: {nome_completo} "
            f"\nNome de Usuário.....: {email}"
            f"\nNome completo.....: {senhaUsuario}"       
        )

    """ return "Este usuário não existe"
 """
def remover_usuario(funcionamento_usuario: dict, nome_usuario: str) -> str:
    if nome_usuario in funcionamento_usuario:
        funcionamento_usuario.pop(nome_usuario)

        return "Usuário removido com sucesso!"

    """ return "Ussuário não foi removido" """

def realizar_missao():
    return None

def interagir_postagens():
    return None