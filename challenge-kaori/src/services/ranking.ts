import type { RankingItem, CategoriaMissao } from '../types';
import { apiFetch } from './api';

interface RankingItemApi extends Omit<RankingItem, 'id'> {
  id: number;
}

export type FiltroRanking = 'GLOBAL' | CategoriaMissao;

export async function listarRanking(filtro: FiltroRanking = 'GLOBAL'): Promise<RankingItem[]> {
  const query = filtro === 'GLOBAL' ? '' : `?categoria=${encodeURIComponent(filtro)}`;
  const resposta = await apiFetch<RankingItemApi[]>(`/usuarios/ranking${query}`);
  if (!Array.isArray(resposta)) {
    throw new Error('A API retornou uma lista de ranking invalida.');
  }
  return resposta.map((item) => ({ ...item, id: String(item.id) }));
}
