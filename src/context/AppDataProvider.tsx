import { useCallback, useState, type ReactNode } from 'react';
import type { Usuario, Missao, UsuarioMissao, PerfilData } from '../types';
import { apiFetch } from '../services/api';
import { AppDataContext } from './appDataContext';
import {
  concluirMissao as concluirMissaoApi,
  iniciarMissao as iniciarMissaoApi,
  listarCatalogoMissoes,
  listarMissoesDoUsuario,
} from '../services/missoes';

import type { Resultado } from './appDataContext';

export function AppDataProvider({ children }: { children: ReactNode }) {
  const [missoes, setMissoes] = useState<Missao[]>([]);
  const [usuarioMissoes, setUsuarioMissoes] = useState<UsuarioMissao[]>([]);
  const [usuarioLogado, setUsuarioLogado] = useState<Usuario | null>(null);

  function login(usuario: Usuario) {
    const usuarioNormalizado = { ...usuario, id: String(usuario.id) };
    localStorage.setItem('@SoulUp:userId', String(usuario.id));
    setUsuarioMissoes([]);
    setUsuarioLogado(usuarioNormalizado);
  }

  function logout() {
    localStorage.removeItem('@SoulUp:userId');
    setUsuarioLogado(null);
    setUsuarioMissoes([]);
  }

  function statusPerfil(): PerfilData | null {
    if (!usuarioLogado) return null;
    const idUsuario = String(usuarioLogado.id);
    const concluidas = usuarioMissoes.filter((missao) => String(missao.idUsuario) === idUsuario && missao.status === 'CONCLUIDA');
    const emAndamento = usuarioMissoes.filter((missao) => String(missao.idUsuario) === idUsuario && missao.status === 'EM_ANDAMENTO');
    const total = concluidas.length + emAndamento.length;

    return {
      id: usuarioLogado.id,
      nomeUsuario: usuarioLogado.nomeUsuario,
      nomeCompleto: usuarioLogado.nomeCompleto,
      email: usuarioLogado.email,
      pontos: usuarioLogado.pontos,
      missoesConcluidas: concluidas.length,
      missoesEmAndamento: emAndamento.length,
      taxaConclusao: total === 0 ? 0 : Math.round((concluidas.length / total) * 100),
    };
  }

  const atualizarPerfil = async (
    nomeUsuario: string,
    nomeCompleto: string,
    email: string,
    senha: string,
  ): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');
    if (!id) return { sucesso: false, mensagem: 'Você precisa estar logado para fazer alterações.' };

    try {
      await apiFetch(`/api/usuarios/atualizar/${id}`, {
        method: 'PATCH',
        body: JSON.stringify({ nomeUsuario, nomeCompleto, email, senha }),
      });
      return { sucesso: true, mensagem: 'Alterações concluídas.' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Não foi possível atualizar os dados da conta.';
      return { sucesso: false, mensagem };
    }
  };

  const removerPerfil = async (senha: string): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');
    if (!id) return { sucesso: false, mensagem: 'Você precisa estar logado como usuário.' };

    try {
      await apiFetch(`/api/usuarios/remover/${id}`, {
        method: 'DELETE',
        body: JSON.stringify({ senha }),
      });
      logout();
      return { sucesso: true, mensagem: '' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Não foi possível excluir a conta.';
      return { sucesso: false, mensagem };
    }
  };

  const carregarCatalogoMissoes = useCallback(async (): Promise<Resultado> => {
    try {
      setMissoes(await listarCatalogoMissoes());
      return { sucesso: true, mensagem: '' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Não foi possível carregar o catálogo de missões.';
      return { sucesso: false, mensagem };
    }
  }, []);

  const carregarMissoesUsuario = useCallback(async (): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');
    if (!id) {
      setUsuarioMissoes([]);
      return { sucesso: true, mensagem: '' };
    }

    const idUsuario = Number(id);
    if (!Number.isInteger(idUsuario) || idUsuario <= 0) {
      return { sucesso: false, mensagem: 'O identificador do usuário é inválido.' };
    }

    try {
      const [emAndamento, concluidas] = await Promise.all([
        listarMissoesDoUsuario(idUsuario, 'EM_ANDAMENTO'),
        listarMissoesDoUsuario(idUsuario, 'CONCLUIDA'),
      ]);
      setUsuarioMissoes([
        ...emAndamento.map((missao) => ({
          idUsuario: String(idUsuario),
          idMissao: missao.id,
          status: 'EM_ANDAMENTO' as const,
        })),
        ...concluidas.map((missao) => ({
          idUsuario: String(idUsuario),
          idMissao: missao.id,
          status: 'CONCLUIDA' as const,
        })),
      ]);
      return { sucesso: true, mensagem: '' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Não foi possível carregar suas missões.';
      return { sucesso: false, mensagem };
    }
  }, [usuarioLogado?.id]);

  function listarMissoesDisponiveis() {
    const idUsuario = String(usuarioLogado?.id);
    const idsEmAndamentoOuConcluidos = new Set(
      usuarioMissoes
        .filter((missao) => String(missao.idUsuario) === idUsuario)
        .map((missao) => missao.idMissao),
    );
    return missoes.filter((missao) => !idsEmAndamentoOuConcluidos.has(missao.id));
  }

  function listarMissoesEmAndamento(idUsuario: string | number) {
    return usuarioMissoes
      .filter((missao) => String(missao.idUsuario) === String(idUsuario) && missao.status === 'EM_ANDAMENTO')
      .map((relacao) => missoes.find((missao) => missao.id === relacao.idMissao))
      .filter((missao): missao is Missao => Boolean(missao));
  }

  function listarMissoesConcluidas(idUsuario: string | number) {
    return usuarioMissoes
      .filter((missao) => String(missao.idUsuario) === String(idUsuario) && missao.status === 'CONCLUIDA')
      .map((relacao) => missoes.find((missao) => missao.id === relacao.idMissao))
      .filter((missao): missao is Missao => Boolean(missao));
  }

  function obterMissao(idMissao: number) {
    return missoes.find((missao) => missao.id === idMissao);
  }

  function obterStatusMissao(idMissao: number): 'DISPONIVEL' | 'EM_ANDAMENTO' | 'CONCLUIDA' {
    if (!usuarioLogado) return 'DISPONIVEL';
    return usuarioMissoes.find((missao) =>
      String(missao.idUsuario) === String(usuarioLogado.id) && missao.idMissao === idMissao,
    )?.status ?? 'DISPONIVEL';
  }

  const iniciarMissao = async (idMissao: number): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');
    if (!id) return { sucesso: false, mensagem: 'Você precisa estar logado para realizar missões.' };

    const idUsuario = Number(id);
    if (!Number.isInteger(idUsuario) || idUsuario <= 0) {
      return { sucesso: false, mensagem: 'O identificador do usuário é inválido.' };
    }

    try {
      await iniciarMissaoApi(idMissao, idUsuario);
      setUsuarioMissoes((anteriores) => [
        ...anteriores.filter((missao) => missao.idMissao !== idMissao),
        { idUsuario: String(idUsuario), idMissao, status: 'EM_ANDAMENTO' },
      ]);
      return { sucesso: true, mensagem: 'Missão iniciada.' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Não foi possível iniciar a missão.';
      return { sucesso: false, mensagem };
    }
  };

  const concluirMissao = async (idMissao: number): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');
    if (!id) return { sucesso: false, mensagem: 'Você precisa estar logado para concluir missões.' };

    const idUsuario = Number(id);
    if (!Number.isInteger(idUsuario) || idUsuario <= 0) {
      return { sucesso: false, mensagem: 'O identificador do usuário é inválido.' };
    }

    try {
      await concluirMissaoApi(idMissao, idUsuario);
      setUsuarioMissoes((anteriores) => [
        ...anteriores.filter((missao) => missao.idMissao !== idMissao),
        { idUsuario: String(idUsuario), idMissao, status: 'CONCLUIDA' },
      ]);

      const pontosGanhos = obterMissao(idMissao)?.pontosRecompensa ?? 0;
      setUsuarioLogado((usuario) => usuario
        ? { ...usuario, pontos: usuario.pontos + pontosGanhos }
        : usuario);
      return { sucesso: true, mensagem: 'Missão concluída.' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Não foi possível concluir a missão.';
      return { sucesso: false, mensagem };
    }
  };

  return (
    <AppDataContext.Provider value={{
      usuarioLogado,
      login,
      logout,
      statusPerfil,
      atualizarPerfil,
      removerPerfil,
      carregarCatalogoMissoes,
      carregarMissoesUsuario,
      listarMissoesDisponiveis,
      listarMissoesEmAndamento,
      listarMissoesConcluidas,
      obterMissao,
      obterStatusMissao,
      iniciarMissao,
      concluirMissao,
    }}>
      {children}
    </AppDataContext.Provider>
  );
}
