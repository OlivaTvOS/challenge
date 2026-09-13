import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, Link } from 'react-router-dom';
import type { FormCadastroData } from '../../types';
import { useAppData } from '../../core/AppDataContext';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import Card from '../../components/ui/Card';

// Página de Cadastro — chama criarUsuario() e mostra erros específicos de cada validação
export default function Cadastro() {
  const navigate = useNavigate();
  const { criarUsuario } = useAppData();
  const [erroCadastro, setErroCadastro] = useState('');

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<FormCadastroData>();

  const senha = watch('senha');

  function onSubmit(data: FormCadastroData) {
    setErroCadastro('');
    const resultado = criarUsuario(data.nomeUsuario, data.nomeCompleto, data.email, data.senha);
    if (!resultado.sucesso) {
      setErroCadastro(resultado.mensagem);
      return;
    }
    // Cadastro com sucesso — já loga automaticamente e vai pro perfil
    navigate('/perfil');
  }

  return (
    <div className="max-w-md mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <div className="text-center mb-8">
        <div className="w-16 h-16 rounded-2xl bg-aqua-500/10 flex items-center justify-center mx-auto mb-4">
          <svg className="w-8 h-8 text-aqua-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
            <circle cx="8.5" cy="7" r="4" />
            <line x1="20" y1="8" x2="20" y2="14" />
            <line x1="23" y1="11" x2="17" y2="11" />
          </svg>
        </div>
        <h1 className="text-2xl font-bold text-dark-50">Criar conta</h1>
        <p className="text-sm text-dark-400 mt-1">Junte-se ao SoulUp e comece suas missões</p>
      </div>

      <Card>
        {/* Mensagem de erro do cadastro (validação de negócio) */}
        {erroCadastro && (
          <div className="mb-4 p-3 rounded-xl bg-red-500/10 border border-red-500/20 text-red-400 text-sm animate-fade-in">
            {erroCadastro}
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
          <Input
            label="Nome de usuário"
            placeholder="ex: eco_guerreiro"
            {...register('nomeUsuario', {
              required: 'O nome de usuário é obrigatório',
              minLength: {
                value: 3,
                message: 'Deve ter no mínimo 3 caracteres',
              },
            })}
            erro={errors.nomeUsuario?.message}
          />

          <Input
            label="Nome completo"
            placeholder="Seu nome completo"
            {...register('nomeCompleto', {
              required: 'O nome completo é obrigatório',
            })}
            erro={errors.nomeCompleto?.message}
          />

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
            placeholder="Mínimo 6 caracteres"
            {...register('senha', {
              required: 'A senha é obrigatória',
              minLength: {
                value: 6,
                message: 'A senha deve ter no mínimo 6 caracteres',
              },
            })}
            erro={errors.senha?.message}
          />

          <Input
            label="Confirmar senha"
            type="password"
            placeholder="Repita sua senha"
            {...register('confirmarSenha', {
              required: 'A confirmação é obrigatória',
              validate: (valor) => valor === senha || 'As senhas não coincidem',
            })}
            erro={errors.confirmarSenha?.message}
          />

          <Button type="submit" variante="primario" className="w-full">
            Criar conta
          </Button>
        </form>

        <p className="text-sm text-dark-400 text-center mt-5">
          Já tem conta?{' '}
          <Link to="/login" className="text-aqua-400 hover:text-aqua-300 transition-colors">
            Entrar
          </Link>
        </p>
      </Card>
    </div>
  );
}
