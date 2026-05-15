import java.util.Arrays;
import java.util.Scanner;

public class bot {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // ========== CATEGORIA 1: PLANTIO & REFLORESTAMENTO ==========
        final String[] FRASES_PLANTIO = {
                "Cada árvore plantada é uma promessa ao futuro de nosso planeta.",
                "Plantar uma árvore é o começo de uma transformação ambiental que durará séculos.",
                "Uma pequena muda hoje pode se tornar uma floresta inteira amanhã.",
                "Você acabou de plantar esperança verde na terra!",
                "Que incrível! Mais uma árvore respirando ar puro para todos nós.",
                "Plantadores de árvores são verdadeiros heróis do planeta.",
                "Sua ação de hoje vai gerar sombra e vida para gerações futuras.",
                "Uma árvore plantada é um investimento eterno em sustentabilidade.",
                "Parabéns! Você fez parte da solução para o aquecimento global.",
                "Plantar é semear um mundo melhor para nossas crianças.",
                "Sua árvore vai absorver toneladas de CO2 ao longo de sua vida.",
                "Que privilégio ter participado dessa missão tão nobre!",
                "Cada árvore é um pulmão do planeta que você ajudou a restaurar.",
                "Florestas começam com gestos como o seu.",
                "Você não plantou apenas uma árvore, plantou um futuro sustentável.",
                "As árvores agradecem sua dedicação ao cuidado do meio ambiente.",
                "Uma árvore conecta o solo, o ar e a vida de forma perfeita.",
                "Sua missão ajuda a reverter a perda de biodiversidade global.",
                "Plantar é um ato de amor com a natureza.",
                "Cada muda é um símbolo de renovação e esperança.",
                "Você faz parte de um movimento global de reflorestamento!",
                "Árvores são verdadeiras máquinas de sequestro de carbono.",
                "Que belo trabalho pelo verde do nosso planeta.",
                "Sua árvore será abrigo para pássaros e pequenos animais.",
                "Plantadores de hoje são salvadores do amanhã!"
        };

        // ========== CATEGORIA 2: ECONOMIA DE ÁGUA & RECURSOS HÍDRICOS ==========
        final String[] FRASES_AGUA = {
                "Cada litro de água economizado é um litro de vida preservado.",
                "Água é ouro azul, e você ajudou a protegê-la!",
                "Parabéns por economizar água em seus compromissos diários.",
                "Sua atitude consciente ajuda a garantir água para as futuras gerações.",
                "Economia de água começa com pequenas atitudes como a sua.",
                "Você economizou uma quantidade significativa de água preciosa.",
                "Água limpa é um direito, não um luxo, obrigado por preservar!",
                "Seu compromisso com a água é um compromisso com a vida.",
                "Em tempos de escassez hídrica, sua economia faz real diferença.",
                "Cada gota economizada é uma gota para quem precisa.",
                "Você demonstrou consciência ambiental ao cuidar da água.",
                "Banhos rápidos, torneiras fechadas, vidas preservadas - obrigado!",
                "Conservar água é investir na sobrevivência do planeta.",
                "Sua ação hoje evita desperdícios para o futuro.",
                "A água agradece sua responsabilidade e cuidado.",
                "Você é um guardião responsável do recurso mais valioso da Terra.",
                "Economizar água é salvar vidas em regiões áridas.",
                "Seu gesto simples tem impacto global imenso.",
                "Água limpa e acessível depende de pessoas como você.",
                "Parabéns por escolher a sustentabilidade hídrica!",
                "Cada ação sua conta para um futuro com água abundante.",
                "Você entendeu que água não é infinita, e agiu com sabedoria.",
                "Seu compromisso hydra a esperança por um planeta mais verde.",
                "A escassez de água pode ser evitada com atitudes assim.",
                "Você é parte da solução para a crise hídrica global!"
        };

