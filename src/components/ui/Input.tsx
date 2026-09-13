import { forwardRef, type InputHTMLAttributes } from 'react';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  erro?: string;
}

// Campo de formulário com label e mensagem de erro.
// Usa forwardRef porque o react-hook-form precisa de uma ref direta no
// <input> real do navegador para conseguir ler o valor digitado — sem isso,
// o campo sempre aparece como "vazio" para o formulário, mesmo preenchido.
const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, erro, className = '', ...props }, ref) => {
    return (
      <div className="flex flex-col gap-1.5">
        {label && (
          <label className="text-sm font-medium text-dark-200">{label}</label>
        )}
        <input
          ref={ref}
          className={`px-4 py-3 rounded-xl bg-dark-800/60 border text-dark-100 placeholder-dark-400
            focus:outline-none focus:border-aqua-500 focus:ring-1 focus:ring-aqua-500
            transition-all duration-300
            ${erro ? 'border-red-500' : 'border-dark-600'}
            ${className}`}
          {...props}
        />
        {erro && <span className="text-xs text-red-400">{erro}</span>}
      </div>
    );
  }
);

Input.displayName = 'Input';

export default Input;
