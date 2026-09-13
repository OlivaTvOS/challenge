import Card from '../../components/ui/Card';
import { IconeCategoria } from '../../components/ui/Icones';
import { categorias } from '../../data/categorias';

// Página Solução — explica a solução em si seguindo a narrativa:
// problema → SoulUp → missões → pontos → comportamento → personalização
export default function Solucao() {
  const passos = [
    {
      titulo: 'O problema',
      descricao:
        'O engajamento sustentável é baixo. As pessoas querem ajudar, mas não têm motivação nem feedback para manter o hábito.',
      cor: 'text-red-400',
      corFundo: 'bg-red-500/10',
    },
    {
      titulo: 'SoulUp',
      descricao:
        'Uma plataforma que transforma ações sustentáveis.',
      cor: 'text-aqua-400',
      corFundo: 'bg-aqua-500/10',
    },
    {
      titulo: 'Kairós',
      descricao: 'Implementa toda a lógica de gameficação e competitividade',
      cor: 'text-green-400',
      corFundo: 'bg-green-500/10',
    },
    {
      titulo: 'Missões',
      descricao:
        'O usuário escolhe missões em 6 categorias: Reciclagem, Mobilidade, Água, Energia, Natureza e Consumo.',
      cor: 'text-neon-400',
      corFundo: 'bg-neon-500/10',
    },
    {
      titulo: 'Pontos',
      descricao:
        'Ao concluir uma missão, o usuário ganha pontos de recompensa que se acumulam no seu perfil.',
      cor: 'text-yellow-400',
      corFundo: 'bg-yellow-500/10',
    },
    {
      titulo: 'Comportamento',
      descricao:
        'No futuro, uma IA (K-Means) vai analisar o padrão de ações do usuário e classificá-lo em um perfil comportamental (ex: "Guardião da Reciclagem").',
      cor: 'text-green-500',
      corFundo: 'bg-green-600/10',
    },
    {
      titulo: 'Personalização',
      descricao:
        'Com base no perfil comportamental, o SoulUp poderá sugerir missões personalizadas e recompensas específicas para cada usuário.',
      cor: 'text-orange-400',
      corFundo: 'bg-orange-500/10',
    },
  ];

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <h1 className="text-3xl sm:text-4xl font-bold text-dark-50 mb-2">A Solução</h1>
      <p className="text-dark-400 mb-10">
        Do problema à personalização — como o SoulUp resolve o desafio do engajamento sustentável
      </p>

      {/* Narrativa em etapas conectadas */}
      <div className="space-y-4">
        {passos.map((passo, index) => (
          <div key={index}>
            <Card className="flex gap-4 items-start">
              <div className={`w-12 h-12 rounded-xl ${passo.corFundo} ${passo.cor} flex items-center justify-center flex-shrink-0`}>
                <span className="text-lg font-bold">{index + 1}</span>
              </div>
              <div>
                <h3 className={`text-lg font-semibold ${passo.cor} mb-1`}>{passo.titulo}</h3>
                <p className="text-sm text-dark-400 leading-relaxed">{passo.descricao}</p>
              </div>
            </Card>
            {/* Seta conectora entre os passos */}
            {index < passos.length - 1 && (
              <div className="flex justify-center py-1">
                <svg className="w-5 h-5 text-dark-500" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <line x1="12" y1="5" x2="12" y2="19" />
                  <polyline points="19 12 12 19 5 12" />
                </svg>
              </div>
            )}
          </div>
        ))}
      </div>

      {/* Categorias em destaque */}
      <div className="mt-12">
        <h2 className="text-xl font-semibold text-dark-100 mb-6 text-center">Categorias de missão</h2>
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-4">
          {categorias.map((cat) => (
            <Card key={cat.nome} className="text-center">
              <div className={`w-10 h-10 rounded-lg ${cat.corFundo} ${cat.cor} flex items-center justify-center mx-auto mb-2`}>
                <IconeCategoria nome={cat.icone} className="w-5 h-5" />
              </div>
              <span className={`text-xs font-medium ${cat.cor}`}>{cat.label}</span>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}
