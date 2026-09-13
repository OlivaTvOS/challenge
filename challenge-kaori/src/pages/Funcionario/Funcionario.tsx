import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useAppData } from '../../core/AppDataContext';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';

interface FormEdicao { nomeCompleto: string; email: string }
interface FormExclusao { confirmacao: string }

export default function Funcionario() {
  const {
    funcionarioLogado, temPermissao, listarUsuariosParaFuncionario,
    atualizarUsuarioComoFuncionario, removerUsuarioComoFuncionario,
    listarFuncionarios, listarMissoesDoUsuario, obterResumoAdministrativo,
  } = useAppData();
  const [usuarioSelecionado, setUsuarioSelecionado] = useState<string | null>(null);
  const [usuarioExcluir, setUsuarioExcluir] = useState<string | null>(null);
  const [mensagem, setMensagem] = useState('');
  const { register: registerEdicao, handleSubmit: handleEdicao, reset: resetEdicao, formState: { errors: errosEdicao } } = useForm<FormEdicao>();
  const { register: registerExclusao, handleSubmit: handleExclusao, reset: resetExclusao, formState: { errors: errosExclusao } } = useForm<FormExclusao>();

  if (!funcionarioLogado) return null;
  const usuarios = listarUsuariosParaFuncionario();
  const funcionarios = listarFuncionarios();
  const resumo = obterResumoAdministrativo();

  function abrirEdicao(id: string, nomeCompleto: string, email: string) {
    setUsuarioSelecionado(id);
    resetEdicao({ nomeCompleto, email });
  }
  function editar(data: FormEdicao) {
    if (!usuarioSelecionado) return;
    const resultado = atualizarUsuarioComoFuncionario(usuarioSelecionado, data.nomeCompleto, data.email);
    setMensagem(resultado.mensagem);
    if (resultado.sucesso) setUsuarioSelecionado(null);
  }
  function excluir(data: FormExclusao) {
    if (!usuarioExcluir) return;
    if (data.confirmacao !== 'EXCLUIR') return setMensagem('Digite EXCLUIR para confirmar.');
    const resultado = removerUsuarioComoFuncionario(usuarioExcluir);
    setMensagem(resultado.mensagem);
    if (resultado.sucesso) { setUsuarioExcluir(null); resetExclusao(); }
  }

  return <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
    <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
      <div><h1 className="text-3xl font-bold text-dark-50">Painel do funcionário</h1><p className="text-dark-400">{funcionarioLogado.nome} {funcionarioLogado.sobrenome} · {funcionarioLogado.email}</p></div>
      <Badge>{funcionarioLogado.cargo}</Badge>
    </div>
    <Card className="mb-6 border-yellow-500/20"><p className="text-sm text-yellow-200">Simulação acadêmica: em uma aplicação real, o servidor também precisaria verificar todas as permissões.</p></Card>
    {mensagem && <div className="mb-6 p-3 rounded-xl bg-aqua-500/10 text-aqua-300 text-sm">{mensagem}</div>}

    {resumo && <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
      <Card><p className="text-2xl font-bold text-aqua-400">{resumo.totalUsuarios}</p><p className="text-xs text-dark-400">Usuários</p></Card>
      <Card><p className="text-2xl font-bold text-aqua-400">{resumo.totalMissoes}</p><p className="text-xs text-dark-400">Missões</p></Card>
      <Card><p className="text-2xl font-bold text-yellow-400">{resumo.missoesEmAndamento}</p><p className="text-xs text-dark-400">Em andamento</p></Card>
      <Card><p className="text-2xl font-bold text-green-500">{resumo.missoesConcluidas}</p><p className="text-xs text-dark-400">Concluídas</p></Card>
    </div>}

    <h2 className="text-xl font-semibold text-dark-100 mb-4">Usuários</h2>
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-8">
      {usuarios.map((usuario) => <Card key={usuario.id}>
        <p className="font-semibold text-dark-100">@{usuario.nomeUsuario}</p><p className="text-sm text-dark-400">{usuario.nomeCompleto}</p><p className="text-sm text-dark-400">{usuario.email}</p><p className="text-sm text-aqua-400 mt-2">{usuario.pontos} pontos</p>
        {temPermissao('CONSULTAR_MISSOES_USUARIO') && <p className="text-xs text-dark-400 mt-2">Relações de missão: {listarMissoesDoUsuario(usuario.id).length}</p>}
        <div className="flex gap-2 mt-4">
          {temPermissao('EDITAR_DADOS_BASICOS') && <Button variante="secundario" onClick={() => abrirEdicao(usuario.id, usuario.nomeCompleto, usuario.email)}>Editar dados</Button>}
          {temPermissao('EXCLUIR_USUARIO') && <Button variante="perigo" onClick={() => { setUsuarioExcluir(usuario.id); resetExclusao(); }}>Excluir</Button>}
        </div>
      </Card>)}
    </div>

    {temPermissao('CONSULTAR_FUNCIONARIOS') && <><h2 className="text-xl font-semibold text-dark-100 mb-4">Funcionários</h2><div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">{funcionarios.map((f) => <Card key={f.id}><p className="font-semibold">{f.nome} {f.sobrenome}</p><p className="text-sm text-dark-400">ID {f.id}</p><p className="text-sm text-dark-400">{f.email}</p><Badge>{f.cargo}</Badge></Card>)}</div></>}

    {usuarioSelecionado && <div className="fixed inset-0 z-50 bg-black/60 flex items-center justify-center p-4"><Card className="max-w-md w-full"><h2 className="text-xl font-bold mb-4">Editar dados básicos</h2><form onSubmit={handleEdicao(editar)} className="space-y-4"><Input label="Nome completo" {...registerEdicao('nomeCompleto', { required: 'Obrigatório' })} erro={errosEdicao.nomeCompleto?.message}/><Input label="E-mail" type="email" {...registerEdicao('email', { required: 'Obrigatório' })} erro={errosEdicao.email?.message}/><div className="flex gap-2"><Button type="submit">Salvar</Button><Button type="button" variante="secundario" onClick={() => setUsuarioSelecionado(null)}>Cancelar</Button></div></form></Card></div>}
    {usuarioExcluir && <div className="fixed inset-0 z-50 bg-black/60 flex items-center justify-center p-4"><Card className="max-w-md w-full"><h2 className="text-xl font-bold mb-2">Excluir usuário</h2><p className="text-sm text-dark-400 mb-4">Digite EXCLUIR para confirmar.</p><form onSubmit={handleExclusao(excluir)} className="space-y-4"><Input {...registerExclusao('confirmacao', { required: 'Confirmação obrigatória' })} erro={errosExclusao.confirmacao?.message}/><div className="flex gap-2"><Button type="submit" variante="perigo">Excluir</Button><Button type="button" variante="secundario" onClick={() => setUsuarioExcluir(null)}>Cancelar</Button></div></form></Card></div>}
  </div>;
}
