import { useCallback, useEffect, useState, type ReactNode } from 'react';
import type { Usuario, Missao, UsuarioMissao, PerfilData } from '../types';
import { apiFetch } from '../services/api';
import { AppDataContext } from './appDataContext';
import {
  concluirMissao as concluirMissaoApi,
  iniciarMissao as iniciarMissaoApi,
  listarCatalogoMissoes,
  listarMissoesDoUsuario,
} from '../services/missoes';
import type { Resultado, ResultadoComPerfil } from './appDataContext';

export function AppDataProvider({ children }: { children: ReactNode }) {
  const [missoes, setMissoes] = useState<Missao[]>([]);
  const [usuarioMissoes, setUsuarioMissoes] = useState<UsuarioMissao[]>([]);
  const [usuarioLogado, setUsuarioLogado] = useState<Usuario | null>(null);
  const [carregandoSessao, setCarregandoSessao] = useState(true);

  // Recupera a conta pelo ID salvo quando a pagina e atualizada.
  useEffect(() => {
    let cancelado = false;
    const idSalvo = localStorage.getItem('@SoulUp:userId');

    if (!idSalvo) {
      setCarregandoSessao(false);
      return () => {
        cancelado = true;
      };
    }

    apiFetch<Usuario>(`/usuarios/${idSalvo}`)
      .then((usuario) => {
        if (!usuario) {
          throw new Error('A API nao retornou os dados da conta.');
        }
        if (!cancelado) {
          setUsuarioLogado({ ...usuario, id: String(usuario.id) });
        }
      })
      .catch(() => {
        localStorage.removeItem('@SoulUp:userId');
        if (!cancelado) {
          setUsuarioLogado(null);
        }
      })
      .finally(() => {
        if (!cancelado) {
          setCarregandoSessao(false);
        }
      });

    return () => {
      cancelado = true;
    };
  }, []);

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
    const concluidas = usuarioMissoes.filter((missao) =>
      String(missao.idUsuario) === idUsuario && missao.status === 'CONCLUIDA');
    const emAndamento = usuarioMissoes.filter((missao) =>
      String(missao.idUsuario) === idUsuario && missao.status === 'EM_ANDAMENTO');
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
  ): Promise<ResultadoComPerfil> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');
    if (!id) return { sucesso: false, mensagem: 'Voce precisa estar logado para fazer alteracoes.' };

    try {
      await apiFetch(`/usuarios/atualizar/${id}`, {
        method: 'PATCH',
        body: JSON.stringify({ nomeUsuario, nomeCompleto, email, senha }),
      });
      const usuarioAtualizado = await apiFetch<Usuario>(`/usuarios/${id}`);
      if (!usuarioAtualizado) {
        throw new Error('A API nao retornou o perfil atualizado.');
      }
      const usuarioNormalizado = { ...usuarioAtualizado, id: String(usuarioAtualizado.id) };
      setUsuarioLogado(usuarioNormalizado);
      return {
        sucesso: true,
        mensagem: 'Alteracoes concluidas.',
        perfil: usuarioNormalizado,
      };
    } catch (error: unknown) {
      const mensagem = error instanceof Error
        ? error.message
        : 'Nao foi possivel atualizar os dados da conta.';
      return { sucesso: false, mensagem };
    }
  };

  const removerPerfil = async (senha: string): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');
    if (!id) return { sucesso: false, mensagem: 'Voce precisa estar logado como usuario.' };

    try {
      await apiFetch(`/usuarios/remover/${id}`, {
        method: 'DELETE',
        body: JSON.stringify({ senha }),
      });
      logout();
      return { sucesso: true, mensagem: '' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error
        ? error.message
        : 'Nao foi possivel excluir a conta.';
      return { sucesso: false, mensagem };
    }
  };

  const carregarCatalogoMissoes = useCallback(async (): Promise<Resultado> => {
    try {
      setMissoes(await listarCatalogoMissoes());
      return { sucesso: true, mensagem: '' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error
        ? error.message
        : 'Nao foi possivel carregar o catalogo de missoes.';
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
      return { sucesso: false, mensagem: 'O identificador do usuario e invalido.' };
    }

    try {
      const [emAndamento, concluidas] = await Promise.all([
        listarMissoesDoUsuario(idUsuario, 'EM_ANDAMENTO'),
        listarMissoesDoUsuario(idUsuario, 'CONCLUIDA'),
      ]);
      const relacoes: UsuarioMissao[] = [];

      emAndamento.forEach((missao) => {
        relacoes.push({
          idUsuario: String(idUsuario),
          idMissao: missao.id,
          status: 'EM_ANDAMENTO',
        });
      });
      concluidas.forEach((missao) => {
        relacoes.push({
          idUsuario: String(idUsuario),
          idMissao: missao.id,
          status: 'CONCLUIDA',
        });
      });
      setUsuarioMissoes(relacoes);
      return { sucesso: true, mensagem: '' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error
        ? error.message
        : 'Nao foi possivel carregar suas missoes.';
      return { sucesso: false, mensagem };
    }
  }, [usuarioLogado?.id]);

  function listarMissoesDisponiveis() {
    const idUsuario = String(usuarioLogado?.id);
    const idsEmAndamentoOuConcluidos = new Set<number>();
    usuarioMissoes.forEach((missao) => {
      if (String(missao.idUsuario) === idUsuario) {
        idsEmAndamentoOuConcluidos.add(missao.idMissao);
      }
    });
    return missoes.filter((missao) => !idsEmAndamentoOuConcluidos.has(missao.id));
  }

  function listarMissoesEmAndamento(idUsuario: string | number) {
    const resultado: Missao[] = [];
    usuarioMissoes.forEach((relacao) => {
      if (String(relacao.idUsuario) === String(idUsuario) && relacao.status === 'EM_ANDAMENTO') {
        const missao = missoes.find((item) => item.id === relacao.idMissao);
        if (missao) resultado.push(missao);
      }
    });
    return resultado;
  }

  function listarMissoesConcluidas(idUsuario: string | number) {
    const resultado: Missao[] = [];
    usuarioMissoes.forEach((relacao) => {
      if (String(relacao.idUsuario) === String(idUsuario) && relacao.status === 'CONCLUIDA') {
        const missao = missoes.find((item) => item.id === relacao.idMissao);
        if (missao) resultado.push(missao);
      }
    });
    return resultado;
  }

  function obterMissao(idMissao: number) {
    return missoes.find((missao) => missao.id === idMissao);
  }

  function obterStatusMissao(idMissao: number): 'DISPONIVEL' | 'EM_ANDAMENTO' | 'CONCLUIDA' {
    if (!usuarioLogado) return 'DISPONIVEL';
    const relacao = usuarioMissoes.find((missao) =>
      String(missao.idUsuario) === String(usuarioLogado.id) && missao.idMissao === idMissao);
    return relacao?.status ?? 'DISPONIVEL';
  }

  const iniciarMissao = async (idMissao: number): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');
    if (!id) return { sucesso: false, mensagem: 'Voce precisa estar logado para realizar missoes.' };

    const idUsuario = Number(id);
    if (!Number.isInteger(idUsuario) || idUsuario <= 0) {
      return { sucesso: false, mensagem: 'O identificador do usuario e invalido.' };
    }

    try {
      await iniciarMissaoApi(idMissao, idUsuario);
      setUsuarioMissoes((anteriores) => [
        ...anteriores.filter((missao) => missao.idMissao !== idMissao),
        { idUsuario: String(idUsuario), idMissao, status: 'EM_ANDAMENTO' },
      ]);
      return { sucesso: true, mensagem: 'Missao iniciada.' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Nao foi possivel iniciar a missao.';
      return { sucesso: false, mensagem };
    }
  };

  const concluirMissao = async (idMissao: number): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');
    if (!id) return { sucesso: false, mensagem: 'Voce precisa estar logado para concluir missoes.' };

    const idUsuario = Number(id);
    if (!Number.isInteger(idUsuario) || idUsuario <= 0) {
      return { sucesso: false, mensagem: 'O identificador do usuario e invalido.' };
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
      return { sucesso: true, mensagem: 'Missao concluida.' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Nao foi possivel concluir a missao.';
      return { sucesso: false, mensagem };
    }
  };

  return (
    <AppDataContext.Provider value={{
      usuarioLogado,
      carregandoSessao,
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
