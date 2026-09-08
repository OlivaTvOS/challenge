package minhas_funcoes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import database.DatabaseConfig;
import missao.MissaoDAO;
import usuario.Cadastro;
import usuario.SessaoUsuario;
import usuario.UsuarioDAO;
import missao.Missao;
import missao.MissaoDAO;
import missao.UsuarioMissaoDAO;

import java.util.List;

public class funcoes {

    public static void cadastrarUsuario(Scanner scanner) {
        // 1. Dados de configuração do banco (carregados antes para evitar abrir conexão
        // à toa)
        String url = DatabaseConfig.get("db.url");
        String usuario = DatabaseConfig.get("db.user");
        String senha = DatabaseConfig.get("db.password");

        // Abre a conexão com o banco de dados logo no início para validar em tempo real
        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("Conexão realizada com sucesso!");
            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

            String nomeUsuario;
            String nomeCompleto;
            String email;
            String senhaUsuario;

            // --- ETAPA 1: NOME DE USUÁRIO (Texto + Banco) ---
            while (true) {
                System.out.print("Nome de usuário: ");
                nomeUsuario = scanner.nextLine();

                if (nomeUsuario.trim().isEmpty()) {
                    System.out.println("Erro! Seu usuário não pode estar vazio!");
                    continue; // Volta para o início deste while para pedir o usuário de novo
                }

                // Já valida no banco na mesma hora!
                if (usuarioDAO.existeUsuarioRegistro("USUARIO", "NOME", nomeUsuario)) {
                    System.out.println("Este nome de usuário já existe! Escolha outro.\n");
                    continue; // Volta para o início deste while para pedir o usuário de novo
                }

                break; // Se passou no empty e no banco, sai deste loop e vai para o próximo campo
            }

            // --- ETAPA 2: NOME COMPLETO ---
            while (true) {
                System.out.print("Nome completo: ");
                nomeCompleto = scanner.nextLine();

                if (nomeCompleto.trim().isEmpty()) {
                    System.out.println("Erro! Você não pode deixar seu nome vazio");
                    continue;
                }
                break;
            }

            // --- ETAPA 3: EMAIL (Texto + Banco) ---
            while (true) {
                System.out.print("Email: ");
                email = scanner.nextLine();

                if (email.trim().isEmpty()) {
                    System.out.println("Erro! Seu email não pode estar vazio");
                    continue;
                }

                // Já valida no banco na mesma hora!
                if (usuarioDAO.existeUsuarioRegistro("USUARIO", "EMAIL", email)) {
                    System.out.println("Este email já está vinculado a uma conta! Escolha outro.\n");
                    continue; // Volta para o início deste while para pedir o email de novo
                }

                break; // Se passou no empty e no banco, avança para a senha
            }

            // --- ETAPA 4: SENHA ---
            while (true) {
                System.out.print("Senha: ");
                senhaUsuario = scanner.nextLine();

                if (senhaUsuario.trim().isEmpty()) {
                    System.out.println("Erro! Você não pode deixar sua senha em branco");
                    continue;
                }
                break;
            }

            // 2. CRIAÇÃO DO OBJETO (Garantido que os dados estão 100% validados e corretos)
            Cadastro cadastro = new Cadastro(nomeUsuario, nomeCompleto, email, senhaUsuario);

            boolean criado = usuarioDAO.criarUsuario(cadastro);

            if (criado) {
                System.out.println("\nPerfil criado com sucesso!");
                System.out.println(cadastro);
            } else {
                System.out.println("\nNão foi possível criar o perfil.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao operar no banco");
        }
    }

    public static void statusPerfil() {
        Cadastro perfil = SessaoUsuario.getInstance().getUsuario();

        if (perfil != null) {
            System.out.println("\n=== PERFIL DO USUÁRIO ===");
            System.out.println("Username: " + perfil.getNomeUsuario());
            System.out.println("Nome: " + perfil.getNomeCompleto());
            System.out.println("Email: " + perfil.getEmail());
        } else {
            System.out.println("Usuários não encontrados!");
        }
    }

