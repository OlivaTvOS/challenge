package funcoes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import dao.MissaoDAO;
import dao.UsuarioDAO;
import dao.UsuarioMissaoDAO;
import database.DatabaseConfig;
import model.Cadastro;
import model.SessaoUsuario;
import model.Missao;

import java.util.List;
import funcionario.AutorizacaoFuncionario;
import funcionario.CargoFuncionario;
import funcionario.Funcionario;
import funcionario.FuncionarioDAO;
import funcionario.GeradorCredenciais;
import funcionario.Permissao;
import funcionario.SenhaUtil;
import funcionario.SessaoFuncionario;

public class Funcoes {

    public static void cadastrarUsuario(Scanner scanner) {
        if (SessaoFuncionario.getInstance().estaLogado()) {
            System.out.println("Faça logout do funcionário antes de cadastrar um usuário.");
            return;
        }
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

    public static void removerPerfil(Scanner scanner) {
        String url = DatabaseConfig.get("db.url");
        String usuario = DatabaseConfig.get("db.user");
        String senha = DatabaseConfig.get("db.password");

        // Abre a conexão com o banco de dados logo no início para validar em tempo real
        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("Conexão realizada com sucesso!");
            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

            String senhaPerfil;

            System.out.print("Tem certeza que deseja deletar seu perfil? (S/N): ");
            String resposta = scanner.nextLine().trim();

            if (!resposta.equalsIgnoreCase("S")) {
                System.out.println("Operação cancelada pelo usuário.");
                return; // Encerra o método aqui mesmo se ele não quiser deletar
            }

            while (true) {
                System.out.print("Digite sua senha para confirmar a exclusão: ");
                senhaPerfil = scanner.nextLine();

                if (senhaPerfil.trim().isEmpty()) {
                    System.out.println("Erro! Você não pode deixar sua senha em branco.");
                    continue;
                }
                break; 
            }

            int idLogado = SessaoUsuario.getInstance().getUsuario().getId();

            boolean sucessoRemover = usuarioDAO.removerPerfil(idLogado, senhaPerfil);

            if (sucessoRemover) {
                System.out.println("Sua conta foi permanentemente excluída. Deslogando...");
            }

        } catch (SQLException e) {
            System.out.println("Erro na conexão: " + e.getMessage());
        }
    }

