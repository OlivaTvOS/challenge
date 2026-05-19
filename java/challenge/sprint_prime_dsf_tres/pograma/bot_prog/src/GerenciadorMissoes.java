public class GerenciadorMissoes {

    // Array principal contendo todas
    // as missões disponíveis no sistema.
    public static final Missao[] MISSOES_DISPONIVEIS = {
            // ========== PLANTIO (IDs 1-5) ==========
            new Missao(1, "Plantar uma árvore", "PLANTIO",
                    "Plante uma árvore em seu bairro ou quintal"),
            new Missao(2, "Plantar flores silvestres", "PLANTIO",
                    "Plante flores silvestres para atrair polinizadores"),
            new Missao(3, "Cultivar uma horta", "PLANTIO",
                    "Comece uma pequena horta em casa"),
            new Missao(4, "Cuidar de mudas", "PLANTIO",
                    "Cuide de mudas para futuro plantio"),
            new Missao(5, "Reflorestamento comunitário", "PLANTIO",
                    "Participe de ação de reflorestamento comunitário"),

            // ========== ÁGUA (IDs 6-10) ==========
            new Missao(6, "Banho rápido (5 min)", "AGUA",
                    "Tome banho em máximo 5 minutos"),
            new Missao(7, "Fechar torneira ao escovar dentes", "AGUA",
                    "Não deixe a torneira aberta enquanto escova os dentes"),
            new Missao(8, "Reutilizar água de chuva", "AGUA",
                    "Coletar e reutilizar água de chuva para regar plantas"),
            new Missao(9, "Lavar carro com balde", "AGUA",
                    "Lave o carro usando balde em vez de mangueira"),
            new Missao(10, "Revisar vazamentos", "AGUA",
                    "Procure por vazamentos em casa e reporte"),

            // ========== ENERGIA (IDs 11-15) ==========
            new Missao(11, "Apagar luzes desnecessárias", "ENERGIA",
                    "Apague todas as luzes que não estão sendo usadas"),
            new Missao(12, "Desligar aparelhos em stand-by", "ENERGIA",
                    "Desconecte aparelhos em modo stand-by da tomada"),
            new Missao(13, "Usar luz natural", "ENERGIA",
                    "Passe um dia usando apenas luz natural"),
            new Missao(14, "Reduzir uso de ar condicionado", "ENERGIA",
                    "Aumente a temperatura em 2°C ou use ar condicionado menos"),
            new Missao(15, "Andar ou andar de bicicleta", "ENERGIA",
                    "Vá a um lugar a pé ou de bicicleta em vez de carro"),

            // ========== RECICLAGEM (IDs 16-20) ==========
            new Missao(16, "Reciclar papéis e papelão", "RECICLAGEM",
                    "Separe e recicle papéis e papelão"),
            new Missao(17, "Reciclar vidro", "RECICLAGEM",
                    "Coletar e descartar vidro adequadamente"),
            new Missao(18, "Reciclar plástico", "RECICLAGEM",
                    "Separe plásticos para reciclagem"),
            new Missao(19, "Descartar lixo eletrônico", "RECICLAGEM",
                    "Leve eletrônicos antigos para postos de coleta"),
            new Missao(20, "Compostar resíduos orgânicos", "RECICLAGEM",
                    "Inicie compostagem de resíduos de alimentos")
    };

    public static Missao getMissaoPorId(int id){
        if (id < 1 || id > MISSOES_DISPONIVEIS.length){
            return null;
        }
        return MISSOES_DISPONIVEIS[id - 1];
    }

    public static Missao[] getMissoesPorCategoria(String categoria) {
        int contador = 0;
        // Conta quantas missões existem
        // na categoria solicitada.
        for (Missao m : MISSOES_DISPONIVEIS) {
            if (m.getCategoria().equals(categoria.toUpperCase())) {
                contador++;
            }
        }

        Missao[] resultado = new Missao[contador];
        int indice = 0;
        // Preenche o novo array apenas
        // com missões da categoria desejada.
        for (Missao m : MISSOES_DISPONIVEIS) {
            if (m.getCategoria().equals(categoria.toUpperCase())) {
                resultado[indice] = m;
                indice++;
            }
        }

        return resultado;
    }

    public static void imprimirTodasAsMissoes() {
        System.out.println("\n========== TODAS AS MISSÕES DISPONÍVEIS ==========\n");
        for (Missao m : MISSOES_DISPONIVEIS) {
            System.out.println(m);
            System.out.println("   Descrição: " + m.getDescricao());
            System.out.println();
        }
    }

    public static void imprimirMissoesPorCategoria(String categoria) {
        Missao[] missoes = getMissoesPorCategoria(categoria);
        System.out.println("\n========== MISSÕES DE " + categoria.toUpperCase() + " ==========\n");
        for (Missao m : missoes) {
            System.out.println(m);
            System.out.println("   Descrição: " + m.getDescricao());
            System.out.println();
        }
    }
}