    public static void alterarDadosPerfil(Scanner scanner) {
        String url = DatabaseConfig.get("db.url");
        String usuario = DatabaseConfig.get("db.user");
        String senha = DatabaseConfig.get("db.password");

        // Abre a conexão com o banco de dados logo no início para validar em tempo real
        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("Conexão realizada com sucesso!");
            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

            String novoNomeUsuario;
            String novoNomeCompleto;
            String novoEmail;
            String novaSenhaUsuario;

            // --- ETAPA 1: NOME DE USUÁRIO (Texto + Banco) ---
            while (true) {
                System.out.print("Nome de usuário: ");
                novoNomeUsuario = scanner.nextLine();

                if (novoNomeUsuario.trim().isEmpty()) {
                    System.out.println("Erro! Seu usuário não pode estar vazio!");
                    continue; // Volta para o início deste while para pedir o usuário de novo
                }

                // Já valida no banco na mesma hora!
                if (usuarioDAO.existeUsuarioRegistro("USUARIO", "NOME", novoNomeUsuario)) {
                    System.out.println("Este nome de usuário já existe! Escolha outro.\n");
                    continue; // Volta para o início deste while para pedir o usuário de novo
                }

                break; // Se passou no empty e no banco, sai deste loop e vai para o próximo campo
            }

            // --- ETAPA 2: NOME COMPLETO ---
            while (true) {
                System.out.print("Nome completo: ");
                novoNomeCompleto = scanner.nextLine();

                if (novoNomeCompleto.trim().isEmpty()) {
                    System.out.println("Erro! Você não pode deixar seu nome vazio");
                    continue;
                }
                break;
            }

            // --- ETAPA 3: EMAIL (Texto + Banco) ---
            while (true) {
                System.out.print("Email: ");
                novoEmail = scanner.nextLine();

                if (novoEmail.trim().isEmpty()) {
                    System.out.println("Erro! Seu email não pode estar vazio");
                    continue;
                }

                // Já valida no banco na mesma hora!
                if (usuarioDAO.existeUsuarioRegistro("USUARIO", "EMAIL", novoEmail)) {
                    System.out.println("Este email já está vinculado a uma conta! Escolha outro.\n");
                    continue; // Volta para o início deste while para pedir o email de novo
                }

                break; // Se passou no empty e no banco, avança para a senha
            }

            // --- ETAPA 4: SENHA ---
            while (true) {
                System.out.print("Senha: ");
                novaSenhaUsuario = scanner.nextLine();

                if (novaSenhaUsuario.trim().isEmpty()) {
                    System.out.println("Erro! Você não pode deixar sua senha em branco");
                    continue;
                }
                break;
            }

            // 2. ALTERAÇÃO DO OBJETO (Garantido que os dados estão 100% validados e
            // corretos)
            int idLogado = SessaoUsuario.getInstance().getUsuario().getId();

            Cadastro cadastro = new Cadastro(idLogado, novoNomeUsuario, novoNomeCompleto, novoEmail, novaSenhaUsuario);

            boolean alterado = usuarioDAO.alterarDadosUsuario(cadastro);

            if (alterado) {
                System.out.println("\nDados do perfil alterados com sucesso!");
                System.out.println(alterado);

                SessaoUsuario.getInstance().setUsuario(cadastro);
            } else {
                System.out.println("\nNão foi possível os dados do perfil.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao operar no banco");
        }
    }

