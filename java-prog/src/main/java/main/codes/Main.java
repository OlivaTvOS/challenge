package main.codes;

import java.util.InputMismatchException;
import java.util.Scanner;

import funcionario.CargoFuncionario;
import funcionario.SessaoFuncionario;
import funcoes.Funcoes;
import model.SessaoUsuario;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean continuar = true;
            while (continuar) {
                try {
                    if (SessaoFuncionario.getInstance().estaLogado()) {
                        menuFuncionario(scanner);
                    } else if (SessaoUsuario.getInstance().temUsuarioAtivo()) {
                        menuUsuario(scanner);
                    } else {
                        continuar = menuInicial(scanner);
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Digite apenas números.");
                    scanner.nextLine();
                }
            }
        }
    }

    private static boolean menuInicial(Scanner scanner) {
        System.out.println("\n===== MENU INICIAL =====");
        System.out.println("0 - Sair");
        System.out.println("1 - Criar perfil de usuário");
        System.out.println("2 - Entrar como usuário");
        System.out.println("3 - Entrar como funcionário");
        System.out.println("4 - Cadastrar funcionário (demonstração acadêmica)");
        System.out.print("\nEscolha: ");
        int opcao = scanner.nextInt(); scanner.nextLine();
        switch (opcao) {
            case 0:
                System.out.println("Saindo do sistema... Até logo!"); 
                return false;
            case 1:
                 Funcoes.cadastrarUsuario(scanner);
                 break;
            case 2:
                 Funcoes.logarPerfil(scanner);
                 break;
            case 3:
                 Funcoes.logarFuncionario(scanner);
                 break;
            case 4:
                 Funcoes.cadastrarFuncionario(scanner);
                 break;
            default:
                 System.out.println("Opção inválida.");
                 break;
        }
        return true;
    }

    private static void menuUsuario(Scanner scanner) {
        System.out.println("\n===== MENU DO USUÁRIO =====");
        System.out.println("0 - Logout\n1 - Ver status\n2 - Atualizar dados\n3 - Excluir perfil\n4 - Realizar missão\n5 - Ver postagens");
        System.out.print("Escolha: ");
        int opcao = scanner.nextInt(); scanner.nextLine();
        switch (opcao) {
            case 0:
                 Funcoes.logoutUsuario();
                 break;
            case 1:
                 Funcoes.statusPerfil();
                 break;
            case 2:
                 Funcoes.alterarDadosPerfil(scanner);
                 break;
            case 3: 
                Funcoes.removerPerfil(scanner);
                break;
            case 4: 
                Funcoes.realizarMissao(scanner);
                break;
            case 5:
                 System.out.println("Sistema de postagens ainda não implementado.");
                 break;
            default:
                 System.out.println("Opção inválida.");
                 break;
        }
    }

    private static void menuFuncionario(Scanner scanner) {
        CargoFuncionario cargo = SessaoFuncionario.getInstance().getFuncionario().getCargo();
        System.out.println("\n===== MENU DO FUNCIONÁRIO - " + cargo + " =====");
        System.out.println("0 - Logout\n1 - Consultar usuários");
        if (cargo == CargoFuncionario.ADMIN) System.out.println("2 - Editar dados básicos de usuário\n3 - Excluir usuário\n4 - Consultar funcionários\n5 - Visualizar resumo");
        if (cargo == CargoFuncionario.TECH) System.out.println("2 - Editar dados básicos de usuário");
        if (cargo == CargoFuncionario.CEO) System.out.println("2 - Visualizar resumo");
        System.out.print("Escolha: ");
        int opcao = scanner.nextInt(); scanner.nextLine();
        if (opcao == 0) Funcoes.logoutFuncionario();
        else if (opcao == 1) Funcoes.consultarUsuariosFuncionario();
        else if (opcao == 2 && (cargo == CargoFuncionario.ADMIN || cargo == CargoFuncionario.TECH)) Funcoes.editarUsuarioFuncionario(scanner);
        else if (opcao == 3 && cargo == CargoFuncionario.ADMIN) Funcoes.excluirUsuarioFuncionario(scanner);
        else if (opcao == 4 && cargo == CargoFuncionario.ADMIN) Funcoes.consultarFuncionarios();
        else if (opcao == 5 && cargo == CargoFuncionario.ADMIN) Funcoes.consultarResumo();
        else if (opcao == 2 && cargo == CargoFuncionario.CEO) Funcoes.consultarResumo();
        else System.out.println("Opção inválida ou acesso negado.");
    }
}
