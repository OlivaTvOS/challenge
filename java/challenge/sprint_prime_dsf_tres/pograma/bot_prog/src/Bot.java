import java.util.Scanner;

public class Bot {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FrasesArray fa = new FrasesArray();

        int missao_plantio_limite = 3;
        int plantio_total_semana = 0;
        int missao_agua_limite = 3;
        int agua_total_semana = 0;
        int missao_energia_limite = 3;
        int energia_total_semana = 0;
        int missao_reciclagem_limite = 3;
        int reciclagem_total_semana = 0;

        System.out.println("Digite a categoria: ");
        String opcoes = sc.nextLine();

        String nova_missao;
        do {
            System.out.println("Temos 5 missões disponíveis hoje: ");
            System.out.println("Plantar uma árvore");
            System.out.println("Reuso e economia na rotina:");
            System.out.println("Iluminação eficiente e aparelhos em stand-by");
            System.out.println("Descarte correto de lixo eletrônico e óleo");



            switch (opcoes.toLowerCase()) {
                case "plantio":
                    int plantioAleatorio = (int) (Math.random() * fa.getPlantio_cat().length);
                    System.out.println(fa.getPlantio_cat()[plantioAleatorio]);

                    while (true) {
                        if (plantio_total_semana < missao_plantio_limite) {
                            plantio_total_semana += 1;
                            break;
                        }
                    }
                    break;
                case "agua", "água":
                    int aguaAleatorio = (int) (Math.random() * fa.getPlantio_cat().length);
                    System.out.println(fa.getAgua_cat()[aguaAleatorio]);

                    while (true) {
                        if (agua_total_semana < missao_agua_limite) {
                            agua_total_semana += 1;
                            break;
                        }
                    }
                    break;
                case "energia":
                    int energiaAleatorio = (int) (Math.random() * fa.getPlantio_cat().length);
                    System.out.println(fa.getEnergia_cat()[energiaAleatorio]);

                    while (true) {
                        if (energia_total_semana < missao_energia_limite) {
                            energia_total_semana += 1;
                            break;
                        }
                    }
                    break;
                case "reciclagem":
                    int reciclagemAleatorio = (int) (Math.random() * fa.getPlantio_cat().length);
                    System.out.println(fa.getReciclagem_cat()[reciclagemAleatorio]);

                    while (true) {
                        if (reciclagem_total_semana < missao_reciclagem_limite) {
                            reciclagem_total_semana += 1;
                            break;
                        }
                    }
                    break;
            }
            System.out.println("Deseja realizar outra das missões restantes?: ");
            nova_missao = sc.nextLine();

        } while (nova_missao.equals("sim"));

        System.out.println("Volte a qualquer momento para realizar as outras missões restantes. \nLembre-se, ajudar o planeta sempre!");
    }
}