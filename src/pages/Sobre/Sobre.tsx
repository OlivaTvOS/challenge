import Card from '../../components/ui/Card';

// Página Sobre — explica o projeto e o contexto acadêmico
export default function Sobre() {
  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <h1 className="text-3xl sm:text-4xl font-bold text-dark-50 mb-6">Sobre o SoulUp</h1>

      <div className="space-y-6">
        <Card>
          <h2 className="text-xl font-semibold text-aqua-400 mb-3">O que é?</h2>
          <p className="text-dark-300 leading-relaxed">
            A Kairós implementa um sistema de gamificação baseado em níveis, consistência e interações. A ideia é simples:
            transformar ações do dia a dia que ajudam o planeta em missões com pontos, utilizando de um sistema de
            ranking, conquistas e progresso. Assim, o engajamento na plataforma deixa de ser algo abstrato
            e passa a ser divertido e recompensador.

            Além da lógica retrabalhada, esta em desenvolvimento uma IA que análisa o comportamento dos usuários,
            utilizando não somente de base as missões realizadas pelo usuário, mas também suas interaçõe e comportamentos
            na rede, assim criando 'relatórios' de desempenho atingidos pelo mesmo na semana ou mês em questão. Isto cria uma
            conexão com usuário, não se limitando a interações somente com outros da plataforma, mas com a própria lhe
            sinalizando seu progresso e se comunicando mais 'diretamente', sem precisaranalisar um a um
          </p>
        </Card>

        <Card>
          <h2 className="text-xl font-semibold text-aqua-400 mb-3">Contexto acadêmico</h2>
          <p className="text-dark-300 leading-relaxed">
            Este projeto esta em desenvolvimento durante o 1º ano do curso de Análise e Desenvolvimento
            de Sistemas (ADS), como parte das entregas acadêmicas. O objetivo é utilizar
            conceitos de desenvolvimento web front-end com React, TypeScript e Tailwind CSS,
            além de aplicar práticas de organização de código e tipagem.
          </p>
        </Card>

        <Card>
          <h2 className="text-xl font-semibold text-aqua-400 mb-3">A proposta</h2>
          <p className="text-dark-300 leading-relaxed">
            A Kairós propõe a realização das missões que por sua vez, determinam a posição do usuário no ranking,
            criando um senso de competição saudável e engajamento contínuo.
          </p>
        </Card>

        <Card>
          <h2 className="text-xl font-semibold text-aqua-400 mb-3">Visão de futuro</h2>
          <p className="text-dark-300 leading-relaxed">
            No futuro, a Kairós implementará visualmente as conquistas, recompensas, um feed social onde usuários
            compartilham suas ações, e um perfil comportamental gerado por IA que classifica
            o padrão de cada usuário (ex: "Guardião da Reciclagem"). Essas funcionalidades
            fazem parte da visão maior do produto e serão desenvolvidas na próxima etapa.
          </p>
        </Card>
      </div>
    </div>
  );
}
