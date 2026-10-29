import { useState } from 'react';
import { Navigate, Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAppData } from '../../core/AppDataContext';
import type { FormEditarPerfilData } from '../../types';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import { categorias } from '../../data/categorias';
import { IconeCategoria } from '../../components/ui/Icones';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';

// Página de Perfil — mostra dados reais do usuarioLogado, com editar e excluir conta
export default function Perfil() {
  const { statusPerfil, atualizarPerfil, removerPerfil, listarMissoesConcluidas, usuarioLogado } = useAppData();
  const navigate = useNavigate();

  // Estados para controlar os modais de editar e excluir
  const [editando, setEditando] = useState(false);
  const [excluindo, setExcluindo] = useState(false);
  const [erroEdicao, setErroEdicao] = useState('');
  const [erroExclusao, setErroExclusao] = useState('');

  // Form de edição
  const {
    register: registerEditar,
    handleSubmit: handleEditar,
    reset: resetEditar,
    formState: { errors: errorsEditar },
  } = useForm<FormEditarPerfilData>();

  // Form de exclusão (só senha)
  const {
    register: registerExcluir,
    handleSubmit: handleExcluir,
    reset: resetExcluir,
    formState: { errors: errorsExcluir },
  } = useForm<{ senha: string }>();

  // Se não tem usuário logado, redireciona para o login
  if (!usuarioLogado) {
    return <Navigate to="/login" replace />;
  }

  const perfil = statusPerfil();
  if (!perfil) {
    return <Navigate to="/login" replace />;
  }

  // Missões concluídas do usuário logado (lista resumida)
  const missoesConcluidas = listarMissoesConcluidas(usuarioLogado.id);

  // Abre o modo de edição preenchendo os campos com os dados atuais
  function abrirEdicao() {
    setErroEdicao('');
    resetEditar({
      nomeUsuario: perfil!.nomeUsuario,
      nomeCompleto: perfil!.nomeCompleto,
      email: perfil!.email,
      senha: usuarioLogado!.senha,
    });
    setEditando(true);
  }

  async function onSubmitEdicao(data: FormEditarPerfilData) {
    setErroEdicao('');
    const resultado = await atualizarPerfil(data.nomeUsuario, data.nomeCompleto, data.email, data.senha);
    if (!resultado.sucesso) {
      setErroEdicao(resultado.mensagem);
      return;
    }
    setEditando(false);
  }

  async function onSubmitExclusao(data: { senha: string }) {
    setErroExclusao('');
    const resultado = await removerPerfil(data.senha);
    if (!resultado.sucesso) {
      setErroExclusao(resultado.mensagem);
      return;
    }
    navigate('/');
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      {/* Cabeçalho do perfil */}
      <div className="flex flex-col sm:flex-row items-center gap-6 mb-8">
        <div className="w-24 h-24 rounded-full bg-aqua-500/10 flex items-center justify-center flex-shrink-0">
          <span className="text-3xl font-bold text-aqua-400">
            {perfil.nomeCompleto.charAt(0)}
          </span>
        </div>
        <div className="text-center sm:text-left flex-1">
          <h1 className="text-2xl font-bold text-dark-50">{perfil.nomeCompleto}</h1>
          <p className="text-sm text-dark-400">@{perfil.nomeUsuario}</p>
          <p className="text-sm text-dark-400">{perfil.email}</p>
        </div>
        <div className="flex gap-3">
          <Button variante="secundario" onClick={abrirEdicao}>Editar</Button>
          <Button variante="perigo" onClick={() => { setErroExclusao(''); resetExcluir(); setExcluindo(true); }}>
            Excluir conta
          </Button>
        </div>
      </div>

      {/* Cards de estatísticas */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <Card className="text-center">
          <p className="text-3xl font-bold text-aqua-400">{perfil.pontos}</p>
          <p className="text-xs text-dark-400 mt-1">Pontos totais</p>
        </Card>
        <Card className="text-center">
          <p className="text-3xl font-bold text-green-500">{perfil.missoesConcluidas}</p>
          <p className="text-xs text-dark-400 mt-1">Missões concluídas</p>
        </Card>
        <Card className="text-center">
          <p className="text-3xl font-bold text-yellow-400">{perfil.missoesEmAndamento}</p>
          <p className="text-xs text-dark-400 mt-1">Em andamento</p>
        </Card>
        <Card className="text-center">
          <p className="text-3xl font-bold text-neon-400">{perfil.taxaConclusao}%</p>
          <p className="text-xs text-dark-400 mt-1">Taxa de conclusão</p>
        </Card>
      </div>

      {/* Barra de progresso da taxa de conclusão */}
      <Card className="mb-8">
        <h3 className="text-sm font-medium text-dark-200 mb-3">Taxa de conclusão</h3>
        <div className="w-full h-3 rounded-full bg-dark-700 overflow-hidden">
          <div
            className="h-full rounded-full bg-gradient-to-r from-aqua-500 to-neon-400 transition-all duration-500"
            style={{ width: `${perfil.taxaConclusao}%` }}
          />
        </div>
        <p className="text-xs text-dark-400 mt-2">
          {perfil.taxaConclusao}% das missões iniciadas foram concluídas
        </p>
      </Card>

      {/* Últimas missões concluídas */}
      {missoesConcluidas.length > 0 && (
        <Card className="mb-8">
          <h3 className="text-lg font-semibold text-dark-100 mb-4">Missões concluídas</h3>
          <div className="space-y-3">
            {missoesConcluidas.map((missao) => {
              const categoria = categorias.find((c) => c.nome === missao.categoria);
              return (
                <div
                  key={missao.id}
                  className="flex items-center gap-3 p-3 rounded-xl bg-dark-800/40 hover:bg-dark-800/60 transition-colors"
                >
                  <div className={`w-10 h-10 rounded-lg ${categoria?.corFundo} ${categoria?.cor} flex items-center justify-center flex-shrink-0`}>
                    <IconeCategoria nome={categoria?.icone || ''} className="w-5 h-5" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-dark-100 truncate">{missao.titulo}</p>
                    <Badge cor={`${categoria?.cor} ${categoria?.corFundo}`}>{categoria?.label}</Badge>
                  </div>
                  <span className="text-sm font-bold text-aqua-400 flex-shrink-0">
                    +{missao.pontosRecompensa} pts
                  </span>
                </div>
              );
            })}
          </div>
        </Card>
      )}

      {/* Botões de ação */}
      <div className="flex flex-col sm:flex-row gap-4">
        <Link to="/missoes" className="flex-1">
          <Button variante="primario" className="w-full">Ver missões</Button>
        </Link>
        <Link to="/ranking" className="flex-1">
          <Button variante="secundario" className="w-full">Ver ranking</Button>
        </Link>
      </div>

      {/* Modal de edição */}
      {editando && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
          <Card className="max-w-md w-full">
            <h2 className="text-xl font-bold text-dark-50 mb-4">Editar perfil</h2>
            {erroEdicao && (
              <div className="mb-4 p-3 rounded-xl bg-red-500/10 border border-red-500/20 text-red-400 text-sm">
                {erroEdicao}
              </div>
            )}
            <form onSubmit={handleEditar(onSubmitEdicao)} className="space-y-4">
              <Input
                label="Nome de usuário"
                {...registerEditar('nomeUsuario', { required: 'Obrigatório' })}
                erro={errorsEditar.nomeUsuario?.message}
              />
              <Input
                label="Nome completo"
                {...registerEditar('nomeCompleto', { required: 'Obrigatório' })}
                erro={errorsEditar.nomeCompleto?.message}
              />
              <Input
                label="E-mail"
                type="email"
                {...registerEditar('email', { required: 'Obrigatório' })}
                erro={errorsEditar.email?.message}
              />
              <Input
                label="Senha"
                type="password"
                {...registerEditar('senha', { required: 'Obrigatório' })}
                erro={errorsEditar.senha?.message}
              />
              <div className="flex gap-3 pt-2">
                <Button type="submit" variante="primario" className="flex-1">Salvar</Button>
                <Button type="button" variante="secundario" onClick={() => setEditando(false)}>Cancelar</Button>
              </div>
            </form>
          </Card>
        </div>
      )}

      {/* Modal de exclusão */}
      {excluindo && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
          <Card className="max-w-md w-full">
            <h2 className="text-xl font-bold text-dark-50 mb-2">Excluir conta</h2>
            <p className="text-sm text-dark-400 mb-4">
              Tem certeza? Esta ação não pode ser desfeita. Digite sua senha para confirmar.
            </p>
            {erroExclusao && (
              <div className="mb-4 p-3 rounded-xl bg-red-500/10 border border-red-500/20 text-red-400 text-sm">
                {erroExclusao}
              </div>
            )}
            <form onSubmit={handleExcluir(onSubmitExclusao)} className="space-y-4">
              <Input
                label="Confirme sua senha"
                type="password"
                {...registerExcluir('senha', { required: 'A senha é obrigatória' })}
                erro={errorsExcluir.senha?.message}
              />
              <div className="flex gap-3 pt-2">
                <Button type="submit" variante="perigo" className="flex-1">Excluir conta</Button>
                <Button type="button" variante="secundario" onClick={() => setExcluindo(false)}>Cancelar</Button>
              </div>
            </form>
          </Card>
        </div>
      )}
    </div>
  );
}
