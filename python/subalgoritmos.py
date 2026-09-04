def adicionar_usuario(funcionamento_usuario: dict, nome_usuario: str, nome_completo: str, email: str, senhaUsuario) -> dict:
    funcionamento_usuario[nome_usuario] = {"nome_completo":nome_completo, "email": email, "senha": senhaUsuario}

    return funcionamento_usuario

def consultar_usuario(funcionamento_usuario: dict, nome_usuario: str) -> str:
    if nome_usuario in funcionamento_usuario:
        dados = funcionamento_usuario[nome_usuario]
        return (
            f"\nNome de Usuário.....: {nome_usuario}"
            f"\nNome completo.....: {dados['nome_completo']} "
            f"\nEmail.....: {dados['email']}"
            f"\nSenha.....: {dados['senha']}"                    
            )

    return "Este usuário não existe"