    public static void logarPerfil(Scanner scanner) {
        if (SessaoFuncionario.getInstance().estaLogado()) {
            System.out.println("Faça logout do funcionário antes de entrar como usuário.");
            return;
        }
        String url = DatabaseConfig.get("db.url");
        String usuario = DatabaseConfig.get("db.user");
        String senha = DatabaseConfig.get("db.password");

        // Abre a conexão com o banco de dados logo no início para validar em tempo real
        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("Conexão realizada com sucesso!");
            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

            String emailPerfil;
            String senhaPerfil;

            // --- ETAPA 3: EMAIL (Texto + Banco) ---
            while (true) {
                System.out.print("Email: ");
                emailPerfil = scanner.nextLine();

                if (emailPerfil.trim().isEmpty()) {
                    System.out.println("Erro! Seu email não pode estar vazio");
                    continue;
                }

                // Já valida no banco na mesma hora!
                if (!usuarioDAO.existeUsuarioRegistro("USUARIO", "EMAIL", emailPerfil)) {
                    System.out.println("Este email não existe!.\n");
                    break; // Volta para o início deste while para pedir o email de novo
                }

                break; // Se passou no empty e no banco, avança para a senha
            }

            // --- ETAPA 4: SENHA ---
            while (true) {
                System.out.print("Senha: ");
                senhaPerfil = scanner.nextLine();

                if (senhaPerfil.trim().isEmpty()) {
                    System.out.println("Erro! Você não pode deixar sua senha em branco");
                    continue;
                }
                break;
            }

            boolean sucessoLogin = usuarioDAO.logarUsuario(emailPerfil.toLowerCase(), senhaPerfil.toLowerCase());

            if (sucessoLogin) {
                System.out.println("\nLogin realizado com sucesso!");
                System.out.println(sucessoLogin);

                String nomeUsuarioLogado = SessaoUsuario.getInstance().getUsuario().getNomeUsuario();
                System.out.println("Usuário: " + nomeUsuarioLogado);
                String nomeCompletoLogado = SessaoUsuario.getInstance().getUsuario().getNomeCompleto();
                System.out.println("Nome: " + nomeCompletoLogado);
                String emailLogado = SessaoUsuario.getInstance().getUsuario().getEmail();
                System.out.println("Email: " + emailLogado);
            } else {
                System.out.println("\nNão foi possível logar no perfil.");
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
                    System.out.println(missao.getIdMissao() + " - " + missao.getTitulo() + " ("
                            + missao.getPontosRecompensa() + " pontos)");
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
                    } else if (conclusao.equalsIgnoreCase("N")) {
                        System.out.println("Missão permanece em andamento.");
                    } else {
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

    public static void logoutUsuario() {
        SessaoUsuario.getInstance().logout();
        System.out.println("Logout realizado com sucesso.");
    }

    public static void cadastrarFuncionario(Scanner scanner) {
        if (SessaoUsuario.getInstance().temUsuarioAtivo() || SessaoFuncionario.getInstance().estaLogado()) {
            System.out.println("Faça logout antes de cadastrar um funcionário.");
            return;
        }

        // Esta opção existe somente para demonstração acadêmica.
        System.out.println("=========================================");
        System.out.println("MODO DE DEMONSTRAÇÃO ACADÊMICA");
        System.out.println("Cadastro de funcionários administrativos.");
        System.out.println("Esta opção não estaria disponível publicamente.");
        System.out.println("=========================================");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Último sobrenome: ");
        String sobrenome = scanner.nextLine().trim();
        if (nome.isEmpty() || sobrenome.isEmpty()) {
            System.out.println("Nome e sobrenome são obrigatórios.");
            return;
        }
        System.out.println("1 - ADMIN\n2 - TECH\n3 - CEO");
        System.out.print("Cargo: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();
        CargoFuncionario cargo = switch (opcao) {
            case 1 -> CargoFuncionario.ADMIN;
            case 2 -> CargoFuncionario.TECH;
            case 3 -> CargoFuncionario.CEO;
            default -> null;
        };
        if (cargo == null) {
            System.out.println("Cargo inválido.");
            return;
        }

        try (Connection conn = abrirConexao()) {
            FuncionarioDAO dao = new FuncionarioDAO(conn);
            int id = dao.gerarIdDisponivel();
            if (id == -1) {
                System.out.println("Todos os IDs de 1 a 200 estão ocupados.");
                return;
            }
            String email = dao.gerarEmail(nome, sobrenome);
            String senhaGerada = GeradorCredenciais.gerarSenha();
            Funcionario funcionario = new Funcionario(id, nome, sobrenome, email, SenhaUtil.gerarHash(senhaGerada), cargo);
            if (dao.cadastrar(funcionario)) {
                System.out.println("Funcionário criado com sucesso.\n");
                System.out.println("ID: " + id);
                System.out.println("E-mail: " + email);
                System.out.println("Senha: " + senhaGerada);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao operar no banco: " + e.getMessage());
        }
    }

    public static void logarFuncionario(Scanner scanner) {
        if (SessaoUsuario.getInstance().temUsuarioAtivo()) {
            System.out.println("Faça logout do usuário antes de entrar como funcionário.");
            return;
        }
        System.out.print("ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        try (Connection conn = abrirConexao()) {
            Funcionario funcionario = new FuncionarioDAO(conn).autenticar(id, email, senha);
            if (funcionario == null) {
                System.out.println("Credenciais inválidas.");
                return;
            }
            SessaoFuncionario.getInstance().setFuncionario(funcionario);
            System.out.println("Login de funcionário realizado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao operar no banco.");
        }
    }

    public static void logoutFuncionario() {
        SessaoFuncionario.getInstance().logout();
        System.out.println("Logout realizado com sucesso.");
    }

    public static void consultarUsuariosFuncionario() {
        if (!AutorizacaoFuncionario.autorizar(Permissao.CONSULTAR_USUARIO)) return;
        try (Connection conn = abrirConexao()) {
            for (Cadastro cadastro : new UsuarioDAO(conn).listarUsuarios()) System.out.println(cadastro);
        } catch (SQLException e) { System.out.println("Erro ao consultar usuários."); }
    }

    public static void editarUsuarioFuncionario(Scanner scanner) {
        if (!AutorizacaoFuncionario.autorizar(Permissao.EDITAR_USUARIO)) return;
        System.out.print("ID do usuário: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Novo nome completo: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Novo e-mail: ");
        String email = scanner.nextLine().trim();
        if (nome.isEmpty() || email.isEmpty()) { System.out.println("Nome e e-mail são obrigatórios."); return; }
        try (Connection conn = abrirConexao()) {
            System.out.println(new UsuarioDAO(conn).alterarDadosBasicos(id, nome, email) ? "Dados básicos atualizados." : "Usuário não atualizado.");
        } catch (SQLException e) { System.out.println("Erro ao atualizar usuário."); }
    }

    public static void excluirUsuarioFuncionario(Scanner scanner) {
        if (!AutorizacaoFuncionario.autorizar(Permissao.EXCLUIR_USUARIO)) return;
        System.out.print("ID do usuário: ");
        int id = scanner.nextInt(); scanner.nextLine();
        try (Connection conn = abrirConexao()) {
            System.out.println(new UsuarioDAO(conn).removerUsuarioComoFuncionario(id) ? "Usuário removido." : "Usuário não removido.");
        } catch (SQLException e) { System.out.println("Erro ao remover usuário."); }
    }

    public static void consultarFuncionarios() {
        if (!AutorizacaoFuncionario.autorizar(Permissao.CONSULTAR_FUNCIONARIO)) return;
        try (Connection conn = abrirConexao()) {
            for (Funcionario funcionario : new FuncionarioDAO(conn).listar()) System.out.println(funcionario);
        } catch (SQLException e) { System.out.println("Erro ao consultar funcionários."); }
    }

    public static void consultarResumo() {
        if (!AutorizacaoFuncionario.autorizar(Permissao.CONSULTAR_RESUMO)) return;
        try (Connection conn = abrirConexao()) {
            int totalUsuarios = new UsuarioDAO(conn).listarUsuarios().size();
            int totalFuncionarios = new FuncionarioDAO(conn).listar().size();
            System.out.println("Usuários cadastrados: " + totalUsuarios);
            System.out.println("Funcionários cadastrados: " + totalFuncionarios);
        } catch (SQLException e) { System.out.println("Erro ao consultar resumo."); }
    }

    private static Connection abrirConexao() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.get("db.url"), DatabaseConfig.get("db.user"), DatabaseConfig.get("db.password"));
    }
}