        // ========== CATEGORIA 3: ECONOMIA DE ENERGIA & SUSTENTABILIDADE ==========
        final String[] FRASES_ENERGIA = {
                "Energia limpa começa com escolhas conscientes como a sua!",
                "Você acabou de economizar eletricidade e dinheiro ao mesmo tempo.",
                "Cada kilowatt economizado reduz sua pegada de carbono.",
                "Apagar luzes desnecessárias é um gesto simples com grande impacto.",
                "Parabéns por usar energia de forma responsável!",
                "Sua economia de energia ajuda a reduzir emissões de carbono.",
                "Menos consumo = planeta mais limpo e bolso mais cheio.",
                "Você demonstrou eficiência energética genuína.",
                "Fontes renováveis dependem de consumidores conscientes como você.",
                "Economia de energia é economia de recursos naturais.",
                "Sua atitude reduz a demanda por combustíveis fósseis.",
                "Eficiência energética é inteligência ambiental em ação.",
                "Cada quarto com luz apagada é um passo rumo à sustentabilidade.",
                "Você escolheu o caminho do desenvolvimento sustentável.",
                "Energia economizada é energia preservada para o futuro.",
                "Seu consumo consciente inspira mudanças em toda a comunidade.",
                "Parabéns por pensar global ao agir local!",
                "Economia de energia protege recursos naturais preciosos.",
                "Você é um agente de mudança na transição energética global.",
                "Menos lixo elétrico, menos poluição, mais futuro.",
                "Sua responsabilidade energética beneficia gerações vindouras.",
                "Consumo consciente é uma virtude do século XXI.",
                "Você compreendeu que energia deve ser usada com sabedoria.",
                "Cada ação sua acelera a revolução da energia limpa.",
                "Sustentabilidade energética começa com pessoas como você!"
        };

        // ========== CATEGORIA 4: RECICLAGEM & GESTÃO DE RESÍDUOS ==========
        final String[] FRASES_RECICLAGEM = {
                "Reciclar é dar uma segunda vida aos materiais e ao planeta!",
                "Você acabou de evitar que lixo fosse desperdiçado em aterros.",
                "Parabéns por transformar resíduos em recursos valiosos.",
                "Reciclagem é o ciclo perfeito de responsabilidade ambiental.",
                "Cada item reciclado é uma vitória contra a poluição.",
                "Você compreendeu que lixo é recurso mal localizado.",
                "Recicladores são heróis silenciosos da sustentabilidade.",
                "Seu gesto reduz a extração de matérias-primas do planeta.",
                "Menos lixo em aterros, mais matéria-prima reutilizável.",
                "Parabéns por escolher a economia circular!",
                "Reciclagem cria empregos e preserva recursos naturais.",
                "Você evitou que materiais valiosos fossem perdidos para sempre.",
                "Gestão de resíduos começa com pessoas conscientes como você.",
                "Cada lata, cada garrafa reciclada faz diferença real.",
                "Sua dedicação à reciclagem reduz demanda de novas matérias-primas.",
                "Você é parte de uma revolução de zero desperdício!",
                "Reciclar é investir em um futuro limpo e próspero.",
                "Materiais reciclados geram novos produtos de qualidade.",
                "Parabéns por pensar em toda a cadeia de sustentabilidade.",
                "Reciclagem é responsabilidade que você assumiu com sucesso.",
                "Seu compromisso com resíduos impacta positivamente o planeta.",
                "Menos lixo no meio ambiente, mais esperança no futuro.",
                "Você compreendeu que resíduos são recursos valiosos.",
                "Reciclagem responsável é reciclagem que muda o mundo.",
                "Você é um agente ativo da economia circular do planeta!"
        };

        System.out.println(" ");
        String opcoes = sc.nextLine();

        switch (opcoes.toLowerCase()) {
            case "plantio":
                System.out.println(Arrays.toString(FRASES_PLANTIO));
            case "agua":
                System.out.println(Arrays.toString(FRASES_AGUA));
            case "energia":
                System.out.println(Arrays.toString(FRASES_ENERGIA));
            case "reciclagem":
                System.out.println(Arrays.toString(FRASES_RECICLAGEM));
        }
    }
}