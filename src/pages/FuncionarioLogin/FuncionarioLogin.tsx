import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useNavigate } from 'react-router-dom';
import type { FormLoginFuncionarioData } from '../../types';
import { useAppData } from '../../core/AppDataContext';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import Card from '../../components/ui/Card';

export default function FuncionarioLogin() {
  const { loginFuncionario } = useAppData();
  const navigate = useNavigate();
  const [erro, setErro] = useState('');
  const { register, handleSubmit, formState: { errors } } = useForm<FormLoginFuncionarioData>();

  function onSubmit(data: FormLoginFuncionarioData) {
    const resultado = loginFuncionario(Number(data.id), data.email, data.senha);
    if (!resultado.sucesso) return setErro(resultado.mensagem);
    navigate('/funcionario');
  }

  return <div className="max-w-md mx-auto px-4 py-12 animate-fade-in">
    <h1 className="text-2xl font-bold text-dark-50 text-center mb-2">Acesso de funcionário</h1>
    <p className="text-sm text-dark-400 text-center mb-8">Simulação acadêmica de autenticação e autorização</p>
    <Card>
      {erro && <p className="mb-4 p-3 rounded-xl bg-red-500/10 text-red-400 text-sm">{erro}</p>}
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
        <Input label="ID" type="number" {...register('id', { required: 'O ID é obrigatório', min: { value: 1, message: 'O ID deve estar entre 1 e 200' }, max: { value: 200, message: 'O ID deve estar entre 1 e 200' } })} erro={errors.id?.message} />
        <Input label="E-mail corporativo" type="email" {...register('email', { required: 'O e-mail é obrigatório' })} erro={errors.email?.message} />
        <Input label="Senha" type="password" {...register('senha', { required: 'A senha é obrigatória' })} erro={errors.senha?.message} />
        <Button type="submit" className="w-full">Entrar</Button>
      </form>
      <Link to="/cadastro-funcionario" className="block text-center text-sm text-aqua-400 mt-5">Cadastrar funcionário para demonstração</Link>
    </Card>
  </div>;
}
