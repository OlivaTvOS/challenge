import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, Link } from 'react-router-dom';
import type { FormLoginData } from '../../types';
import { useAppData } from '../../context/useAppData';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import Card from '../../components/ui/Card';
import { apiFetch } from '../../services/api';
import type { Usuario } from '../../types';

// Página de Login — chama login() e mostra erro real se não bater
export default function Login() {
  const navigate = useNavigate();
  const { login } = useAppData();
  const [erroLogin, setErroLogin] = useState('');

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormLoginData>();

  // Post do dados (email e senha)
  const onSubmit = async(data: FormLoginData) => {
    setErroLogin('');
    try {
      const usuarioLogado = await apiFetch<Usuario>('/api/usuarios/login', {
        method: 'POST',
        body: JSON.stringify(data), //Envio do email e senha
      });
      if (!usuarioLogado) throw new Error('A API não retornou os dados da conta.');

      login(usuarioLogado);

      navigate('/perfil')
    } catch (error: unknown) {
      console.error("Erro no login: ", error)
      setErroLogin(error instanceof Error ? error.message : "E-mail ou senha incorretos!");
    }
    
  }

  return (
    <div className="max-w-md mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <div className="text-center mb-8">
        <div className="w-16 h-16 rounded-2xl bg-aqua-500/10 flex items-center justify-center mx-auto mb-4">
          <svg className="w-8 h-8 text-aqua-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M11 20A7 7 0 0 1 9.8 6.1C15.5 5 17 4.48 19 2c1 2 2 4.18 2 8 0 5.5-4.78 10-10 10z" />
            <path d="M2 21c0-3 1.85-5.36 5.08-6" />
          </svg>
        </div>
        <h1 className="text-2xl font-bold text-dark-50">Entrar no SoulUp</h1>
        <p className="text-sm text-dark-400 mt-1">Acesse sua conta para continuar</p>
      </div>

      <Card>
        {/* Mensagem de erro do login */}
        {erroLogin && (
          <div className="mb-4 p-3 rounded-xl bg-red-500/10 border border-red-500/20 text-red-400 text-sm animate-fade-in">
            {erroLogin}
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
          <Input
            label="E-mail"
            type="email"
            placeholder="seu@email.com"
            {...register('email', {
              required: 'O e-mail é obrigatório',
              pattern: {
                value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                message: 'Digite um e-mail válido',
              },
            })}
            erro={errors.email?.message}
          />

          <Input
            label="Senha"
            type="password"
            placeholder="Sua senha"
            {...register('senha', {
              required: 'A senha é obrigatória',
            })}
            erro={errors.senha?.message}
          />

          <Button type="submit" variante="primario" className="w-full">
            Entrar
          </Button>
        </form>

        <p className="text-sm text-dark-400 text-center mt-5">
          Não tem conta?{' '}
          <Link to="/cadastro" className="text-aqua-400 hover:text-aqua-300 transition-colors">
            Criar conta
          </Link>
        </p>
      </Card>
    </div>
  );
}
