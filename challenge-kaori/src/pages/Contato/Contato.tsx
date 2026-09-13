import { useState } from 'react';
import { useForm } from 'react-hook-form';
import type { FormContatoData } from '../../types';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import Card from '../../components/ui/Card';

// Página de Contato — formulário com validação (sem envio real)
export default function Contato() {
  const [enviado, setEnviado] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<FormContatoData>();

  // Simula o envio da mensagem — apenas mostra feedback visual
  function onSubmit(_data: FormContatoData) {
    setEnviado(true);
    reset();
    // Esconde a mensagem de sucesso depois de 3 segundos
    setTimeout(() => setEnviado(false), 3000);
  }

  return (
    <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-12 animate-fade-in">
      <h1 className="text-3xl sm:text-4xl font-bold text-dark-50 mb-2">Contato</h1>
      <p className="text-dark-400 mb-8">
        Tem alguma dúvida ou sugestão? Envie uma mensagem para a equipe.
      </p>

      <Card>
        {/* Mensagem de sucesso */}
        {enviado && (
          <div className="mb-6 p-4 rounded-xl bg-aqua-500/10 border border-aqua-500/20 text-aqua-300 text-sm animate-fade-in">
            Mensagem enviada com sucesso! Agradecemos o seu contato.
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
          <Input
            label="Nome"
            placeholder="Seu nome completo"
            {...register('nome', { required: 'O nome é obrigatório' })}
            erro={errors.nome?.message}
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

          {/* Campo de mensagem (textarea) */}
          <div className="flex flex-col gap-1.5">
            <label className="text-sm font-medium text-dark-200">Mensagem</label>
            <textarea
              rows={5}
              placeholder="Escreva sua mensagem aqui..."
              className={`px-4 py-3 rounded-xl bg-dark-800/60 border text-dark-100 placeholder-dark-400
                focus:outline-none focus:border-aqua-500 focus:ring-1 focus:ring-aqua-500
                transition-all duration-300 resize-none
                ${errors.mensagem ? 'border-red-500' : 'border-dark-600'}`}
              {...register('mensagem', { required: 'A mensagem é obrigatória' })}
            />
            {errors.mensagem && (
              <span className="text-xs text-red-400">{errors.mensagem.message}</span>
            )}
          </div>

          <Button type="submit" variante="primario" className="w-full">
            Enviar mensagem
          </Button>
        </form>
      </Card>
    </div>
  );
}
