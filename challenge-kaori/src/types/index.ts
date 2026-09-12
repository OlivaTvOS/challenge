// Tipos das categorias de missão (nomes fixos que já existem no backend)
export type CategoriaMissao =
  | 'RECICLAGEM'
  | 'MOBILIDADE'
  | 'AGUA'
  | 'ENERGIA'
  | 'NATUREZA'
  | 'CONSUMO';

// Status que uma missão pode ter (para exibição)
export type StatusMissao = 'DISPONIVEL' | 'EM_ANDAMENTO' | 'CONCLUIDA';

// Status da relação usuário-missão (sem DISPONIVEL, pois é o padrão)
export type StatusUsuarioMissao = 'EM_ANDAMENTO' | 'CONCLUIDA';

// Interface que representa um usuário da plataforma
export interface Usuario {
  id: string;
  nomeUsuario: string;
  nomeCompleto: string;
  email: string;
  senha: string;
  pontos: number;
}

// Interface que representa uma missão sustentável (não tem status — o status vem da relação)
export interface Missao {
  id: number;
  categoria: CategoriaMissao;
  titulo: string;
  descricao: string;
  pontosRecompensa: number;
}

// Relação usuário x missão x status (equivalente à tabela USUARIO_MISSAO do banco)
export interface UsuarioMissao {
  idUsuario: string;
  idMissao: number;
  status: StatusUsuarioMissao;
}

// Interface que representa uma linha do ranking
export interface RankingItem {
  posicao: number;
  id: string;
  usuario: string;
  pontos: number;
}

// Dados do perfil (calculados a partir das relações de missão)
export interface PerfilData {
  id: string;
  nomeUsuario: string;
  nomeCompleto: string;
  email: string;
  pontos: number;
  missoesConcluidas: number;
  missoesEmAndamento: number;
  taxaConclusao: number;
}

// Interface que representa um integrante da equipe
export interface Integrante {
  id: number;
  nome: string;
  rm: string;
  turma: string;
  github: string;
  linkedin: string;
  foto?: string;
}

// Interface para os dados do formulário de login
export interface FormLoginData {
  email: string;
  senha: string;
}

// Interface para os dados do formulário de cadastro
export interface FormCadastroData {
  nomeUsuario: string;
  nomeCompleto: string;
  email: string;
  senha: string;
  confirmarSenha: string;
}

// Interface para os dados do formulário de edição de perfil
export interface FormEditarPerfilData {
  nomeUsuario: string;
  nomeCompleto: string;
  email: string;
  senha: string;
}

// Interface para os dados do formulário de contato
export interface FormContatoData {
  nome: string;
  email: string;
  mensagem: string;
}

// Informações de cada categoria (cor, ícone SVG, descrição)
export interface InfoCategoria {
  nome: CategoriaMissao;
  label: string;
  cor: string;
  corFundo: string;
  icone: string;
  descricao: string;
}

export type CargoFuncionario = 'ADMIN' | 'TECH' | 'CEO';

export type PermissaoFuncionario =
  | 'CONSULTAR_USUARIOS'
  | 'EDITAR_DADOS_BASICOS'
  | 'EXCLUIR_USUARIO'
  | 'CONSULTAR_FUNCIONARIOS'
  | 'CONSULTAR_RESUMO'
  | 'CONSULTAR_MISSOES_USUARIO';

export interface Funcionario {
  id: number;
  nome: string;
  sobrenome: string;
  email: string;
  senha: string;
  cargo: CargoFuncionario;
}

export interface FormLoginFuncionarioData {
  id: number;
  email: string;
  senha: string;
}

export interface FormCadastroFuncionarioData {
  nome: string;
  sobrenome: string;
  cargo: CargoFuncionario;
}

export interface ResumoAdministrativo {
  totalUsuarios: number;
  totalMissoes: number;
  missoesEmAndamento: number;
  missoesConcluidas: number;
}
