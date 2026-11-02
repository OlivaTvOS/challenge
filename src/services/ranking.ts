import type { RankingItem, CategoriaMissao } from '../types';
import { apiFetch } from './api';

interface RankingItemApi extends Omit<RankingItem, 'id'> {
  id: number;
}

export type EscopoRanking = 'GLOBAL' | CategoriaMissao;

export async function listarRanking(escopo: EscopoRanking = 'GLOBAL'): Promise<RankingItem[]> {
  const query = escopo === 'GLOBAL' ? '' : `?categoria=${encodeURIComponent(escopo)}`;
  const resposta = await apiFetch<RankingItemApi[]>(`/api/usuarios/ranking${query}`);
  if (!Array.isArray(resposta)) {
    throw new Error('A API retornou uma lista de ranking inválida.');
  }
  return resposta.map((item) => ({ ...item, id: String(item.id) }));
}

export async function listarRankingGlobal(): Promise<RankingItem[]> {
  return listarRanking('GLOBAL');
}
