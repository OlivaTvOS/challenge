import { useEffect, useState } from 'react';
import { Navigate, Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAppData } from '../../context/useAppData';
import type { FormEditarPerfilData } from '../../types';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import { categorias } from '../../data/categorias';
import { IconeCategoria } from '../../components/ui/Icones';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';

export default function Perfil() {
  const {
    statusPerfil,
    atualizarPerfil,
    removerPerfil,
    listarMissoesConcluidas,
    carregarMissoesUsuario,
    usuarioLogado,
  } = useAppData();
  const navigate = useNavigate();

  const [editando, setEditando] = useState(false);
  const [excluindo, setExcluindo] = useState(false);
  const [erroEdicao, setErroEdicao] = useState('');
  const [erroExclusao, setErroExclusao] = useState('');
  const [erroMissoes, setErroMissoes] = useState('');

  useEffect(() => {
    let cancelado = false;
    void carregarMissoesUsuario().then((resultado) => {
      if (!cancelado && !resultado.sucesso) {
        setErroMissoes(resultado.mensagem);
      }
    });
    return () => {
      cancelado = true;
    };
  }, [carregarMissoesUsuario]);

  const {
    register: registerEditar,
    handleSubmit: handleEditar,
    reset: resetEditar,
    formState: { errors: errorsEditar },
  } = useForm<FormEditarPerfilData>();

  const {
    register: registerExcluir,
    handleSubmit: handleExcluir,
    reset: resetExcluir,
    formState: { errors: errorsExcluir },
  } = useForm<{ senha: string }>();

  if (!usuarioLogado) {
    return <Navigate to="/login" replace />;
  }

  const perfil = statusPerfil();
  if (!perfil) {
    return <Navigate to="/login" replace />;
  }

  const missoesConcluidas = listarMissoesConcluidas(usuarioLogado.id);

  function abrirEdicao() {
    setErroEdicao('');
    resetEditar({
      nomeUsuario: perfil.nomeUsuario,
      nomeCompleto: perfil.nomeCompleto,
      email: perfil.email,
      senha: '',
    });
    setEditando(true);
  }

  async function onSubmitEdicao(data: FormEditarPerfilData) {
    setErroEdicao('');
    const resultado = await atualizarPerfil(
      data.nomeUsuario,
      data.nomeCompleto,
      data.email,
      data.senha,
    );
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
          <Button variante="perigo" onClick={() => {
            setErroExclusao('');
            resetExcluir();
            setExcluindo(true);
          }}>
            Excluir conta
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <Card className="text-center">
          <p className="text-3xl font-bold text-aqua-400">{perfil.pontos}</p>
          <p className="text-xs text-dark-400 mt-1">Pontos totais</p>
        </Card>
        <Card className="text-center">
          <p className="text-3xl font-bold text-green-500">{perfil.missoesConcluidas}</p>
          <p className="text-xs text-dark-400 mt-1">Missoes concluidas</p>
        </Card>
        <Card className="text-center">
          <p className="text-3xl font-bold text-yellow-400">{perfil.missoesEmAndamento}</p>
          <p className="text-xs text-dark-400 mt-1">Em andamento</p>
        </Card>
        <Card className="text-center">
          <p className="text-3xl font-bold text-neon-400">{perfil.taxaConclusao}%</p>
          <p className="text-xs text-dark-400 mt-1">Taxa de conclusao</p>
        </Card>
      </div>

      {erroMissoes && <p className="mb-4 text-sm text-red-400">{erroMissoes}</p>}

      <Card className="mb-8">
        <h3 className="text-sm font-medium text-dark-200 mb-3">Taxa de conclusao</h3>
        <div className="w-full h-3 rounded-full bg-dark-700 overflow-hidden">
          <div
            className="h-full rounded-full bg-gradient-to-r from-aqua-500 to-neon-400 transition-all duration-500"
            style={{ width: `${perfil.taxaConclusao}%` }}
          />
        </div>
        <p className="text-xs text-dark-400 mt-2">
          {perfil.taxaConclusao}% das missoes iniciadas foram concluidas
        </p>
      </Card>

      {missoesConcluidas.length > 0 && (
        <Card className="mb-8">
          <h3 className="text-lg font-semibold text-dark-100 mb-4">Missoes concluidas</h3>
          <div className="space-y-3">
            {missoesConcluidas.map((missao) => {
              const categoria = categorias.find((item) => item.nome === missao.categoria);
              return (
                <div key={missao.id} className="flex items-center gap-3 p-3 rounded-xl bg-dark-800/40 hover:bg-dark-800/60 transition-colors">
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

      <div className="flex flex-col sm:flex-row gap-4">
        <Link to="/missoes" className="flex-1">
          <Button variante="primario" className="w-full">Ver missoes</Button>
        </Link>
        <Link to="/ranking" className="flex-1">
          <Button variante="secundario" className="w-full">Ver ranking</Button>
        </Link>
      </div>

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
              <Input label="Nome de usuario" {...registerEditar('nomeUsuario', { required: 'Obrigatorio' })} erro={errorsEditar.nomeUsuario?.message} />
              <Input label="Nome completo" {...registerEditar('nomeCompleto', { required: 'Obrigatorio' })} erro={errorsEditar.nomeCompleto?.message} />
              <Input label="E-mail" type="email" {...registerEditar('email', { required: 'Obrigatorio' })} erro={errorsEditar.email?.message} />
              <Input label="Nova senha (opcional)" type="password" {...registerEditar('senha')} erro={errorsEditar.senha?.message} />
              <p className="text-xs text-dark-500">Deixe a nova senha vazia para manter a senha atual.</p>
              <div className="flex gap-3 pt-2">
                <Button type="submit" variante="primario" className="flex-1">Salvar</Button>
                <Button type="button" variante="secundario" onClick={() => setEditando(false)}>Cancelar</Button>
              </div>
            </form>
          </Card>
        </div>
      )}

      {excluindo && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
          <Card className="max-w-md w-full">
            <h2 className="text-xl font-bold text-dark-50 mb-2">Excluir conta</h2>
            <p className="text-sm text-dark-400 mb-4">
              Esta acao nao pode ser desfeita. Digite sua senha para confirmar.
            </p>
            {erroExclusao && (
              <div className="mb-4 p-3 rounded-xl bg-red-500/10 border border-red-500/20 text-red-400 text-sm">
                {erroExclusao}
              </div>
            )}
            <form onSubmit={handleExcluir(onSubmitExclusao)} className="space-y-4">
              <Input label="Confirme sua senha" type="password" {...registerExcluir('senha', { required: 'A senha e obrigatoria' })} erro={errorsExcluir.senha?.message} />
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
