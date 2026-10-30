import { useEffect, useState } from 'react';
import Card from '../../components/ui/Card';
import { useAppData } from '../../context/useAppData';
import { categorias } from '../../data/categorias';
import { listarRankingGlobal } from '../../services/ranking';
import type { CategoriaMissao, RankingItem } from '../../types';

type EscopoRanking = 'GLOBAL' | CategoriaMissao;

export default function Ranking() {
  const { usuarioLogado } = useAppData();
  const [escopo, setEscopo] = useState<EscopoRanking>('GLOBAL');
  const [ranking, setRanking] = useState<RankingItem[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState('');
  const [tentativa, setTentativa] = useState(0);

  useEffect(() => {
    let cancelado = false;
    setCarregando(true);
    setErro('');

    void listarRankingGlobal().then((itens) => {
      if (!cancelado) setRanking(itens);
    }).catch((error: unknown) => {
      if (!cancelado) {
        setErro(error instanceof Error ? error.message : 'Não foi possível carregar o ranking.');
      }
    }).finally(() => {
      if (!cancelado) setCarregando(false);
    });

    return () => {
      cancelado = true;
    };
  }, [tentativa]);

  const coresPodio = [
    'text-yellow-400 bg-yellow-500/10 border-yellow-500/30',
    'text-dark-200 bg-dark-600/20 border-dark-400/30',
    'text-orange-400 bg-orange-500/10 border-orange-500/30',
  ];

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <h1 className="text-3xl sm:text-4xl font-bold text-dark-50 mb-2">Ranking</h1>
      <p className="text-dark-400 mb-6">
        Acompanhe a classificação geral e, futuramente, os rankings por foco sustentável.
      </p>

      <div className="flex flex-wrap gap-2 mb-8" aria-label="Escopo do ranking">
        <button
          type="button"
          aria-pressed={escopo === 'GLOBAL'}
          onClick={() => setEscopo('GLOBAL')}
          className={`px-4 py-2 rounded-xl text-sm transition-colors ${
            escopo === 'GLOBAL' ? 'bg-aqua-500 text-dark-950 font-semibold' : 'glass text-dark-200 hover:text-aqua-400'
          }`}
        >
          Global
        </button>
        {categorias.map((categoria) => (
          <button
            key={categoria.nome}
            type="button"
            aria-pressed={escopo === categoria.nome}
            onClick={() => setEscopo(categoria.nome)}
            className={`px-4 py-2 rounded-xl text-sm transition-colors ${
              escopo === categoria.nome ? 'bg-aqua-500 text-dark-950 font-semibold' : 'glass text-dark-200 hover:text-aqua-400'
            }`}
          >
            {categoria.label}
          </button>
        ))}
      </div>

      {escopo !== 'GLOBAL' ? (
        <Card>
          <h2 className="text-lg font-semibold text-dark-100 mb-2">
            Ranking de {categorias.find((categoria) => categoria.nome === escopo)?.label}
          </h2>
          <p className="text-sm text-dark-400">
            Este ranking será conectado à API de classificação de perfis quando ela estiver disponível.
            Nenhuma pontuação por foco está sendo simulada.
          </p>
        </Card>
      ) : carregando ? (
        <p className="text-sm text-dark-400">Carregando ranking global...</p>
      ) : erro ? (
        <Card className="text-center">
          <p className="mb-4 text-sm text-red-400">{erro}</p>
          <button
            type="button"
            onClick={() => setTentativa((valor) => valor + 1)}
            className="text-sm text-aqua-400 hover:text-aqua-300"
          >
            Tentar novamente
          </button>
        </Card>
      ) : ranking.length === 0 ? (
        <Card>
          <p className="text-sm text-dark-400">Ainda não há usuários no ranking.</p>
        </Card>
      ) : (
        <>
          <div className="grid grid-cols-3 gap-3 mb-8 items-end">
            {ranking[1] && (
              <Card className={`text-center border ${coresPodio[1]} sm:mb-4`}>
                <div className="text-3xl mb-2">🥈</div>
                <p className="text-sm font-semibold text-dark-100 truncate">{ranking[1].usuario}</p>
                <p className="text-xs text-dark-400 mt-1">{ranking[1].pontos} pts</p>
              </Card>
            )}
            {ranking[0] && (
              <Card className={`text-center border ${coresPodio[0]}`}>
                <div className="text-3xl mb-2">🥇</div>
                <p className="text-sm font-semibold text-dark-100 truncate">{ranking[0].usuario}</p>
                <p className="text-xs text-dark-400 mt-1">{ranking[0].pontos} pts</p>
              </Card>
            )}
            {ranking[2] && (
              <Card className={`text-center border ${coresPodio[2]} sm:mt-4`}>
                <div className="text-3xl mb-2">🥉</div>
                <p className="text-sm font-semibold text-dark-100 truncate">{ranking[2].usuario}</p>
                <p className="text-xs text-dark-400 mt-1">{ranking[2].pontos} pts</p>
              </Card>
            )}
          </div>

          <Card className="p-0 overflow-hidden">
            <div className="divide-y divide-white/5">
              {ranking.map((item) => {
                const ehUsuarioLogado = String(usuarioLogado?.id) === item.id;
                return (
                <div
                  key={item.id}
                  className={`flex items-center gap-4 p-4 transition-colors ${
                    ehUsuarioLogado ? 'bg-aqua-500/10 border-l-4 border-aqua-500' : 'hover:bg-white/5'
                  }`}
                >
                  <div className={`w-10 h-10 rounded-xl flex items-center justify-center font-bold flex-shrink-0 ${
                    item.posicao <= 3 ? coresPodio[item.posicao - 1] : 'bg-dark-800 text-dark-400'
                  }`}>
                    {item.posicao}
                  </div>
                  <div className="w-10 h-10 rounded-full bg-dark-700 flex items-center justify-center flex-shrink-0">
                    <span className="text-sm font-bold text-dark-300">{item.usuario.charAt(0).toUpperCase()}</span>
                  </div>
                  <p className={`flex-1 min-w-0 text-sm font-medium truncate ${
                    ehUsuarioLogado ? 'text-aqua-400' : 'text-dark-100'
                  }`}>
                    {item.usuario}
                    {ehUsuarioLogado && <span className="ml-2 text-xs">(você)</span>}
                  </p>
                  <span className="text-sm font-bold text-aqua-400 flex-shrink-0">{item.pontos} pts</span>
                </div>
                );
              })}
            </div>
          </Card>
        </>
      )}
    </div>
  );
}
