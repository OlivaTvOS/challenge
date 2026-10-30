import { createContext } from 'react';
import type { Usuario, Missao, PerfilData } from '../types';

export type Resultado = { sucesso: boolean; mensagem: string };

export interface AppDataContextType {
  usuarioLogado: Usuario | null;
  login: (usuario: Usuario) => void;
  logout: () => void;
  statusPerfil: () => PerfilData | null;
  atualizarPerfil: (nomeUsuario: string, nomeCompleto: string, email: string, senha: string) => Promise<Resultado>;
  removerPerfil: (senha: string) => Promise<Resultado>;
  carregarCatalogoMissoes: () => Promise<Resultado>;
  carregarMissoesUsuario: () => Promise<Resultado>;
  listarMissoesDisponiveis: () => Missao[];
  listarMissoesEmAndamento: (idUsuario: string | number) => Missao[];
  listarMissoesConcluidas: (idUsuario: string | number) => Missao[];
  obterMissao: (idMissao: number) => Missao | undefined;
  obterStatusMissao: (idMissao: number) => 'DISPONIVEL' | 'EM_ANDAMENTO' | 'CONCLUIDA';
  iniciarMissao: (idMissao: number) => Promise<Resultado>;
  concluirMissao: (idMissao: number) => Promise<Resultado>;
}

export const AppDataContext = createContext<AppDataContextType | undefined>(undefined);
