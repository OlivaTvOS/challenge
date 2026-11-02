import { useEffect, useState } from 'react';
import { useAppData } from '../../core/AppDataContext';
import Card from '../../components/ui/Card';
import type { RankingItem } from '../../types';
import { listarRanking, type FiltroRanking } from '../../services/ranking';

const filtros: { valor: FiltroRanking; rotulo: string }[] = [
  { valor: 'GLOBAL', rotulo: 'Global' },
  { valor: 'RECICLAGEM', rotulo: 'Reciclagem' },
  { valor: 'MOBILIDADE', rotulo: 'Mobilidade' },
  { valor: 'AGUA', rotulo: 'Água' },
  { valor: 'ENERGIA', rotulo: 'Energia' },
  { valor: 'NATUREZA', rotulo: 'Natureza' },
  { valor: 'CONSUMO', rotulo: 'Consumo' },
];

const coresPodio = [
  'text-yellow-400 bg-yellow-500/10 border-yellow-500/30',
  'text-slate-300 bg-slate-500/10 border-slate-400/30',
  'text-orange-400 bg-orange-500/10 border-orange-500/30',
];

export default function Ranking() {
  const { usuarioLogado } = useAppData();
  const [filtro, setFiltro] = useState<FiltroRanking>('GLOBAL');
  const [ranking, setRanking] = useState<RankingItem[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState('');

  useEffect(() => {
    let cancelado = false;
    setCarregando(true);
    setErro('');
    listarRanking(filtro)
      .then((dados) => {
        if (!cancelado) setRanking(dados);
      })
      .catch((error: unknown) => {
        if (!cancelado) {
          setRanking([]);
          setErro(error instanceof Error ? error.message : 'Não foi possível carregar o ranking.');
        }
      })
      .finally(() => {
        if (!cancelado) setCarregando(false);
      });
    return () => { cancelado = true; };
  }, [filtro]);

  const nomeFiltro = filtros.find((item) => item.valor === filtro)?.rotulo ?? 'Global';

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <h1 className="text-3xl sm:text-4xl font-bold text-dark-50 mb-2">Ranking</h1>
      <p className="text-dark-300 mb-6">
        Acompanhe a classificação geral e os rankings por foco sustentável.
      </p>

      <div className="flex flex-wrap gap-2 mb-6" role="group" aria-label="Filtrar ranking por categoria">
        {filtros.map((item) => (
          <button
            key={item.valor}
            type="button"
            onClick={() => setFiltro(item.valor)}
            aria-pressed={filtro === item.valor}
            className={`rounded-xl border px-4 py-2 text-sm font-semibold transition-colors ${
              filtro === item.valor
                ? 'bg-white text-blue-900 border-white'
                : 'bg-blue-900/50 text-white border-blue-300/40 hover:bg-blue-800'
            }`}
          >
            {item.rotulo}
          </button>
        ))}
      </div>

      <Card className="p-0 overflow-hidden">
        <div className="p-5 border-b border-white/10">
          <h2 className="text-lg font-bold text-white">Ranking {nomeFiltro}</h2>
          <p className="text-sm text-dark-200 mt-1">
            {filtro === 'GLOBAL'
              ? 'Pontuação total acumulada pelos usuários.'
              : `Pontuação obtida em missões concluídas na categoria ${nomeFiltro.toLowerCase()}.`}
          </p>
        </div>

        {carregando ? (
          <p className="p-6 text-sm text-dark-200" role="status">Carregando classificação...</p>
        ) : erro ? (
          <div className="p-6">
            <p className="text-sm text-red-300">{erro}</p>
          </div>
        ) : ranking.length === 0 ? (
          <p className="p-6 text-sm text-dark-200">Ainda não há pontuações para exibir neste ranking.</p>
        ) : (
          <>
            <div className="grid grid-cols-3 gap-3 p-4 items-end">
              {[1, 0, 2].map((posicao) => {
                const item = ranking[posicao];
                if (!item) return <div key={posicao} />;
                return (
                  <div key={item.id} className={`rounded-xl border p-3 text-center ${coresPodio[posicao]} ${posicao === 0 ? 'min-h-32' : 'min-h-24'}`}>
                    <div className="text-2xl mb-1">{posicao === 0 ? '🥇' : posicao === 1 ? '🥈' : '🥉'}</div>
                    <p className="text-sm font-semibold truncate">{item.usuario}</p>
                    <p className="text-xs mt-1">{item.pontos} pts</p>
                  </div>
                );
              })}
            </div>
            <div className="divide-y divide-white/10">
              {ranking.map((item) => {
                const ehUsuarioLogado = usuarioLogado?.id === item.id;
                return (
                  <div key={item.id} className={`flex items-center gap-4 p-4 ${ehUsuarioLogado ? 'bg-cyan-400/10 border-l-4 border-cyan-400' : ''}`}>
                    <div className={`w-9 h-9 rounded-xl flex items-center justify-center font-bold ${item.posicao <= 3 ? coresPodio[item.posicao - 1] : 'bg-slate-800 text-slate-300'}`}>
                      {item.posicao}
                    </div>
                    <div className="w-9 h-9 rounded-full bg-slate-700 flex items-center justify-center font-bold text-white">
                      {item.usuario.charAt(0).toUpperCase()}
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="font-medium text-white truncate">
                        {item.usuario}{ehUsuarioLogado && <span className="ml-2 text-xs text-cyan-300">(você)</span>}
                      </p>
                    </div>
                    <span className="font-bold text-cyan-300">{item.pontos} pts</span>
                  </div>
                );
              })}
            </div>
          </>
        )}
      </Card>
    </div>
  );
}
