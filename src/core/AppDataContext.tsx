import { createContext, useCallback, useContext, useState, type ReactNode } from 'react';
import type {
  Usuario, Missao, UsuarioMissao, RankingItem, PerfilData
} from '../types';
import { seedMissoes } from './seedMissoes';
import { seedUsuarios } from './seedUsuarios';
import { apiFecth } from '../services/api';

type Resultado = { sucesso: boolean; mensagem: string };

interface AppDataContextType {
  usuarioLogado: Usuario | null;
  criarUsuario: (nomeUsuario: string, nomeCompleto: string, email: string, senha: string) => Resultado;
  login: (usuario: any) => void;
  logout: () => void;
  statusPerfil: () => PerfilData | null;
  atualizarPerfil: (nomeUsuario: string, nomeCompleto: string, email: string, senha: string) => Promise<Resultado>;
  removerPerfil: (senha: string) => Promise<Resultado>;
  carregarMissoesUsuario: () => Promise<Resultado>;
  listarMissoesDisponiveis: () => Missao[];
  listarMissoesEmAndamento: (idUsuario: string) => Missao[];
  listarMissoesConcluidas: (idUsuario: string) => Missao[];
  obterMissao: (idMissao: number) => Missao | undefined;
  obterStatusMissao: (idMissao: number) => 'DISPONIVEL' | 'EM_ANDAMENTO' | 'CONCLUIDA';
  iniciarMissao: (idMissao: number) => Promise<Resultado>;
  concluirMissao: (idMissao: number) => Promise<Resultado>;
  listarRanking: () => RankingItem[];
}

const AppDataContext = createContext<AppDataContextType | undefined>(undefined);

