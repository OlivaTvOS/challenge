import java.util.Scanner;

public class Bot {
    private static int plantio_total_semana = 0;
    private static int agua_total_semana = 0;
    private static int energia_total_semana = 0;
    private static int reciclagem_total_semana = 0;

    private static final int limite_plantio = 5;
    private static final int limite_agua = 5;
    private static final int limite_energia = 5;
    private static final int limite_reciclagem = 5;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean continuar = true;

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║    BEM-VINDO AO BOT DE SUSTENTABILIDADE        ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        String nova_missao;
        do {
            System.out.println("\n========== ESCOLHA UMA MISSÃO PARA REALIZAR ==========\n");

            // Opção 1: Ver todas as missões
            System.out.println("1. Ver todas as missões (20 total)");

            // Opção 2: Ver por categoria
            System.out.println("2. Ver missões de PLANTIO");
            System.out.println("3. Ver missões de ÁGUA");
            System.out.println("4. Ver missões de ENERGIA");
            System.out.println("5. Ver missões de RECICLAGEM");

            // Opção 3: Realizar uma missão pelo ID
            System.out.println("6. Realizar uma missão (digite o ID)");
            System.out.println("0. Sair");

            System.out.println("Digite a categoria: ");
            String opcoes = sc.nextLine();

            switch (opcoes) {
                case "1":
                    GerenciadorMissoes.imprimirTodasAsMissoes();
                    break;
                case "2":
                    GerenciadorMissoes.imprimirMissoesPorCategoria("PLANTIO");
                    break;
                case "3":
                    GerenciadorMissoes.imprimirMissoesPorCategoria("AGUA");
                    break;
                case "4":
                    GerenciadorMissoes.imprimirMissoesPorCategoria("ENERGIA");
                    break;
                case "5":
                    GerenciadorMissoes.imprimirMissoesPorCategoria("RECICLAGEM");
                    break;
                case "6":
                    realizarMissao(sc);
                    break;
                case "0":
                    continuar = false;
                    System.out.println("\nValeu por ajudar o nosso planeta, te vejo de novo em breve!");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

        } while (continuar);

        sc.close();
    }
    private static void realizarMissao(Scanner sc){
        System.out.print("\nDigite o ID da missão que deseja realizar (1-20): ");

        String entrada = sc.nextLine().trim();

        if (!entrada.matches("\\d+")) {
            System.out.println("Entrada inválida! Digite apenas números.");
            return;
        }

        int missaoID = Integer.parseInt(entrada);

        Missao missao = GerenciadorMissoes.getMissaoPorId(missaoID);

        if (missao == null) {
            System.out.println("ID inválido!");
            return;
        }

        System.out.println("\n========== MISSÃO SELECIONADA ==========");
        System.out.println("ID: " + missao.getId());
        System.out.println("Título: " + missao.getTitulo());
        System.out.println("Categoria: " + missao.getCategoria());
        System.out.println("Descrição: " + missao.getDescricao());

        System.out.print("\nDeseja confirmar a realização dessa missão? (S/N): ");
        String confirmacao = sc.nextLine().trim().toUpperCase();

        if (!confirmacao.equals("S")) {
            System.out.println("Missão cancelada.");
            return;
        }
        processarMissao(missao);
    }

    private static void processarMissao(Missao missao){
        String cat = missao.getCategoria();
        FrasesArray fa = new FrasesArray();

        switch (cat) {
            case "PLANTIO":
                int plantioAleatorio = (int) (Math.random() * fa.getPlantio_cat().length);
                System.out.println(fa.getPlantio_cat()[plantioAleatorio]);

                if (plantio_total_semana < limite_plantio) {
                    plantio_total_semana++;
                    System.out.println("\nMissão de PLANTIO realizada!");
                    System.out.println("Total de missões PLANTIO esta semana: " + plantio_total_semana + "/" + limite_plantio);
                } else {
                    System.out.println("\nVocê já atingiu o limite de missões PLANTIO esta semana!");
                }
                break;
            case "AGUA":
                int aguaAleatorio = (int) (Math.random() * fa.getPlantio_cat().length);
                System.out.println(fa.getAgua_cat()[aguaAleatorio]);

                if (agua_total_semana < limite_agua) {
                    agua_total_semana ++;
                    System.out.println("\n Missão de AGUA realizada!");
                    System.out.println("Total de missões ÁGUA esta semana: " + agua_total_semana + "/" + limite_agua);
                } else {
                    System.out.println("\nVocê já atingiu o limite de missões ÁGUA esta semana!");
                }
                break;
            case "ENERGIA":
                int energiaAleatorio = (int) (Math.random() * fa.getPlantio_cat().length);
                System.out.println(fa.getEnergia_cat()[energiaAleatorio]);

                if (energia_total_semana < limite_energia) {
                    energia_total_semana ++;
                    System.out.println("\nMissão de ENERGIA realizada!");
                    System.out.println("Total de missões ENERGIA esta semana: " + energia_total_semana + "/" + limite_energia);
                } else {
                    System.out.println("\nVocê já atingiu o limite de missões ENERGIA esta semana!");
                }
                break;
            case "RECICLAGEM":
                int reciclagemAleatorio = (int) (Math.random() * fa.getPlantio_cat().length);
                System.out.println(fa.getReciclagem_cat()[reciclagemAleatorio]);

                if (reciclagem_total_semana < limite_reciclagem) {
                    reciclagem_total_semana ++;
                    System.out.println("\nMissão de RECICLAGEM realizada!");
                    System.out.println("Total de missões RECICLAGEM esta semana: " + reciclagem_total_semana + "/" + limite_reciclagem);
                } else {
                    System.out.println("\nVocê já atingiu o limite de missões RECICLAGEM esta semana!");
                }
                break;
        }
    }
}