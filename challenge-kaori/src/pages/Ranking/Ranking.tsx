import { useAppData } from '../../core/AppDataContext';
import Card from '../../components/ui/Card';

// Página de Ranking — lista real de usuários ordenada por pontos (estilo Duolingo)
export default function Ranking() {
  const { usuarioLogado, listarRanking } = useAppData();
  const ranking = listarRanking();

  // Cores para as 3 primeiras posições (pódio)
  const coresPodio = [
    'text-yellow-400 bg-yellow-500/10 border-yellow-500/30', // 1º lugar — ouro
    'text-dark-200 bg-dark-600/20 border-dark-400/30',       // 2º lugar — prata
    'text-orange-400 bg-orange-500/10 border-orange-500/30',  // 3º lugar — bronze
  ];

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <h1 className="text-3xl sm:text-4xl font-bold text-dark-50 mb-2">Ranking</h1>
      <p className="text-dark-400 mb-8">
        Classificação dos usuários por pontos — compita e suba na lista!
      </p>

      {/* Pódio — destaque para os 3 primeiros (2º, 1º, 3º visualmente) */}
      <div className="grid grid-cols-3 gap-3 mb-8 items-end">
        {/* 2º lugar */}
        {ranking[1] && (
          <Card className={`text-center border ${coresPodio[1]} sm:mb-4`}>
            <div className="text-3xl mb-2">🥈</div>
            <p className="text-sm font-semibold text-dark-100 truncate">{ranking[1].usuario}</p>
            <p className="text-xs text-dark-400 mt-1">{ranking[1].pontos} pts</p>
          </Card>
        )}
        {/* 1º lugar — mais alto */}
        {ranking[0] && (
          <Card className={`text-center border ${coresPodio[0]}`}>
            <div className="text-3xl mb-2">🥇</div>
            <p className="text-sm font-semibold text-dark-100 truncate">{ranking[0].usuario}</p>
            <p className="text-xs text-dark-400 mt-1">{ranking[0].pontos} pts</p>
          </Card>
        )}
        {/* 3º lugar — mais baixo */}
        {ranking[2] && (
          <Card className={`text-center border ${coresPodio[2]} sm:mt-4`}>
            <div className="text-3xl mb-2">🥉</div>
            <p className="text-sm font-semibold text-dark-100 truncate">{ranking[2].usuario}</p>
            <p className="text-xs text-dark-400 mt-1">{ranking[2].pontos} pts</p>
          </Card>
        )}
      </div>

      {/* Lista completa do ranking */}
      <Card className="p-0 overflow-hidden">
        <div className="divide-y divide-white/5">
          {ranking.map((item) => {
            // Destaca o usuário logado na lista
            const ehUsuarioLogado = usuarioLogado?.id === item.id;

            return (
              <div
                key={item.posicao}
                className={`flex items-center gap-4 p-4 transition-colors ${
                  ehUsuarioLogado
                    ? 'bg-aqua-500/10 border-l-4 border-aqua-500'
                    : 'hover:bg-white/5'
                }`}
              >
                {/* Posição */}
                <div className={`w-10 h-10 rounded-xl flex items-center justify-center font-bold flex-shrink-0 ${
                  item.posicao <= 3 ? coresPodio[item.posicao - 1] : 'bg-dark-800 text-dark-400'
                }`}>
                  {item.posicao}
                </div>

                {/* Avatar com inicial */}
                <div className="w-10 h-10 rounded-full bg-dark-700 flex items-center justify-center flex-shrink-0">
                  <span className="text-sm font-bold text-dark-300">
                    {item.usuario.charAt(0).toUpperCase()}
                  </span>
                </div>

                {/* Nome */}
                <div className="flex-1 min-w-0">
                  <p className={`text-sm font-medium truncate ${
                    ehUsuarioLogado ? 'text-aqua-400' : 'text-dark-100'
                  }`}>
                    {item.usuario}
                    {ehUsuarioLogado && (
                      <span className="ml-2 text-xs text-aqua-400">(você)</span>
                    )}
                  </p>
                </div>

                {/* Pontos */}
                <span className="text-sm font-bold text-aqua-400 flex-shrink-0">
                  {item.pontos} pts
                </span>
              </div>
            );
          })}
        </div>
      </Card>
    </div>
  );
}
