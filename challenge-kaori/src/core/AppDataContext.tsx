import { createContext, useContext, useState, type ReactNode } from 'react';
import type {
  Usuario, Missao, UsuarioMissao, RankingItem, PerfilData, Funcionario,
  CargoFuncionario, PermissaoFuncionario, ResumoAdministrativo,
} from '../types';
import { seedMissoes } from './seedMissoes';
import { seedUsuarios } from './seedUsuarios';
import { seedFuncionarios } from './seedFuncionarios';

type Resultado = { sucesso: boolean; mensagem: string };
type ResultadoCadastroFuncionario = Resultado & { credenciais?: { id: number; email: string; senha: string } };
type UsuarioPublico = Omit<Usuario, 'senha'>;
type FuncionarioPublico = Omit<Funcionario, 'senha'>;

const permissoesPorCargo: Record<CargoFuncionario, PermissaoFuncionario[]> = {
  ADMIN: ['CONSULTAR_USUARIOS', 'EDITAR_DADOS_BASICOS', 'EXCLUIR_USUARIO', 'CONSULTAR_FUNCIONARIOS', 'CONSULTAR_RESUMO'],
  TECH: ['CONSULTAR_USUARIOS', 'EDITAR_DADOS_BASICOS', 'CONSULTAR_MISSOES_USUARIO'],
  CEO: ['CONSULTAR_USUARIOS', 'CONSULTAR_RESUMO'],
};

interface AppDataContextType {
  usuarioLogado: Usuario | null;
  funcionarioLogado: Funcionario | null;
  criarUsuario: (nomeUsuario: string, nomeCompleto: string, email: string, senha: string) => Resultado;
  login: (email: string, senha: string) => Resultado;
  logout: () => void;
  criarFuncionario: (nome: string, sobrenome: string, cargo: CargoFuncionario) => ResultadoCadastroFuncionario;
  loginFuncionario: (id: number, email: string, senha: string) => Resultado;
  logoutFuncionario: () => void;
  temPermissao: (permissao: PermissaoFuncionario) => boolean;
  listarUsuariosParaFuncionario: () => UsuarioPublico[];
  atualizarUsuarioComoFuncionario: (id: string, nomeCompleto: string, email: string) => Resultado;
  removerUsuarioComoFuncionario: (id: string) => Resultado;
  listarFuncionarios: () => FuncionarioPublico[];
  listarMissoesDoUsuario: (idUsuario: string) => UsuarioMissao[];
  obterResumoAdministrativo: () => ResumoAdministrativo | null;
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
  const [funcionarios, setFuncionarios] = useState<Funcionario[]>([...seedFuncionarios]);
  const [usuarioLogado, setUsuarioLogado] = useState<Usuario | null>(null);
  const [funcionarioLogado, setFuncionarioLogado] = useState<Funcionario | null>(null);

  function gerarIdUsuario(): string {
    return 'u' + (usuarios.length + 1) + Date.now().toString().slice(-4);
  }

