import { Link } from 'react-router-dom';
import type { Missao, StatusMissao } from '../../types';
import { categorias } from '../../data/categorias';
import Badge from '../ui/Badge';
import { IconeCategoria } from '../ui/Icones';

interface CardMissaoProps {
  missao: Missao;
  status: StatusMissao;
}

// Card que mostra uma missão resumida com link para os detalhes
export default function CardMissao({ missao, status }: CardMissaoProps) {
  // Busca as informações visuais da categoria (cor, ícone)
  const categoria = categorias.find((c) => c.nome === missao.categoria);

  // Define o badge do status
  const statusBadge: Record<StatusMissao, { texto: string; cor: string }> = {
    DISPONIVEL: { texto: 'Disponível', cor: 'text-aqua-400 bg-aqua-500/10' },
    EM_ANDAMENTO: { texto: 'Em andamento', cor: 'text-yellow-400 bg-yellow-500/10' },
    CONCLUIDA: { texto: 'Concluída', cor: 'text-green-500 bg-green-600/10' },
  };

  const badge = statusBadge[status];

  return (
    <Link to={`/missoes/${missao.id}`} className="block">
      <div className="glass-card rounded-2xl p-5 h-full transition-all duration-300 hover:border-aqua-500/30 hover:shadow-lg hover:shadow-aqua-500/10 group">
        {/* Cabeçalho do card */}
        <div className="flex items-start justify-between mb-3">
          <div className={`p-2.5 rounded-xl ${categoria?.corFundo} ${categoria?.cor}`}>
            <IconeCategoria nome={categoria?.icone || ''} className="w-5 h-5" />
          </div>
          <Badge cor={badge.cor}>{badge.texto}</Badge>
        </div>

        {/* Conteúdo */}
        <h3 className="text-base font-semibold text-dark-100 mb-2 group-hover:text-aqua-400 transition-colors">
          {missao.titulo}
        </h3>
        <p className="text-sm text-dark-400 line-clamp-2 mb-4">
          {missao.descricao}
        </p>

        {/* Rodapé do card */}
        <div className="flex items-center justify-between">
          <span className={`text-xs font-medium ${categoria?.cor}`}>
            {categoria?.label}
          </span>
          <span className="text-sm font-bold text-aqua-400">
            +{missao.pontosRecompensa} pts
          </span>
        </div>
      </div>
    </Link>
  );
}
