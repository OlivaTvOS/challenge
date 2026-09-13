import { useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAppData } from '../../core/AppDataContext';
import { categorias } from '../../data/categorias';
import ListaMissoes from '../../components/missao/ListaMissoes';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import Button from '../../components/ui/Button';
import { IconeCategoria } from '../../components/ui/Icones';

// Página de Missões — lista e detalhes das missões (rota dinâmica /missoes/:id)
export default function Missoes() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const {
    usuarioLogado,
    listarMissoesDisponiveis,
    listarMissoesEmAndamento,
    listarMissoesConcluidas,
    obterStatusMissao,
    iniciarMissao,
    concluirMissao,
  } = useAppData();

  // Guarda mensagens de feedback ao iniciar/concluir missão
  const [feedback, setFeedback] = useState<{ texto: string; tipo: 'erro' | 'sucesso' } | null>(null);

  // --- Página de detalhes (tem id na URL) ---
  if (id) {
    const idMissao = Number(id);
    const todasMissoes = listarMissoesDisponiveis();
    const missao = todasMissoes.find((m) => m.id === idMissao);

    if (!missao) {
      return (
        <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-12 text-center animate-fade-in">
          <h1 className="text-2xl font-bold text-dark-50 mb-4">Missão não encontrada</h1>
          <Link to="/missoes">
            <Button variante="secundario">Voltar para missões</Button>
          </Link>
        </div>
      );
    }

    const categoria = categorias.find((c) => c.nome === missao.categoria);
    const status = obterStatusMissao(idMissao);

    function handleIniciar() {
      setFeedback(null);
      const resultado = iniciarMissao(idMissao);
      if (!resultado.sucesso) {
        setFeedback({ texto: resultado.mensagem, tipo: 'erro' });
      } else {
        setFeedback({ texto: 'Missão iniciada com sucesso!', tipo: 'sucesso' });
      }
    }

    function handleConcluir() {
      setFeedback(null);
      const resultado = concluirMissao(idMissao);
      if (!resultado.sucesso) {
        setFeedback({ texto: resultado.mensagem, tipo: 'erro' });
      } else {
        setFeedback({ texto: `Missão concluída! +${missao!.pontosRecompensa} pontos ganhos!`, tipo: 'sucesso' });
      }
    }

    const statusBadge = {
      DISPONIVEL: { texto: 'Disponível', cor: 'text-aqua-400 bg-aqua-500/10' },
      EM_ANDAMENTO: { texto: 'Em andamento', cor: 'text-yellow-400 bg-yellow-500/10' },
      CONCLUIDA: { texto: 'Concluída', cor: 'text-green-500 bg-green-600/10' },
    };
    const badge = statusBadge[status];

    return (
      <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
        {/* Botão voltar */}
        <button
          onClick={() => navigate('/missoes')}
          className="flex items-center gap-2 text-sm text-dark-400 hover:text-aqua-400 transition-colors mb-6"
        >
          <svg className="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <line x1="19" y1="12" x2="5" y2="12" />
            <polyline points="12 19 5 12 12 5" />
          </svg>
          Voltar para missões
        </button>

        <Card>
          {/* Cabeçalho */}
          <div className="flex items-start justify-between mb-6">
            <div className={`p-3 rounded-xl ${categoria?.corFundo} ${categoria?.cor}`}>
              <IconeCategoria nome={categoria?.icone || ''} className="w-8 h-8" />
            </div>
            <Badge cor={badge.cor}>{badge.texto}</Badge>
          </div>

          <h1 className="text-2xl font-bold text-dark-50 mb-2">{missao.titulo}</h1>
          <p className="text-sm text-dark-400 mb-6">{missao.descricao}</p>

          {/* Informações da missão */}
          <div className="flex flex-wrap gap-4 mb-8">
            <div className="flex items-center gap-2">
              <span className="text-xs text-dark-500">Categoria:</span>
              <Badge cor={`${categoria?.cor} ${categoria?.corFundo}`}>{categoria?.label}</Badge>
            </div>
            <div className="flex items-center gap-2">
              <span className="text-xs text-dark-500">Recompensa:</span>
              <span className="text-sm font-bold text-aqua-400">+{missao.pontosRecompensa} pontos</span>
            </div>
          </div>

          {/* Feedback de iniciar/concluir */}
          {feedback && (
            <div className={`mb-4 p-3 rounded-xl text-sm animate-fade-in ${feedback.tipo === 'erro'
              ? 'bg-red-500/10 border border-red-500/20 text-red-400'
              : 'bg-green-600/10 border border-green-600/20 text-green-500'
              }`}>
              {feedback.texto}
            </div>
          )}

          {/* Botões de ação conforme o status */}
          <div className="flex flex-col sm:flex-row gap-3">
            {status === 'DISPONIVEL' && (
              <Button variante="primario" className="w-full" onClick={handleIniciar}>
                Iniciar missão
              </Button>
            )}
            {status === 'EM_ANDAMENTO' && (
              <Button variante="primario" className="w-full" onClick={handleConcluir}>
                Concluir missão
              </Button>
            )}
            {status === 'CONCLUIDA' && (
              <div className="w-full text-center py-3 rounded-xl bg-green-600/10 text-green-500 text-sm font-medium">
                Missão concluída! +{missao.pontosRecompensa} pontos ganhos
              </div>
            )}
          </div>
        </Card>
      </div>
    );
  }

  // --- Lista de missões (não tem id na URL) ---
  const disponiveis = listarMissoesDisponiveis();
  const emAndamento = usuarioLogado ? listarMissoesEmAndamento(usuarioLogado.id) : [];
  const concluidas = usuarioLogado ? listarMissoesConcluidas(usuarioLogado.id) : [];

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <h1 className="text-3xl sm:text-4xl font-bold text-dark-50 mb-2">Missões</h1>
      <p className="text-dark-400 mb-8">
        Escolha uma missão sustentável, inicie, conclua e ganhe pontos
      </p>

      {!usuarioLogado && (
        <Card className="mb-8 text-center">
          <p className="text-sm text-dark-400 mb-4">
            Faça login para iniciar e concluir missões
          </p>
          <Link to="/login">
            <Button variante="primario">Entrar</Button>
          </Link>
        </Card>
      )}

      <div className="space-y-10">
        {/* Missões em andamento (só mostra se tiver alguma) */}
        {emAndamento.length > 0 && (
          <ListaMissoes missoes={emAndamento} status="EM_ANDAMENTO" titulo="Em andamento" />
        )}

        {/* Missões disponíveis */}
        <ListaMissoes
          missoes={disponiveis}
          status="DISPONIVEL"
          titulo="Disponíveis"
          mensagemVazia="Nenhuma missão disponível no momento."
        />

        {/* Missões concluídas (só mostra se tiver alguma) */}
        {concluidas.length > 0 && (
          <ListaMissoes missoes={concluidas} status="CONCLUIDA" titulo="Concluídas" />
        )}
      </div>
    </div>
  );
}