  function criarUsuario(nomeUsuario: string, nomeCompleto: string, email: string, senha: string): Resultado {
    if (funcionarioLogado) return { sucesso: false, mensagem: 'Faça logout do funcionário antes de criar uma conta.' };
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

  function login(email: string, senha: string): Resultado {
    if (funcionarioLogado) return { sucesso: false, mensagem: 'Faça logout do funcionário antes de entrar como usuário.' };
    const usuario = usuarios.find((u) => u.email.toLowerCase() === email.toLowerCase() && u.senha === senha);
    if (!usuario) return { sucesso: false, mensagem: 'Não foi possível logar no perfil.' };
    setUsuarioLogado(usuario);
    return { sucesso: true, mensagem: '' };
  }

  function logout() { setUsuarioLogado(null); }

  function embaralhar<T>(valores: T[]): T[] {
    const resultado = [...valores];
    for (let i = resultado.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [resultado[i], resultado[j]] = [resultado[j], resultado[i]];
    }
    return resultado;
  }

  function gerarIdFuncionario(): number | null {
    return embaralhar(Array.from({ length: 200 }, (_, i) => i + 1)).find((id) => !funcionarios.some((f) => f.id === id)) ?? null;
  }

  function gerarEmailFuncionario(nome: string, sobrenome: string): string {
    const base = `${nome.trim().toLowerCase()}.${sobrenome.trim().toLowerCase()}`.split(' ').join('');
    let numero = 0;
    let email = `${base}@kairos.com`;
    while (funcionarios.some((f) => f.email.toLowerCase() === email.toLowerCase())) {
      numero += 1;
      email = `${base}${numero}@kairos.com`;
    }
    return email;
  }

  function gerarSenhaFuncionario(): string {
    const maiusculas = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    const minusculas = 'abcdefghijklmnopqrstuvwxyz';
    const numeros = '0123456789';
    const todos = maiusculas + minusculas + numeros;
    const caracteres = [maiusculas[Math.floor(Math.random() * maiusculas.length)], minusculas[Math.floor(Math.random() * minusculas.length)], numeros[Math.floor(Math.random() * numeros.length)]];
    while (caracteres.length < 10) caracteres.push(todos[Math.floor(Math.random() * todos.length)]);
    return embaralhar(caracteres).join('');
  }

  function criarFuncionario(nome: string, sobrenome: string, cargo: CargoFuncionario): ResultadoCadastroFuncionario {
    if (usuarioLogado || funcionarioLogado) return { sucesso: false, mensagem: 'Faça logout antes de cadastrar um funcionário.' };
    if (!nome.trim() || !sobrenome.trim()) return { sucesso: false, mensagem: 'Nome e sobrenome são obrigatórios.' };
    if (!['ADMIN', 'TECH', 'CEO'].includes(cargo)) return { sucesso: false, mensagem: 'Cargo inválido.' };
    const id = gerarIdFuncionario();
    if (id === null) return { sucesso: false, mensagem: 'Todos os IDs de funcionário estão ocupados.' };
    const email = gerarEmailFuncionario(nome, sobrenome);
    const senha = gerarSenhaFuncionario();
    setFuncionarios((prev) => [...prev, { id, nome: nome.trim(), sobrenome: sobrenome.trim(), email, senha, cargo }]);
    return { sucesso: true, mensagem: 'Funcionário criado com sucesso.', credenciais: { id, email, senha } };
  }

  function loginFuncionario(id: number, email: string, senha: string): Resultado {
    if (usuarioLogado) return { sucesso: false, mensagem: 'Faça logout do usuário antes de entrar como funcionário.' };
    const funcionario = funcionarios.find((f) => f.id === id && f.email.toLowerCase() === email.toLowerCase() && f.senha === senha);
    if (!funcionario) return { sucesso: false, mensagem: 'Credenciais inválidas.' };
    setFuncionarioLogado(funcionario);
    return { sucesso: true, mensagem: '' };
  }

  function logoutFuncionario() { setFuncionarioLogado(null); }
  function temPermissao(permissao: PermissaoFuncionario): boolean {
    return !!funcionarioLogado && permissoesPorCargo[funcionarioLogado.cargo].includes(permissao);
  }
  function listarUsuariosParaFuncionario(): UsuarioPublico[] {
    if (!temPermissao('CONSULTAR_USUARIOS')) return [];
    return usuarios.map(({ senha: _senha, ...usuario }) => usuario);
  }
  function atualizarUsuarioComoFuncionario(id: string, nomeCompleto: string, email: string): Resultado {
    if (!temPermissao('EDITAR_DADOS_BASICOS')) return { sucesso: false, mensagem: 'Acesso negado.' };
    if (!nomeCompleto.trim() || !email.trim()) return { sucesso: false, mensagem: 'Nome completo e e-mail são obrigatórios.' };
    if (usuarios.some((u) => u.id !== id && u.email.toLowerCase() === email.toLowerCase())) return { sucesso: false, mensagem: 'Este e-mail já está cadastrado.' };
    if (!usuarios.some((u) => u.id === id)) return { sucesso: false, mensagem: 'Usuário não encontrado.' };
    setUsuarios((prev) => prev.map((u) => u.id === id ? { ...u, nomeCompleto, email } : u));
    return { sucesso: true, mensagem: 'Dados básicos atualizados.' };
  }
  function removerUsuarioComoFuncionario(id: string): Resultado {
    if (!temPermissao('EXCLUIR_USUARIO')) return { sucesso: false, mensagem: 'Acesso negado.' };
    if (!usuarios.some((u) => u.id === id)) return { sucesso: false, mensagem: 'Usuário não encontrado.' };
    setUsuarios((prev) => prev.filter((u) => u.id !== id));
    setUsuarioMissoes((prev) => prev.filter((um) => um.idUsuario !== id));
    return { sucesso: true, mensagem: 'Usuário removido.' };
  }
  function listarFuncionarios(): FuncionarioPublico[] {
    if (!temPermissao('CONSULTAR_FUNCIONARIOS')) return [];
    return funcionarios.map(({ senha: _senha, ...funcionario }) => funcionario);
  }
  function listarMissoesDoUsuario(idUsuario: string): UsuarioMissao[] {
    if (!temPermissao('CONSULTAR_MISSOES_USUARIO')) return [];
    return usuarioMissoes.filter((um) => um.idUsuario === idUsuario);
  }
  function obterResumoAdministrativo(): ResumoAdministrativo | null {
    if (!temPermissao('CONSULTAR_RESUMO')) return null;
    return { totalUsuarios: usuarios.length, totalMissoes: missoes.length, missoesEmAndamento: usuarioMissoes.filter((um) => um.status === 'EM_ANDAMENTO').length, missoesConcluidas: usuarioMissoes.filter((um) => um.status === 'CONCLUIDA').length };
  }

  function statusPerfil(): PerfilData | null {
    if (!usuarioLogado) return null;
    const concluidas = usuarioMissoes.filter((um) => um.idUsuario === usuarioLogado.id && um.status === 'CONCLUIDA');
    const emAndamento = usuarioMissoes.filter((um) => um.idUsuario === usuarioLogado.id && um.status === 'EM_ANDAMENTO');
    const total = concluidas.length + emAndamento.length;
    return { id: usuarioLogado.id, nomeUsuario: usuarioLogado.nomeUsuario, nomeCompleto: usuarioLogado.nomeCompleto, email: usuarioLogado.email, pontos: usuarioLogado.pontos, missoesConcluidas: concluidas.length, missoesEmAndamento: emAndamento.length, taxaConclusao: total === 0 ? 0 : Math.round((concluidas.length / total) * 100) };
  }
  function atualizarPerfil(nomeUsuario: string, nomeCompleto: string, email: string, senha: string): Resultado {
    if (!usuarioLogado || funcionarioLogado) return { sucesso: false, mensagem: 'Nenhum usuário logado.' };
    if (![nomeUsuario, nomeCompleto, email, senha].every((valor) => valor.trim())) return { sucesso: false, mensagem: 'Todos os campos são obrigatórios.' };
    if (usuarios.some((u) => u.id !== usuarioLogado.id && (u.nomeUsuario === nomeUsuario || u.email.toLowerCase() === email.toLowerCase()))) return { sucesso: false, mensagem: 'Nome de usuário ou e-mail já cadastrado.' };
    const atualizado = { ...usuarioLogado, nomeUsuario, nomeCompleto, email, senha };
    setUsuarios((prev) => prev.map((u) => u.id === atualizado.id ? atualizado : u));
    setUsuarioLogado(atualizado);
    return { sucesso: true, mensagem: '' };
  }
  function removerPerfil(senha: string): Resultado {
    if (!usuarioLogado || funcionarioLogado) return { sucesso: false, mensagem: 'Nenhum usuário logado.' };
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
    if (!usuarioLogado || funcionarioLogado) return { sucesso: false, mensagem: 'Você precisa estar logado como usuário.' };
    if (usuarioMissoes.some((um) => um.idUsuario === usuarioLogado.id && um.idMissao === idMissao)) return { sucesso: false, mensagem: 'Você já iniciou esta missão.' };
    setUsuarioMissoes((prev) => [...prev, { idUsuario: usuarioLogado.id, idMissao, status: 'EM_ANDAMENTO' }]);
    return { sucesso: true, mensagem: '' };
  }
  function concluirMissao(idMissao: number): Resultado {
    if (!usuarioLogado || funcionarioLogado) return { sucesso: false, mensagem: 'Você precisa estar logado como usuário.' };
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

  return <AppDataContext.Provider value={{ usuarioLogado, funcionarioLogado, criarUsuario, login, logout, criarFuncionario, loginFuncionario, logoutFuncionario, temPermissao, listarUsuariosParaFuncionario, atualizarUsuarioComoFuncionario, removerUsuarioComoFuncionario, listarFuncionarios, listarMissoesDoUsuario, obterResumoAdministrativo, statusPerfil, atualizarPerfil, removerPerfil, listarMissoesDisponiveis, listarMissoesEmAndamento, listarMissoesConcluidas, obterStatusMissao, iniciarMissao, concluirMissao, listarRanking }}>{children}</AppDataContext.Provider>;
}

export function useAppData() {
  const contexto = useContext(AppDataContext);
  if (!contexto) throw new Error('useAppData precisa estar dentro de AppDataProvider');
  return contexto;
}
