import type { ButtonHTMLAttributes, ReactNode } from 'react';

// Variantes de estilo do botão
type Variante = 'primario' | 'secundario' | 'fantasma' | 'perigo';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variante?: Variante;
  children: ReactNode;
}

// Mapeia cada variante para suas classes Tailwind
const estilos: Record<Variante, string> = {
  primario:
    'bg-aqua-500 hover:bg-aqua-600 text-dark-950 font-semibold transition-all duration-300 hover:shadow-lg hover:shadow-aqua-500/30',
  secundario:
    'glass text-aqua-300 hover:text-aqua-200 border-aqua-500/30 hover:border-aqua-500/50 transition-all duration-300',
  fantasma:
    'text-dark-300 hover:text-aqua-400 transition-colors duration-300',
  perigo:
    'bg-red-500/80 hover:bg-red-500 text-white font-medium transition-all duration-300',
};

export default function Button({
  variante = 'primario',
  children,
  className = '',
  ...props
}: ButtonProps) {
  return (
    <button
      className={`px-5 py-2.5 rounded-xl text-sm sm:text-base ${estilos[variante]} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}
