package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static void main() {
        // Scanner gerenciado por try-with-resources (fecha automaticamente ao sair do programa)
        try (Scanner scanner = new Scanner(System.in)) {
            boolean continuar = true;

            while (continuar) {
                // Até a 4 são exclusivamente funções para atender os requsitos de um CRUD
                System.out.println("\n===== MENU =====");
                
                System.out.println("0 - Sair"); //  ✅ Feito

                System.out.println("1 - Criar perfil"); // ✅ Feito

                System.out.println("2 - Ver status do perfil"); // Ver missões realizadas, tipos de interação, quantidade de interações (possivelmente com visualização de diferentes perídos)

                System.out.println("3 - Atualizar dados do perfil"); // Permite atualizar informações erradas digitadas pelo usuário (somente com confirmação dos dados).

                System.out.println("4 - Deletar perfil"); // Apenas deleta o perfil do banco com confirmação dos dados do usuário

                System.out.println("5 - Realizar missão"); // Realizar missões engloba período de testes, adição no ranking pós período, junção dos dados para geração do csv, realização contínua de missões pós avaliação, aumento no ranking, pontuação para cada missão

                System.out.println("6 - Ver postagens"); // Ver postagens abrange dar curtidas, comentar, ir para próxima ou parar de ver
                System.out.print("Escolha: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer do teclado após ler o número

                // Utilizando a sintaxe moderna do Switch (Java 14+)
                switch (opcao) {
                    case 0:
                        System.out.println("Saindo do sistema... Até logo!");
                        continuar = false;
                        break;
                    case 1:
                        cadastrarUsuario(scanner);
                        break;
                    case 2:
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                        break;
                }
            }
        }
    }

    /**
     * Método auxiliar para isolar a lógica de cadastro e manter o método main limpo.
     */
    private static void cadastrarUsuario(Scanner scanner) {
        // 1. Dados de configuração do banco (carregados antes para evitar abrir conexão à toa)
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
                if (usuarioDAO.existeRegistro("USUARIO", "NOME", nomeUsuario)) {
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
                if (usuarioDAO.existeRegistro("USUARIO", "EMAIL", email)) {
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
}
