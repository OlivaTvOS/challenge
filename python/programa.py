import json

import api_java
import api_externa
import api_oracle


sessao_usuario = None


def ler_numero(mensagem):
    """Lê um número inteiro e informa entradas inválidas."""
    try:
        return int(input(mensagem))
    except ValueError:
        print("Digite apenas números.")
        return None


def cadastrar_usuario():
    """Cadastra um usuário na API Java."""
    global sessao_usuario
    nome_usuario = input("Nome de usuário: ").strip()
    nome_completo = input("Nome completo: ")
    email = input("E-mail: ")
    senha = input("Senha: ")

    try:
        sessao_usuario = api_java.cadastrar_usuario(
            nome_usuario,
            nome_completo,
            email,
            senha,
        )
        if sessao_usuario is None or "id" not in sessao_usuario:
            print("A API não retornou os dados da conta criada.")
            sessao_usuario = None
            return
        print("Usuário cadastrado com sucesso.")
        menu_usuario()
    except RuntimeError as erro:
        print(erro)


def entrar_usuario():
    """Autentica um usuário pela API Java."""
    global sessao_usuario
    email = input("E-mail: ")
    senha = input("Senha: ")
    try:
        sessao_usuario = api_java.entrar_usuario(email, senha)
        if sessao_usuario is None or "id" not in sessao_usuario:
            print("A API não retornou os dados da conta.")
            sessao_usuario = None
            return
        print("Login realizado com sucesso.")
        menu_usuario()
    except RuntimeError as erro:
        print(erro)


def realizar_missao_usuario():
    """Consulta, organiza e permite atualizar as missões do usuário."""
    try:
        em_andamento = api_java.listar_missoes_em_andamento(sessao_usuario["id"])
        concluidas = api_java.listar_missoes_concluidas(sessao_usuario["id"])
        missoes = api_java.listar_missoes()
    except RuntimeError as erro:
        print(erro)
        return

    grupos = organizar_missoes(em_andamento, concluidas, missoes)
    for grupo in grupos:
        if grupo["missoes"] or grupo["situacao"] == "Disponíveis":
            print(f"\n===== MISSÕES {grupo['situacao'].upper()} =====")
            exibir_missoes(grupo["missoes"])

    id_missao = ler_numero("ID da missão: ")
    if id_missao is None:
        return

    missao_selecionada = filtrar_missao_por_id(missoes, id_missao)
    if missao_selecionada is None:
        print("Missão inexistente.")
        return

    exibir_missao(missao_selecionada)
    acao = input("Ação (I para iniciar, C para concluir): ").upper()
    try:
        if acao == "I":
            resposta = api_java.iniciar_missao(sessao_usuario["id"], id_missao)
        elif acao == "C":
            resposta = api_java.concluir_missao(sessao_usuario["id"], id_missao)
        else:
            print("Ação inválida.")
            return
        if resposta is None or "message" not in resposta:
            print("A API Java retornou uma resposta inválida.")
            return
        print(resposta["message"])
    except RuntimeError as erro:
        print(erro)


def exibir_missoes(missoes):
    """Exibe os registros de uma lista de dicionários de missões."""
    if not missoes:
        print("Nenhuma missão encontrada.")
        return

    for missao in missoes:
        categoria = api_java.CATEGORIAS.get(
            missao["idCategoria"],
            str(missao["idCategoria"]),
        )
        print(
            f"\nID: {missao['idMissao']}"
            f"\nTítulo: {missao['titulo']}"
            f"\nCategoria: {categoria}"
            f"\nPontos: {missao['pontosRecompensa']}"
            "\n------------------------"
        )


def exibir_missao(missao):
    """Exibe os dados de um dicionário de missão."""
    categoria = api_java.CATEGORIAS.get(
        missao["idCategoria"],
        str(missao["idCategoria"]),
    )
    print(
        "\n===== MISSÃO SELECIONADA ====="
        f"\nID: {missao['idMissao']}"
        f"\nTítulo: {missao['titulo']}"
        f"\nCategoria: {categoria}"
        f"\nDescrição: {missao['descricao']}"
        f"\nPontos: {missao['pontosRecompensa']}"
    )