    public static void realizarMissao(Scanner scanner) {

        String url = DatabaseConfig.get("db.url");
        String usuario = DatabaseConfig.get("db.user");
        String senha = DatabaseConfig.get("db.password");

        Cadastro usuarioLogado = SessaoUsuario.getInstance().getUsuario();

        if (usuarioLogado == null) {
            System.out.println("Nenhum usuário está logado.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {

            System.out.println("Conexão realizada com sucesso!");

            int idUsuario = usuarioLogado.getId();

            MissaoDAO missaoDAO = new MissaoDAO(conn);
            UsuarioMissaoDAO usuarioMissaoDAO = new UsuarioMissaoDAO(conn);

            List<Missao> missoesEmAndamento = usuarioMissaoDAO.listarMissoesEmAndamento(idUsuario);

            if (!missoesEmAndamento.isEmpty()) {

                System.out.println("\n=== MISSÕES EM ANDAMENTO ===");

                for (Missao missao : missoesEmAndamento) {
                    System.out.println(missao.getIdMissao() + " - " + missao.getTitulo() + " (" + missao.getPontosRecompensa() + " pontos)");
                }

                System.out.print("\nDeseja continuar uma missão em andamento? (S/N): ");
                String continuar = scanner.nextLine();

                if (continuar.equalsIgnoreCase("S")) {

                    System.out.print("Digite o ID da missão: ");
                    int idMissao = scanner.nextInt();
                    scanner.nextLine();

                    Missao missaoSelecionada = usuarioMissaoDAO.buscarMissaoEmAndamento(
                            idUsuario,
                            idMissao);

                    if (missaoSelecionada == null) {
                        System.out.println("Essa missão não está em andamento.");
                        return;
                    }

                    System.out.println("\n=== MISSÃO EM ANDAMENTO ===");
                    System.out.println("Título: " + missaoSelecionada.getTitulo());
                    System.out.println("Descrição: " + missaoSelecionada.getDescricao());
                    System.out.println("Pontos: " + missaoSelecionada.getPontosRecompensa());

                    System.out.print("\nDeseja concluir a missão? (S/N): ");
                    String conclusao = scanner.nextLine();

                    if (conclusao.equalsIgnoreCase("S")) {
                        usuarioMissaoDAO.concluirMissao(idUsuario, idMissao);
                    } else if (conclusao.equalsIgnoreCase("N")){
                        System.out.println("Missão permanece em andamento.");
                    }
                    else {
                        System.out.println("Opção inválida");
                    }

                    return;
                }
            }

            List<Missao> missoes = missaoDAO.listarMissoes();

            if (missoes.isEmpty()) {
                System.out.println("Nenhuma missão disponível.");
                return;
            }

            System.out.println("\n=== MISSÕES DISPONÍVEIS ===");

            for (Missao missao : missoes) {
                System.out.println(
                        missao.getIdMissao() + " - " +
                                missao.getTitulo() + " (" +
                                missao.getPontosRecompensa() + " pontos)");
            }

            System.out.print("\nDigite o ID da missão: ");
            int idMissao = scanner.nextInt();
            scanner.nextLine();

            Missao missaoSelecionada = missaoDAO.buscarMissao(idMissao);

            if (missaoSelecionada == null ||
                    !"S".equals(missaoSelecionada.getAtiva())) {

                System.out.println("Missão inválida.");
                return;
            }

            System.out.println("\n=== MISSÃO SELECIONADA ===");
            System.out.println("Título: " + missaoSelecionada.getTitulo());
            System.out.println("Descrição: " + missaoSelecionada.getDescricao());
            System.out.println("Pontos: " + missaoSelecionada.getPontosRecompensa());

            System.out.print("\nDeseja iniciar esta missão? (S/N): ");
            String confirmacao = scanner.nextLine();

            if (!confirmacao.equalsIgnoreCase("S")) {
                System.out.println("Missão não iniciada.");
                return;
            }

            boolean iniciada = usuarioMissaoDAO.iniciarMissao(
                    idUsuario,
                    missaoSelecionada.getIdMissao());

            if (!iniciada) {
                return;
            }

            System.out.print("\nDeseja concluir a missão agora? (S/N): ");
            String conclusao = scanner.nextLine();

            if (conclusao.equalsIgnoreCase("S")) {

                usuarioMissaoDAO.concluirMissao(
                        idUsuario,
                        missaoSelecionada.getIdMissao());

            } else {
                System.out.println("Missão permanece em andamento.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao operar no banco: " + e.getMessage());
        }
    }
}
