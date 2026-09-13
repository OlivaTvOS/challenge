import type { Missao } from '../types';

// As 30 missões fixas do SoulUp — idênticas ao back-end (Python/SQL)
export const seedMissoes: Missao[] = [
  // RECICLAGEM (ids 1-5)
  { id: 1, categoria: 'RECICLAGEM', titulo: 'Separe seus recicláveis', descricao: 'Separe papel, plástico, vidro e metal em recipientes distintos para facilitar a coleta seletiva.', pontosRecompensa: 30 },
  { id: 2, categoria: 'RECICLAGEM', titulo: 'Leve recicláveis a um ponto de coleta', descricao: 'Reúna seus recicláveis e leve a um ponto de coleta mais próximo da sua casa.', pontosRecompensa: 60 },
  { id: 3, categoria: 'RECICLAGEM', titulo: 'Recicle uma embalagem de vidro', descricao: 'Lave e destine corretamente uma embalagem de vidro para reciclagem.', pontosRecompensa: 40 },
  { id: 4, categoria: 'RECICLAGEM', titulo: 'Separe resíduos eletrônicos', descricao: 'Separe pilhas, baterias e pequenos eletrônicos para descarte em local adequado.', pontosRecompensa: 80 },
  { id: 5, categoria: 'RECICLAGEM', titulo: 'Monte um ponto de separação em casa', descricao: 'Crie um espaço organizado em casa com lixeiras para cada tipo de reciclável.', pontosRecompensa: 100 },
  // MOBILIDADE (ids 6-10)
  { id: 6, categoria: 'MOBILIDADE', titulo: 'Use transporte público', descricao: 'Substitua o carro particular por transporte público em um trajeto do dia.', pontosRecompensa: 50 },
  { id: 7, categoria: 'MOBILIDADE', titulo: 'Faça um trajeto de bicicleta', descricao: 'Pedale em vez de usar carro ou ônibus para um trajeto curto do dia.', pontosRecompensa: 70 },
  { id: 8, categoria: 'MOBILIDADE', titulo: 'Caminhe em um trajeto curto', descricao: 'Vá a pé em um trajeto que você faria de carro ou transporte.', pontosRecompensa: 50 },
  { id: 9, categoria: 'MOBILIDADE', titulo: 'Compartilhe uma rota sustentável', descricao: 'Compartilhe uma rota com amigos ou colegas para reduzir o número de veículos.', pontosRecompensa: 35 },
  { id: 10, categoria: 'MOBILIDADE', titulo: 'Desafio de mobilidade da semana', descricao: 'Use apenas transporte sustentável (bicicleta, a pé ou público) durante uma semana inteira.', pontosRecompensa: 140 },
  // AGUA (ids 11-15)
  { id: 11, categoria: 'AGUA', titulo: 'Banho consciente', descricao: 'Reduza o tempo de banho para no máximo 5 minutos e feche a torneira enquanto se ensaboa.', pontosRecompensa: 30 },
  { id: 12, categoria: 'AGUA', titulo: 'Feche a torneira', descricao: 'Não deixe a torneira aberta enquanto escova os dentes ou lava a louça.', pontosRecompensa: 20 },
  { id: 13, categoria: 'AGUA', titulo: 'Reutilize água', descricao: 'Reaproveite a água da máquina de lavar ou do banho para regar plantas ou limpar o quintal.', pontosRecompensa: 60 },
  { id: 14, categoria: 'AGUA', titulo: 'Verifique vazamentos', descricao: 'Inspecione torneiras, canos e vasos sanitários em casa e conserte qualquer vazamento.', pontosRecompensa: 80 },
  { id: 15, categoria: 'AGUA', titulo: 'Desafio de economia de água', descricao: 'Reduza o consumo de água da sua casa em pelo menos 20% durante uma semana inteira.', pontosRecompensa: 130 },
  // ENERGIA (ids 16-20)
  { id: 16, categoria: 'ENERGIA', titulo: 'Apague luzes desnecessárias', descricao: 'Apague as luzes de cômodos vazios e aproveite a luz natural sempre que possível.', pontosRecompensa: 20 },
  { id: 17, categoria: 'ENERGIA', titulo: 'Desligue um aparelho', descricao: 'Desligue da tomada um aparelho que não está em uso para evitar o consumo fantasma.', pontosRecompensa: 25 },
  { id: 18, categoria: 'ENERGIA', titulo: 'Use luz natural', descricao: 'Abra as janelas e use a luz do sol em vez de acender lâmpadas durante o dia.', pontosRecompensa: 30 },
  { id: 19, categoria: 'ENERGIA', titulo: 'Reduza o uso do ar-condicionado', descricao: 'Diminua o uso do ar-condicionado ou ajuste a temperatura para 24°C ou mais.', pontosRecompensa: 70 },
  { id: 20, categoria: 'ENERGIA', titulo: 'Desafio de economia de energia', descricao: 'Mantenha o consumo de energia da casa no mínimo durante uma semana inteira com hábitos eficientes.', pontosRecompensa: 130 },
  // NATUREZA (ids 21-25)
  { id: 21, categoria: 'NATUREZA', titulo: 'Cuide de uma planta', descricao: 'Regue, adube e cuide de uma planta em casa ou no trabalho por uma semana.', pontosRecompensa: 30 },
  { id: 22, categoria: 'NATUREZA', titulo: 'Plante uma muda', descricao: 'Plante uma muda de árvore ou horta em um local adequado e registre o plantio.', pontosRecompensa: 100 },
  { id: 23, categoria: 'NATUREZA', titulo: 'Participe de uma ação verde', descricao: 'Participe de um mutirão de plantio, limpeza ou preservação ambiental na sua comunidade.', pontosRecompensa: 120 },
  { id: 24, categoria: 'NATUREZA', titulo: 'Limpe uma área verde', descricao: 'Recolha lixo de uma praça, parque ou área verde perto de você.', pontosRecompensa: 90 },
  { id: 25, categoria: 'NATUREZA', titulo: 'Desafio de cuidado com a natureza', descricao: 'Durante uma semana, realize uma ação de cuidado com a natureza todos os dias.', pontosRecompensa: 150 },
  // CONSUMO (ids 26-30)
  { id: 26, categoria: 'CONSUMO', titulo: 'Evite um descartável', descricao: 'Evite usar copos, talheres ou canudos descartáveis em um dia. Leve os seus reutilizáveis.', pontosRecompensa: 25 },
  { id: 27, categoria: 'CONSUMO', titulo: 'Use uma sacola reutilizável', descricao: 'Leve sua própria sacola reutilizável ao fazer compras e recuse sacolas plásticas.', pontosRecompensa: 25 },
  { id: 28, categoria: 'CONSUMO', titulo: 'Planeje uma compra', descricao: 'Faça uma lista de compras e compre apenas o necessário para evitar desperdício.', pontosRecompensa: 40 },
  { id: 29, categoria: 'CONSUMO', titulo: 'Reaproveite um objeto', descricao: 'Reaproveite ou conserte um objeto em vez de descartá-lo e comprar novo.', pontosRecompensa: 60 },
  { id: 30, categoria: 'CONSUMO', titulo: 'Desafio de consumo consciente', descricao: 'Durante uma semana, evite compras desnecessárias e priorize produtos sustentáveis.', pontosRecompensa: 120 },
];