def exibir_ranking():
    """Consulta e exibe a sequência de registros do ranking."""
    try:
        ranking = api_java.listar_ranking_global()
    except RuntimeError as erro:
        print(erro)
        return

    if not ranking:
        print("Nenhum usuário cadastrado.")
        return

    for item in ranking:
        print(
            f"\n{item['posicao']}º - {item['usuario']}: "
            f"{item['pontos']} pontos"
        )


def exportar_consultas_json():
    """Exporta consultas do perfil e das missões para um arquivo JSON."""
    id_usuario = sessao_usuario["id"]
    try:
        perfil = api_java.buscar_perfil(id_usuario)
        em_andamento = api_java.listar_missoes_em_andamento(id_usuario)
        concluidas = api_java.listar_missoes_concluidas(id_usuario)
    except RuntimeError as erro:
        print(erro)
        return

    if perfil is None or "id" not in perfil:
        print("A API Java retornou os dados do perfil em formato inválido.")
        return

    campos_perfil = ("id", "nomeUsuario", "nomeCompleto", "email", "pontos")
    perfil_exportado = {}
    for campo in campos_perfil:
        if campo not in perfil:
            print("A API Java retornou os dados do perfil incompletos.")
            return
        perfil_exportado[campo] = perfil[campo]

    dados = {
        "perfil": perfil_exportado,
        "missoes_em_andamento": em_andamento,
        "missoes_concluidas": concluidas,
    }
    nome_arquivo = f"consultas_usuario_{id_usuario}.json"
    try:
        with open(nome_arquivo, "w", encoding="utf-8") as arquivo:
            json.dump(dados, arquivo, ensure_ascii=False, indent=4)
        print(f"Consultas exportadas para {nome_arquivo}.")
    except OSError as erro:
        print(f"Não foi possível salvar o arquivo JSON: {erro}")


def consultar_clima():
    """Consulta o clima atual em coordenadas informadas pelo usuário."""
    try:
        latitude = float(input("Latitude: ").replace(",", "."))
        longitude = float(input("Longitude: ").replace(",", "."))
    except ValueError:
        print("Informe latitude e longitude numéricas.")
        return

    if latitude < -90 or latitude > 90:
        print("A latitude deve estar entre -90 e 90.")
        return
    if longitude < -180 or longitude > 180:
        print("A longitude deve estar entre -180 e 180.")
        return

    try:
        clima = api_externa.consultar_clima(latitude, longitude)
        print(
            "\nClima atual para planejar atividades ao ar livre"
            f"\nTemperatura: {clima['temperature_2m']} °C"
            f"\nPrecipitação: {clima['precipitation']} mm"
        )
    except (RuntimeError, KeyError) as erro:
        print(erro)



def consultar_relatorios_oracle():
    """Consulta os quatro relatórios SQL definidos para a Sprint 4."""
    try:
        resultados = api_oracle.listar_consultas()
    except RuntimeError as erro:
        print(erro)
        return

    for titulo, registros in resultados.items():
        print(f"\n===== {titulo.upper()} =====")
        if not registros:
            print("Nenhum registro encontrado.")
            continue

        for registro in registros:
            print("------------------------")
            for campo, valor in registro.items():
                print(f"{campo.replace('_', ' ').capitalize()}: {valor}")

        print(f"Total de registros: {len(registros)}")

    opcao = input("\nExportar estes relatórios para JSON? (S/N): ").strip().upper()
    if opcao == "S":
        nome_arquivo = "relatorios_soulup_oracle.json"
        try:
            with open(nome_arquivo, "w", encoding="utf-8") as arquivo:
                json.dump(resultados, arquivo, ensure_ascii=False, indent=4, default=str)
            print(f"Relatórios exportados para {nome_arquivo}.")
        except OSError as erro:
            print(f"Não foi possível salvar o arquivo JSON: {erro}")
    elif opcao != "N":
        print("Opção inválida. Os relatórios não foram exportados.")


def organizar_missoes(em_andamento, concluidas, catalogo):
    """Separa as missões em andamento, concluídas e ainda disponíveis."""
    disponiveis = []

    # Uma missão só fica disponível se ainda não estiver em outra situação.
    for missao in catalogo:
        encontrada = False

        for missao_andamento in em_andamento:
            if missao["idMissao"] == missao_andamento["idMissao"]:
                encontrada = True

        for missao_concluida in concluidas:
            if missao["idMissao"] == missao_concluida["idMissao"]:
                encontrada = True

        if not encontrada:
            disponiveis.append(missao)

    grupos = []
    grupos.append({"situacao": "Em andamento", "missoes": em_andamento})
    grupos.append({"situacao": "Concluídas", "missoes": concluidas})
    grupos.append({"situacao": "Disponíveis", "missoes": disponiveis})
    return grupos


