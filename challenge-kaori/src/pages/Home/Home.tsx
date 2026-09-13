import { Link } from 'react-router-dom';
import Button from '../../components/ui/Button';
import Card from '../../components/ui/Card';
import { categorias } from '../../data/categorias';
import { IconeCategoria } from '../../components/ui/Icones';

// Página inicial — apresenta o SoulUp
export default function Home() {
  return (
    <div className="animate-fade-in">
      {/* Seção hero */}
      <section className="relative overflow-hidden py-20 sm:py-28">
        {/* Efeito de fundo com gradientes */}
        <div className="absolute inset-0 bg-gradient-to-b from-aqua-500/5 via-transparent to-transparent" />
        <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[600px] h-[600px] bg-aqua-500/10 rounded-full blur-[120px] pointer-events-none" />

        <div className="relative max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <span className="inline-block px-4 py-1.5 rounded-full text-xs font-medium text-aqua-300 bg-aqua-500/10 border border-aqua-500/20 mb-6">
            Gamificação da Sustabilidade
          </span>
          <h1 className="text-4xl sm:text-5xl lg:text-6xl font-bold text-dark-50 mb-6 leading-tight">
            Transforme ações sustentáveis em{' '}
            <span className="text-aqua-400 neon-text">pontos e conquistas</span>
          </h1>
          <p className="text-lg text-dark-300 mb-8 max-w-2xl mx-auto">
            O SoulUp gamifica o engajamento sustentável: realize missões, ganhe pontos,
            suba no ranking e construa um planeta melhor — de forma divertida.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link to="/login">
              <Button variante="primario" className="w-full sm:w-auto">Entrar na plataforma</Button>
            </Link>
            <Link to="/cadastro">
              <Button variante="secundario" className="w-full sm:w-auto">Criar conta grátis</Button>
            </Link>
          </div>
        </div>
      </section>

      {/* Seção: o problema e a proposta */}
      <section className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Card className="border-red-500/10">
            <div className="w-12 h-12 rounded-xl bg-red-500/10 flex items-center justify-center mb-4">
              <svg className="w-6 h-6 text-red-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <circle cx="12" cy="12" r="10" />
                <line x1="12" y1="8" x2="12" y2="12" />
                <line x1="12" y1="16" x2="12.01" y2="16" />
              </svg>
            </div>
            <h3 className="text-xl font-semibold text-dark-100 mb-2">O problema</h3>
            <p className="text-sm text-dark-400">
              O engajamento da população em práticas sustentáveis é baixo. As pessoas sabem que
              precisam agir, mas falta motivação e feedback concreto para manter o hábito.
            </p>
          </Card>

          <Card className="border-aqua-500/20">
            <div className="w-12 h-12 rounded-xl bg-aqua-500/10 flex items-center justify-center mb-4">
              <svg className="w-6 h-6 text-aqua-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2" />
              </svg>
            </div>
            <h3 className="text-xl font-semibold text-dark-100 mb-2">A proposta</h3>
            <p className="text-sm text-dark-400">
              Usar gamificação para transformar ações sustentáveis em missões com pontos,
              ranking e conquistas. Tornar o ato de cuidar do planeta divertido e recompensador.
            </p>
          </Card>
        </div>
      </section>

      {/* Seção: como funciona */}
      <section className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <h2 className="text-2xl sm:text-3xl font-bold text-dark-50 text-center mb-12">
          Como funciona
        </h2>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
          {/* Passo 1 */}
          <Card className="text-center">
            <div className="w-14 h-14 rounded-2xl bg-aqua-500/10 flex items-center justify-center mx-auto mb-4">
              <span className="text-2xl font-bold text-aqua-400">1</span>
            </div>
            <h3 className="text-lg font-semibold text-dark-100 mb-2">Escolha uma missão</h3>
            <p className="text-sm text-dark-400">
              Selecione uma missão sustentável entre as categorias disponíveis.
            </p>
          </Card>
          {/* Passo 2 */}
          <Card className="text-center">
            <div className="w-14 h-14 rounded-2xl bg-aqua-500/10 flex items-center justify-center mx-auto mb-4">
              <span className="text-2xl font-bold text-aqua-400">2</span>
            </div>
            <h3 className="text-lg font-semibold text-dark-100 mb-2">Conclua e ganhe pontos</h3>
            <p className="text-sm text-dark-400">
              Realize a missão, marque como concluída e acumule pontos de recompensa.
            </p>
          </Card>
          {/* Passo 3 */}
          <Card className="text-center">
            <div className="w-14 h-14 rounded-2xl bg-aqua-500/10 flex items-center justify-center mx-auto mb-4">
              <span className="text-2xl font-bold text-aqua-400">3</span>
            </div>
            <h3 className="text-lg font-semibold text-dark-100 mb-2">Suba no ranking</h3>
            <p className="text-sm text-dark-400">
              Seus pontos refletem no ranking. Compare seu desempenho com outros usuários.
            </p>
          </Card>
        </div>
      </section>

      {/* Seção: categorias */}
      <section className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <h2 className="text-2xl sm:text-3xl font-bold text-dark-50 text-center mb-12">
          Categorias de missão
        </h2>
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-4">
          {categorias.map((cat) => (
            <Card key={cat.nome} className="text-center hover:scale-105">
              <div className={`w-12 h-12 rounded-xl ${cat.corFundo} ${cat.cor} flex items-center justify-center mx-auto mb-3`}>
                <IconeCategoria nome={cat.icone} className="w-6 h-6" />
              </div>
              <h3 className={`text-sm font-medium ${cat.cor}`}>{cat.label}</h3>
            </Card>
          ))}
        </div>
      </section>

      {/* CTA final */}
      <section className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-20 text-center">
        <Card className="border-aqua-500/20 neon-border">
          <h2 className="text-2xl sm:text-3xl font-bold text-dark-50 mb-4">
            Pronto para começar?
          </h2>
          <p className="text-dark-300 mb-6">
            Crie sua conta e comece a realizar missões sustentáveis hoje mesmo.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link to="/cadastro">
              <Button variante="primario" className="w-full sm:w-auto">Criar conta grátis</Button>
            </Link>
            <Link to="/solucao">
              <Button variante="secundario" className="w-full sm:w-auto">Conhecer a solução</Button>
            </Link>
          </div>
        </Card>
      </section>
    </div>
  );
}
