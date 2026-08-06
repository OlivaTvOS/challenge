const FRASES = {

    PLANTIO: [
        "Cada árvore plantada é uma promessa ao futuro de nosso planeta.",
        "Uma pequena muda hoje pode se tornar uma floresta inteira amanhã.",
        "Você acabou de plantar esperança verde na terra!"
    ],

    AGUA: [
        "Cada litro de água economizado é um litro de vida preservado.",
        "Água é ouro azul, e você ajudou a protegê-la!",
        "Cada gota economizada é uma gota para quem precisa."
    ],

    ENERGIA: [
        "Energia limpa começa com escolhas conscientes como a sua!",
        "Cada kilowatt economizado reduz sua pegada de carbono.",
        "Menos consumo = planeta mais limpo."
    ],

    RECICLAGEM: [
        "Reciclar é dar uma segunda vida aos materiais!",
        "Cada item reciclado é uma vitória contra a poluição.",
        "Reciclagem muda o mundo."
    ]
}

const MISSOES = {

    PLANTIO: [
        "Plantar uma árvore no bairro",
        "Cultivar uma pequena horta",
        "Cuidar de mudas para reflorestamento"
    ],

    AGUA: [
        "Tomar banho em até 5 minutos",
        "Fechar a torneira ao escovar os dentes",
        "Reutilizar água da chuva"
    ],

    ENERGIA: [
        "Apagar luzes desnecessárias",
        "Desligar aparelhos da tomada",
        "Utilizar iluminação natural"
    ],

    RECICLAGEM: [
        "Separar plástico reciclável",
        "Descartar lixo eletrônico corretamente",
        "Fazer compostagem orgânica"
    ]
}

function gerarNumero(maximo) {
    return Math.floor(Math.random() * maximo)
}

function gerarMissao(categoria) {

    const listaMissoes = MISSOES[categoria]
    const listaFrases = FRASES[categoria]

    const missao = listaMissoes[gerarNumero(listaMissoes.length)]
    const frase = listaFrases[gerarNumero(listaFrases.length)]

    const resultado = document.getElementById('resultado')

    resultado.innerHTML = `
                <h3>Categoria: ${categoria}</h3>

                <p><strong>Missão sugerida:</strong></p>
                <p>${missao}</p>

                <p class="frase-final">${frase}</p>
            `
}
