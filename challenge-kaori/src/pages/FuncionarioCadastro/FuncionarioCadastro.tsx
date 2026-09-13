import { useState } from 'react';
import { useForm } from 'react-hook-form';
import type { FormCadastroFuncionarioData } from '../../types';
import { useAppData } from '../../core/AppDataContext';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import Card from '../../components/ui/Card';

export default function FuncionarioCadastro() {
  const { criarFuncionario } = useAppData();
  const [erro, setErro] = useState('');
  const [credenciais, setCredenciais] = useState<{ id: number; email: string; senha: string } | null>(null);
  const { register, handleSubmit, reset, formState: { errors } } = useForm<FormCadastroFuncionarioData>();

  function onSubmit(data: FormCadastroFuncionarioData) {
    setCredenciais(null);
    const resultado = criarFuncionario(data.nome, data.sobrenome, data.cargo);
    if (!resultado.sucesso) return setErro(resultado.mensagem);
    setErro('');
    setCredenciais(resultado.credenciais ?? null);
    reset();
  }

  return <div className="max-w-lg mx-auto px-4 py-12 animate-fade-in">
    <div className="mb-8 p-4 rounded-2xl border border-yellow-500/30 bg-yellow-500/10 text-yellow-200 text-sm">
      <strong className="block mb-2">MODO DE DEMONSTRAÇÃO ACADÊMICA</strong>
      Esta tela demonstra a criação de uma conta de funcionário. Em um sistema real, ela não estaria disponível publicamente.
    </div>
    <Card>
      <h1 className="text-2xl font-bold text-dark-50 mb-5">Cadastrar funcionário</h1>
      {erro && <p className="mb-4 text-red-400 text-sm">{erro}</p>}
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
        <Input label="Nome" {...register('nome', { required: 'O nome é obrigatório' })} erro={errors.nome?.message} />
        <Input label="Último sobrenome" {...register('sobrenome', { required: 'O sobrenome é obrigatório' })} erro={errors.sobrenome?.message} />
        <div className="flex flex-col gap-1.5">
          <label className="text-sm font-medium text-dark-200">Cargo</label>
          <select className="px-4 py-3 rounded-xl bg-dark-800/60 border border-dark-600 text-dark-100" {...register('cargo', { required: 'O cargo é obrigatório' })}>
            <option value="">Selecione</option><option value="ADMIN">ADMIN</option><option value="TECH">TECH</option><option value="CEO">CEO</option>
          </select>
          {errors.cargo && <span className="text-xs text-red-400">{errors.cargo.message}</span>}
        </div>
        <Button type="submit" className="w-full">Gerar credenciais</Button>
      </form>
      {credenciais && <div className="mt-6 p-4 rounded-xl bg-aqua-500/10 border border-aqua-500/20 text-sm space-y-2">
        <p className="text-aqua-300 font-semibold">Funcionário criado. Guarde estes dados:</p>
        <p>ID: {credenciais.id}</p><p>E-mail: {credenciais.email}</p><p>Senha: {credenciais.senha}</p>
      </div>}
    </Card>
  </div>;
}
