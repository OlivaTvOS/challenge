import type { InfoCategoria } from '../types';

// Dados das 6 categorias fixas do SoulUp
export const categorias: InfoCategoria[] = [
  {
    id: 1,
    nome: 'RECICLAGEM',
    label: 'Reciclagem',
    cor: 'text-aqua-400',
    corFundo: 'bg-aqua-500/10',
    icone: 'reciclagem',
    descricao: 'Ações de separação e descarte correto de resíduos recicláveis.',
  },
  {
    id: 2,
    nome: 'MOBILIDADE',
    label: 'Mobilidade',
    cor: 'text-neon-400',
    corFundo: 'bg-neon-500/10',
    icone: 'mobilidade',
    descricao: 'Uso de transporte sustentável como bicicleta e transporte público.',
  },
  {
    id: 3,
    nome: 'AGUA',
    label: 'Água',
    cor: 'text-blue-400',
    corFundo: 'bg-blue-500/10',
    icone: 'agua',
    descricao: 'Economia e reuso consciente de água no dia a dia.',
  },
  {
    id: 4,
    nome: 'ENERGIA',
    label: 'Energia',
    cor: 'text-yellow-400',
    corFundo: 'bg-yellow-500/10',
    icone: 'energia',
    descricao: 'Redução do consumo de energia elétrica e uso eficiente.',
  },
  {
    id: 5,
    nome: 'NATUREZA',
    label: 'Natureza',
    cor: 'text-green-500',
    corFundo: 'bg-green-600/10',
    icone: 'natureza',
    descricao: 'Contato, preservação e plantio de árvores e áreas verdes.',
  },
  {
    id: 6,
    nome: 'CONSUMO',
    label: 'Consumo',
    cor: 'text-orange-400',
    corFundo: 'bg-orange-500/10',
    icone: 'consumo',
    descricao: 'Consumo consciente e redução de desperdícios.',
  },
];
