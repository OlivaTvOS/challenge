import { categorias } from '../data/categorias';
import type { Missao, StatusUsuarioMissao } from '../types';
import { apiFetch } from './api';

interface MissaoApi {
  idMissao: number;
  idCategoria: number;
  titulo: string;
  descricao: string | null;
  pontosRecompensa: number;
}

function mapearMissao(missao: MissaoApi): Missao {
  const categoria = categorias.find((item) => item.id === missao.idCategoria);
  if (!categoria) {
    throw new Error(`A missao ${missao.idMissao} usa uma categoria desconhecida (${missao.idCategoria}).`);
  }

  return {
    id: missao.idMissao,
    categoria: categoria.nome,
    titulo: missao.titulo,
    descricao: missao.descricao ?? '',
    pontosRecompensa: missao.pontosRecompensa,
  };
}

async function buscarMissoes(endpoint: string): Promise<Missao[]> {
  const resposta = await apiFetch<MissaoApi[]>(endpoint);
  if (!Array.isArray(resposta)) {
    throw new Error('A API retornou uma lista de missoes invalida.');
  }
  return resposta.map(mapearMissao);
}

export function listarCatalogoMissoes(): Promise<Missao[]> {
  return buscarMissoes('/missoes');
}

export function listarMissoesDoUsuario(idUsuario: number, status: StatusUsuarioMissao): Promise<Missao[]> {
  const endpoint = `/missoes/usuario/${idUsuario}/${status === 'EM_ANDAMENTO' ? 'em-andamento' : 'concluidas'}`;
  return buscarMissoes(endpoint);
}

export async function iniciarMissao(idMissao: number, idUsuario: number): Promise<void> {
  await apiFetch(`/missoes/${idMissao}/iniciar`, {
    method: 'POST',
    body: JSON.stringify(idUsuario),
  });
}

export async function concluirMissao(idMissao: number, idUsuario: number): Promise<void> {
  await apiFetch(`/missoes/${idMissao}/concluir?idUsuario=${idUsuario}`, {
    method: 'PUT',
  });
}