def filtrar_missao_por_id(missoes, id_missao):
    """Retorna da lista a missão que corresponde ao identificador informado."""
    for missao in missoes:
        if missao["idMissao"] == id_missao:
            return missao
    return None


def menu_usuario():
    """Apresenta as operações disponíveis para a sessão autenticada."""
    global sessao_usuario
    while sessao_usuario:
        print("\n===== MENU DO USUÁRIO =====")
        print(
            "0 - Logout\n1 - Ver perfil\n2 - Atualizar perfil\n"
            "3 - Excluir perfil\n4 - Realizar missão\n5 - Ver ranking\n"
            "6 - Exportar consultas para JSON\n7 - Consultar clima\n"
            "8 - Relatórios do banco Oracle"
        )
        opcao = ler_numero("Escolha: ")
        if opcao is None:
            continue
        if opcao == 0:
            # A sessão termina sem remover a conta do banco.
            sessao_usuario = None
            print("Logout realizado com sucesso.")
        elif opcao == 1:
            # O perfil traz os pontos persistidos após a conclusão das missões.
            try:
                perfil = api_java.buscar_perfil(sessao_usuario["id"])
                if perfil is None or "id" not in perfil:
                    print("A API Java retornou os dados do perfil em formato inválido.")
                    continue
                print(
                    f"\nNome de usuário: {perfil['nomeUsuario']}"
                    f"\nNome completo: {perfil['nomeCompleto']}"
                    f"\nE-mail: {perfil['email']}"
                    f"\nPontos: {perfil['pontos']}"
                )
            except RuntimeError as erro:
                print(erro)
        elif opcao == 2:
            nome_atual = sessao_usuario["nomeUsuario"]
            nome_usuario = input(f"Nome de usuário ({nome_atual}): ").strip()
            if not nome_usuario:
                nome_usuario = nome_atual
            nome = input("Nome completo: ")
            email = input("E-mail: ")
            senha = input("Nova senha: ")
            try:
                # A API valida duplicidade sem bloquear os dados da própria conta.
                id_usuario = sessao_usuario["id"]
                api_java.atualizar_perfil(
                    id_usuario,
                    nome_usuario,
                    nome,
                    email,
                    senha,
                )
                perfil_atualizado = api_java.buscar_perfil(id_usuario)
                if perfil_atualizado is None or "id" not in perfil_atualizado:
                    print("A API Java retornou os dados do perfil em formato inválido.")
                    continue
                sessao_usuario = perfil_atualizado
                print("Dados do perfil atualizados com sucesso.")
            except RuntimeError as erro:
                print(erro)
        elif opcao == 3:
            # A senha é verificada pela API antes de excluir a conta.
            senha = input("Senha para excluir o perfil: ")
            try:
                resposta = api_java.remover_perfil(sessao_usuario["id"], senha)
                if resposta is None or "message" not in resposta:
                    print("A API Java retornou uma resposta inválida.")
                    continue
                print(resposta["message"])
                sessao_usuario = None
            except RuntimeError as erro:
                print(erro)
        elif opcao == 4:
            realizar_missao_usuario()
        elif opcao == 5:
            exibir_ranking()
        elif opcao == 6:
            exportar_consultas_json()
        elif opcao == 7:
            consultar_clima()
        elif opcao == 8:
            consultar_relatorios_oracle()
        else:
            print("Opção inexistente.")


def menu_principal():
    """Apresenta as opções de cadastro, login e encerramento."""
    rodando = True
    while rodando:
        print("\n===== MENU INICIAL =====")
        print("0 - Sair\n1 - Criar perfil\n2 - Entrar")
        opcao = ler_numero("Escolha: ")
        if opcao is None:
            continue
        if opcao == 0:
            print("Encerrando")
            rodando = False
        elif opcao == 1:
            if sessao_usuario:
                print("Faça logout antes de criar outro perfil.")
            else:
                cadastrar_usuario()
        elif opcao == 2:
            entrar_usuario()
        else:
            print("Opção inexistente.")


if __name__ == "__main__":
    menu_principal()
