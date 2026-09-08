package main.codes;

import minhas_funcoes.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static void main() {
        // Scanner gerenciado por try-with-resources (fecha automaticamente ao sair do
        // programa)
        try (Scanner scanner = new Scanner(System.in)) {
            boolean continuar = true;

            while (continuar) {
                // Até a 4 são exclusivamente funções para atender os requsitos de um CRUD
                System.out.println("\n===== MENU =====");

                System.out.println("0 - Sair"); // ✅ Feito

                System.out.println("1 - Criar perfil"); // ✅ Feito

                System.out.println("2 - Ver status do perfil"); // 👍 - Faltam Implemenações / Ver missões realizadas,
                                                                // tipos de interação, quantidade de interações
                                                                // (possivelmente com visualização de diferentes
                                                                // perídos)

                System.out.println("3 - Atualizar dados do perfil"); // Permite atualizar informações erradas digitadas
                                                                     // pelo usuário (somente com confirmação dos
                                                                     // dados).

                System.out.println("4 - Deletar perfil"); // Apenas deleta o perfil do banco com confirmação dos dados
                                                          // do usuário

                System.out.println("5 - Realizar missão"); // Realizar missões engloba período de testes, adição no
                                                           // ranking pós período, junção dos dados para geração do csv,
                                                           // realização contínua de missões pós avaliação, aumento no
                                                           // ranking, pontuação para cada missão

                System.out.println("6 - Ver postagens"); // Ver postagens abrange dar curtidas, comentar, ir para
                                                         // próxima ou parar de ver
                System.out.print("Escolha: ");

                try {
                    int opcao = scanner.nextInt();
                    scanner.nextLine();
                    // Utilizando a sintaxe moderna do Switch (Java 14+)
                    switch (opcao) {
                        case 0:
                            System.out.println("Saindo do sistema... Até logo!");
                            continuar = false;
                            break;
                        case 1:
                            funcoes.cadastrarUsuario(scanner);
                            break;
                        case 2:
                            System.out.println("Exibindo dados: \n");
                            funcoes.statusPerfil();
                            break;
                        case 3:
                            System.out.println("Alterar dados: \n");
                            funcoes.alterarDadosPerfil(scanner);
                            break;
                        case 5:
                            funcoes.realizarMissao(scanner);
                            break;
                        default:
                            System.out.println("Opção inválida! Tente novamente.");
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Números, não letras! " + e.getMessage());
                    scanner.nextLine(); // Limpa o buffer do teclado após ler o núme
                }

            }
        }
    }
}
