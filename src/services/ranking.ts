import type { RankingItem } from '../types';
import { apiFetch } from './api';

interface RankingItemApi extends Omit<RankingItem, 'id'> {
  id: number;
}

export async function listarRankingGlobal(): Promise<RankingItem[]> {
  const resposta = await apiFetch<RankingItemApi[]>('/api/usuarios/ranking');
  if (!Array.isArray(resposta)) {
    throw new Error('A API retornou uma lista de ranking inválida.');
  }

  return resposta.map((item) => ({ ...item, id: String(item.id) }));
}
