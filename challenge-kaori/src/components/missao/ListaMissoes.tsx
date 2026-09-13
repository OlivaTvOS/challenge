import type { Missao, StatusMissao } from '../../types';
import CardMissao from './CardMissao';

interface ListaMissoesProps {
  missoes: Missao[];
  status: StatusMissao;
  titulo: string;
  mensagemVazia?: string;
}

// Lista de missões em formato de grid responsivo
export default function ListaMissoes({ missoes, status, titulo, mensagemVazia = 'Nenhuma missão aqui.' }: ListaMissoesProps) {
  return (
    <section>
      <h2 className="text-lg font-semibold text-dark-100 mb-4">{titulo}</h2>
      {missoes.length === 0 ? (
        <p className="text-sm text-dark-400">{mensagemVazia}</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {missoes.map((missao) => (
            <CardMissao key={missao.id} missao={missao} status={status} />
          ))}
        </div>
      )}
    </section>
  );
}
