import { createContext, useContext, useState, type ReactNode } from 'react';
import type {
  Usuario, Missao, UsuarioMissao, RankingItem, PerfilData
} from '../types';
import { seedMissoes } from './seedMissoes';
import { seedUsuarios } from './seedUsuarios';

type Resultado = { sucesso: boolean; mensagem: string };

interface AppDataContextType {
  usuarioLogado: Usuario | null;
  criarUsuario: (nomeUsuario: string, nomeCompleto: string, email: string, senha: string) => Resultado;
  login: (usuario: any) => void;
  logout: () => void;
  statusPerfil: () => PerfilData | null;
  atualizarPerfil: (nomeUsuario: string, nomeCompleto: string, email: string, senha: string) => Resultado;
  removerPerfil: (senha: string) => Resultado;
  listarMissoesDisponiveis: () => Missao[];
  listarMissoesEmAndamento: (idUsuario: string) => Missao[];
  listarMissoesConcluidas: (idUsuario: string) => Missao[];
  obterStatusMissao: (idMissao: number) => 'DISPONIVEL' | 'EM_ANDAMENTO' | 'CONCLUIDA';
  iniciarMissao: (idMissao: number) => Resultado;
  concluirMissao: (idMissao: number) => Resultado;
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
    setUsuarioLogado(usuario);
    return { sucesso: true, mensagem: '' };
  }

  function logout() { setUsuarioLogado(null); }

  function statusPerfil(): PerfilData | null {
    if (!usuarioLogado) return null;
    const concluidas = usuarioMissoes.filter((um) => um.idUsuario === usuarioLogado.id && um.status === 'CONCLUIDA');
    const emAndamento = usuarioMissoes.filter((um) => um.idUsuario === usuarioLogado.id && um.status === 'EM_ANDAMENTO');
    const total = concluidas.length + emAndamento.length;
    return { id: usuarioLogado.id, nomeUsuario: usuarioLogado.nomeUsuario, nomeCompleto: usuarioLogado.nomeCompleto, email: usuarioLogado.email, pontos: usuarioLogado.pontos, missoesConcluidas: concluidas.length, missoesEmAndamento: emAndamento.length, taxaConclusao: total === 0 ? 0 : Math.round((concluidas.length / total) * 100) };
  }
  function atualizarPerfil(nomeUsuario: string, nomeCompleto: string, email: string, senha: string): Resultado {
    if (!usuarioLogado) {
      return {
        sucesso: false,
        mensagem: 'Você precisa estar logado como usuário.',
      };
    }
    if (![nomeUsuario, nomeCompleto, email, senha].every((valor) => valor.trim())) return { sucesso: false, mensagem: 'Todos os campos são obrigatórios.' };
    if (usuarios.some((u) => u.id !== usuarioLogado.id && (u.nomeUsuario === nomeUsuario || u.email.toLowerCase() === email.toLowerCase()))) return { sucesso: false, mensagem: 'Nome de usuário ou e-mail já cadastrado.' };
    const atualizado = { ...usuarioLogado, nomeUsuario, nomeCompleto, email, senha };
    setUsuarios((prev) => prev.map((u) => u.id === atualizado.id ? atualizado : u));
    setUsuarioLogado(atualizado);
    return { sucesso: true, mensagem: '' };
  }
  function removerPerfil(senha: string): Resultado {
    if (!usuarioLogado) {
      return {
        sucesso: false,
        mensagem: 'Você precisa estar logado como usuário.',
      };
    }
    if (senha !== usuarioLogado.senha) return { sucesso: false, mensagem: 'Senha incorreta. A conta não foi removida.' };
    setUsuarios((prev) => prev.filter((u) => u.id !== usuarioLogado.id));
    setUsuarioMissoes((prev) => prev.filter((um) => um.idUsuario !== usuarioLogado.id));
    setUsuarioLogado(null);
    return { sucesso: true, mensagem: '' };
  }
  function listarMissoesDisponiveis() { return [...missoes]; }
  function listarMissoesEmAndamento(idUsuario: string) { return usuarioMissoes.filter((um) => um.idUsuario === idUsuario && um.status === 'EM_ANDAMENTO').map((um) => missoes.find((m) => m.id === um.idMissao)).filter((m): m is Missao => !!m); }
  function listarMissoesConcluidas(idUsuario: string) { return usuarioMissoes.filter((um) => um.idUsuario === idUsuario && um.status === 'CONCLUIDA').map((um) => missoes.find((m) => m.id === um.idMissao)).filter((m): m is Missao => !!m); }
  function obterStatusMissao(idMissao: number): 'DISPONIVEL' | 'EM_ANDAMENTO' | 'CONCLUIDA' {
    if (!usuarioLogado) return 'DISPONIVEL';
    return usuarioMissoes.find((um) => um.idUsuario === usuarioLogado.id && um.idMissao === idMissao)?.status ?? 'DISPONIVEL';
  }
  function iniciarMissao(idMissao: number): Resultado {
  if (!usuarioLogado) {
      return {
        sucesso: false,
        mensagem: 'Você precisa estar logado como usuário.',
      };
    }
    if (usuarioMissoes.some((um) => um.idUsuario === usuarioLogado.id && um.idMissao === idMissao)) return { sucesso: false, mensagem: 'Você já iniciou esta missão.' };
    setUsuarioMissoes((prev) => [...prev, { idUsuario: usuarioLogado.id, idMissao, status: 'EM_ANDAMENTO' }]);
    return { sucesso: true, mensagem: '' };
  }
  function concluirMissao(idMissao: number): Resultado {
  if (!usuarioLogado) {
      return {
        sucesso: false,
        mensagem: 'Você precisa estar logado como usuário.',
      };
    }
    if (!usuarioMissoes.some((um) => um.idUsuario === usuarioLogado.id && um.idMissao === idMissao && um.status === 'EM_ANDAMENTO')) return { sucesso: false, mensagem: 'Esta missão não está em andamento.' };
    setUsuarioMissoes((prev) => prev.map((um) => um.idUsuario === usuarioLogado.id && um.idMissao === idMissao ? { ...um, status: 'CONCLUIDA' as const } : um));
    const missao = missoes.find((m) => m.id === idMissao);
    if (missao) {
      const atualizado = { ...usuarioLogado, pontos: usuarioLogado.pontos + missao.pontosRecompensa };
      setUsuarios((prev) => prev.map((u) => u.id === atualizado.id ? atualizado : u));
      setUsuarioLogado(atualizado);
    }
    return { sucesso: true, mensagem: '' };
  }
  function listarRanking(): RankingItem[] { return [...usuarios].sort((a, b) => b.pontos - a.pontos).map((u, index) => ({ posicao: index + 1, id: u.id, usuario: u.nomeUsuario, pontos: u.pontos })); }

  return <AppDataContext.Provider value={{ usuarioLogado, criarUsuario, login, logout, statusPerfil, atualizarPerfil, removerPerfil, listarMissoesDisponiveis, listarMissoesEmAndamento, listarMissoesConcluidas, obterStatusMissao, iniciarMissao, concluirMissao, listarRanking }}>{children}</AppDataContext.Provider>;
}

export function useAppData() {
  const contexto = useContext(AppDataContext);
  if (!contexto) throw new Error('useAppData precisa estar dentro de AppDataProvider');
  return contexto;
}