export function AppDataProvider({ children }: { children: ReactNode }) {
  const [usuarios, setUsuarios] = useState<Usuario[]>([...seedUsuarios]);
  const [missoes] = useState<Missao[]>([...seedMissoes]);
  const [usuarioMissoes, setUsuarioMissoes] = useState<UsuarioMissao[]>([]);
  const [usuarioLogado, setUsuarioLogado] = useState<Usuario | null>(null);

  function gerarIdUsuario(): string {
    return 'u' + (usuarios.length + 1) + Date.now().toString().slice(-4);
  }

  function criarUsuario(nomeUsuario: string, nomeCompleto: string, email: string, senha: string): Resultado {
    if (!nomeUsuario.trim()) return { sucesso: false, mensagem: 'Erro! Seu usuário não pode estar vazio!' };
    if (usuarios.some((u) => u.nomeUsuario === nomeUsuario)) return { sucesso: false, mensagem: 'Este nome de usuário já existe! Escolha outro.' };
    if (!nomeCompleto.trim()) return { sucesso: false, mensagem: 'Erro! Seu nome completo não pode estar vazio!' };
    if (!email.trim()) return { sucesso: false, mensagem: 'Erro! O email não pode estar vazio!' };
    if (usuarios.some((u) => u.email.toLowerCase() === email.toLowerCase())) return { sucesso: false, mensagem: 'Este email já está vinculado a uma conta! Escolha outro.' };
    if (!senha.trim()) return { sucesso: false, mensagem: 'Erro! A senha não pode estar vazia!' };
    const novoUsuario: Usuario = { id: gerarIdUsuario(), nomeUsuario, nomeCompleto, email, senha, pontos: 0 };
    setUsuarios((prev) => [...prev, novoUsuario]);
    setUsuarioLogado(novoUsuario);
    return { sucesso: true, mensagem: '' };
  }

  const login = (usuario: any) => {
    setUsuarioMissoes([]);
    setUsuarioLogado(usuario);
    return { sucesso: true, mensagem: '' };
  }

  function logout() {
    setUsuarioLogado(null);
    setUsuarioMissoes([]);
  }

  function statusPerfil(): PerfilData | null {
    if (!usuarioLogado) return null;
    const concluidas = usuarioMissoes.filter((um) => String(um.idUsuario) === String(usuarioLogado.id) && um.status === 'CONCLUIDA');
    const emAndamento = usuarioMissoes.filter((um) => String(um.idUsuario) === String(usuarioLogado.id) && um.status === 'EM_ANDAMENTO');
    const total = concluidas.length + emAndamento.length;
    return { id: usuarioLogado.id, nomeUsuario: usuarioLogado.nomeUsuario, nomeCompleto: usuarioLogado.nomeCompleto, email: usuarioLogado.email, pontos: usuarioLogado.pontos, missoesConcluidas: concluidas.length, missoesEmAndamento: emAndamento.length, taxaConclusao: total === 0 ? 0 : Math.round((concluidas.length / total) * 100) };
  }

  const atualizarPerfil = async(nomeUsuario: string, nomeCompleto: string, email: string, senha: string): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');

    if (!id) {
      return { sucesso: false, mensagem: 'Você precisa estar logado para fazer alterações.' };
    }

    try {
      await apiFecth(`/api/usuarios/atualizar/${id}`, {
        method: 'PATCH',
        body: JSON.stringify({ nomeUsuario, nomeCompleto, email, senha})
      })

      return { sucesso: true, mensagem: "Alterações concluídas" };

    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Não foi possível atualizar os dados da conta.';
      return { sucesso: false, mensagem };
    }
  }

  const removerPerfil = async (senha: string): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');

    if (!id) {
      return { sucesso: false, mensagem: 'Você precisa estar logado como usuário.' };
    }

    try {
      await apiFecth(`/api/usuarios/remover/${id}`, {
        method: 'DELETE',
        body: JSON.stringify({ senha }),
      });

      localStorage.removeItem('@SoulUp:userId');
      
      setUsuarioLogado(null);

      return { sucesso: true, mensagem: '' };

    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Não foi possível excluir a conta.';
      return { sucesso: false, mensagem };
    }
  };


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
        apiFecth(`/api/missoes/usuario/${idUsuario}/em-andamento`),
        apiFecth(`/api/missoes/usuario/${idUsuario}/concluidas`),
      ]);

      setUsuarioMissoes([
        ...(emAndamento ?? []).map((missao: { idMissao: number }) => ({
          idUsuario: String(idUsuario),
          idMissao: missao.idMissao,
          status: 'EM_ANDAMENTO' as const,
        })),
        ...(concluidas ?? []).map((missao: { idMissao: number }) => ({
          idUsuario: String(idUsuario),
          idMissao: missao.idMissao,
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
    const idsEmAndamentoOuConcluidos = new Set(
      usuarioMissoes
        .filter((missao) => String(missao.idUsuario) === String(usuarioLogado?.id))
        .map((missao) => missao.idMissao),
    );
    return missoes.filter((missao) => !idsEmAndamentoOuConcluidos.has(missao.id));
  }
  function listarMissoesEmAndamento(idUsuario: string) { return usuarioMissoes.filter((um) => String(um.idUsuario) === String(idUsuario) && um.status === 'EM_ANDAMENTO').map((um) => missoes.find((m) => m.id === um.idMissao)).filter((m): m is Missao => !!m); }
  function listarMissoesConcluidas(idUsuario: string) { return usuarioMissoes.filter((um) => String(um.idUsuario) === String(idUsuario) && um.status === 'CONCLUIDA').map((um) => missoes.find((m) => m.id === um.idMissao)).filter((m): m is Missao => !!m); }
  function obterMissao(idMissao: number) { return missoes.find((missao) => missao.id === idMissao); }
  function obterStatusMissao(idMissao: number): 'DISPONIVEL' | 'EM_ANDAMENTO' | 'CONCLUIDA' {
    if (!usuarioLogado) return 'DISPONIVEL';
    return usuarioMissoes.find((um) => String(um.idUsuario) === String(usuarioLogado.id) && um.idMissao === idMissao)?.status ?? 'DISPONIVEL';
  }
  const iniciarMissao = async (idMissao: number): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');

    if (!id) {
      return { sucesso: false, mensagem: 'Você precisa estar logado para realizar missões.' };
    }

    const idUsuario = Number(id);
    if (!Number.isInteger(idUsuario) || idUsuario <= 0) {
      return { sucesso: false, mensagem: 'O identificador do usuário é inválido.' };
    }

    try {
      await apiFecth(`/api/missoes/${idMissao}/iniciar`, {
        method: 'POST',
        body: JSON.stringify(idUsuario),
      });

      setUsuarioMissoes((anteriores) => [
        ...anteriores.filter((missao) => missao.idMissao !== idMissao),
        { idUsuario: String(idUsuario), idMissao, status: 'EM_ANDAMENTO' },
      ]);

      return { sucesso: true, mensagem: 'Missão iniciada.' };

    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Não foi possível atualizar os dados da conta.';
      return { sucesso: false, mensagem };
    }
  }

  const concluirMissao = async (idMissao: number): Promise<Resultado> => {
    const id = usuarioLogado?.id ?? localStorage.getItem('@SoulUp:userId');

    if (!id) {
      return { sucesso: false, mensagem: 'Você precisa estar logado para concluir missões.' };
    }

    const idUsuario = Number(id);
    if (!Number.isInteger(idUsuario) || idUsuario <= 0) {
      return { sucesso: false, mensagem: 'O identificador do usuário é inválido.' };
    }

    try {
      await apiFecth(`/api/missoes/${idMissao}/concluir?idUsuario=${idUsuario}`, {
        method: 'PUT',
      });

      setUsuarioMissoes((anteriores) => [
        ...anteriores.filter((missao) => missao.idMissao !== idMissao),
        { idUsuario: String(idUsuario), idMissao, status: 'CONCLUIDA' },
      ]);

      const pontosGanhos = missoes.find((missao) => missao.id === idMissao)?.pontosRecompensa ?? 0;
      setUsuarioLogado((usuario) => usuario
        ? { ...usuario, pontos: (usuario.pontos ?? 0) + pontosGanhos }
        : usuario);

      return { sucesso: true, mensagem: 'Missão concluída.' };
    } catch (error: unknown) {
      const mensagem = error instanceof Error ? error.message : 'Não foi possível concluir a missão.';
      return { sucesso: false, mensagem };
    }
  }

  function listarRanking(): RankingItem[] { return [...usuarios].sort((a, b) => b.pontos - a.pontos).map((u, index) => ({ posicao: index + 1, id: u.id, usuario: u.nomeUsuario, pontos: u.pontos })); }

  return <AppDataContext.Provider value={{ usuarioLogado, criarUsuario, login, logout, statusPerfil, atualizarPerfil, removerPerfil, carregarMissoesUsuario, listarMissoesDisponiveis, listarMissoesEmAndamento, listarMissoesConcluidas, obterMissao, obterStatusMissao, iniciarMissao, concluirMissao, listarRanking }}>{children}</AppDataContext.Provider>;
}

export function useAppData() {
  const contexto = useContext(AppDataContext);
  if (!contexto) throw new Error('useAppData precisa estar dentro de AppDataProvider');
  return contexto;
}
