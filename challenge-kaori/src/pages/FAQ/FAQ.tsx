import { useState } from 'react';
import Card from '../../components/ui/Card';

// Pergunta e resposta do FAQ
interface PerguntaFAQ {
  pergunta: string;
  resposta: string;
}

const perguntas: PerguntaFAQ[] = [
  {
    pergunta: 'O que é a Kairós?',
    resposta:
      'SoulUp é uma plataforma que usa gamificação para incentivar ações sustentáveis. Você realiza missões, ganha pontos e sobe no ranking.',
  },
  {
    pergunta: 'Como funcionam as missões?',
    resposta:
      'As missões são ações sustentáveis divididas em 6 categorias: Reciclagem, Mobilidade, Água, Energia, Natureza e Consumo. Você escolhe uma missão, inicia, realiza a ação e marca como concluída para ganhar pontos.',
  },
  {
    pergunta: 'Como ganho pontos?',
    resposta:
      'Cada missão tem um valor de pontos de recompensa. Ao concluir uma missão, os pontos são somados ao seu total e refletem na sua posição no ranking.',
  },
  {
    pergunta: 'O que é o ranking?',
    resposta:
      'O ranking é a classificação dos usuários baseada em pontos. Quanto mais missões você concluir, mais pontos acumula e mais alto sobe no ranking.',
  },
  {
    pergunta: 'O ranking é o mesmo que o perfil comportamental?',
    resposta:
      'Não. O ranking mostra sua posição baseada em pontos. O perfil comportamental é uma classificação gerada por IA (algoritmo K-Means) que identifica seu padrão de ação (ex: "Guardião da Reciclagem"). São coisas diferentes.',
  },
  {
    pergunta: 'Preciso pagar para usar?',
    resposta:
      'Não. O SoulUp é um projeto acadêmico e gratuito para uso. Basta criar uma conta e começar a realizar missões.',
  },
  {
    pergunta: 'Meus dados são salvos?',
    resposta:
      'Nesta etapa de demonstração do front-end, os dados não são persistidos. Tudo funciona na sessão do navegador. No futuro, os dados serão salvos por um backend real.',
  },
  {
    pergunta: 'Quais tecnologias são usadas?',
    resposta:
      'O front-end usa React, Vite, TypeScript, Tailwind CSS, React Router DOM e React Hook Form. O backend (futuro) será em Java.',
  },
];

// Página FAQ — lista de perguntas frequentes com acordeão
export default function FAQ() {
  const [aberta, setAberta] = useState<number | null>(0);

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <h1 className="text-3xl sm:text-4xl font-bold text-dark-50 mb-2">Perguntas Frequentes</h1>
      <p className="text-dark-400 mb-8">Tire suas dúvidas sobre o SoulUp</p>

      <div className="space-y-3">
        {perguntas.map((item, index) => (
          <Card key={index} className="p-0 overflow-hidden">
            {/* Botão da pergunta */}
            <button
              onClick={() => setAberta(aberta === index ? null : index)}
              className="w-full flex items-center justify-between p-5 text-left hover:bg-white/5 transition-colors"
            >
              <span className="text-sm font-medium text-dark-100">{item.pergunta}</span>
              <svg
                className={`w-5 h-5 text-aqua-400 flex-shrink-0 transition-transform duration-300 ${aberta === index ? 'rotate-180' : ''}`}
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <polyline points="6 9 12 15 18 9" />
              </svg>
            </button>
            {/* Resposta — só mostra quando a pergunta está aberta */}
            {aberta === index && (
              <div className="px-5 pb-5 animate-fade-in">
                <p className="text-sm text-dark-400 leading-relaxed">{item.resposta}</p>
              </div>
            )}
          </Card>
        ))}
      </div>
    </div>
  );
}
