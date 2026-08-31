package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String url = DatabaseConfig.get("db.url");
        String usuario = DatabaseConfig.get("db.user");
        String senha = DatabaseConfig.get("db.password");

        try (Connection conn =
                     DriverManager.getConnection(url, usuario, senha)) {

            System.out.println("Conexão realizada!");

            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

            System.out.println("\n===== MENU =====");
            System.out.println("1 - Criar perfil");
            System.out.println("2 - Sair");
            System.out.print("Escolha: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {

                System.out.print("Nome de usuário: ");
                String nomeUsuario = scanner.nextLine();

                System.out.print("Nome completo: ");
                String nomeCompleto = scanner.nextLine();

                System.out.print("Email: ");
                String email = scanner.nextLine();

                System.out.print("Senha: ");
                String senhaUsuario = scanner.nextLine();

                Cadastro cadastro = new Cadastro(
                        nomeUsuario,
                        nomeCompleto,
                        email,
                        senhaUsuario
                );

                boolean criado =
                        usuarioDAO.criarUsuario(cadastro);

                if (criado) {

                    System.out.println(
                            "\nPerfil criado com sucesso!"
                    );

                    System.out.println(cadastro);

                } else {

                    System.out.println(
                            "\nNão foi possível criar o perfil."
                    );

                    System.out.println(
                            "O nome de usuário pode já estar cadastrado."
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao conectar no banco: "
                            + e.getMessage()
            );

        } finally {

            scanner.close();
        }
    }
}