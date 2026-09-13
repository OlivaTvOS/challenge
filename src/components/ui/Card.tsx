import type { ReactNode } from 'react';

interface CardProps {
  children: ReactNode;
  className?: string;
}

// Card com efeito glassmorphism usado em toda a aplicação
export default function Card({ children, className = '' }: CardProps) {
  return (
    <div className={`glass-card rounded-2xl p-6 transition-all duration-300 hover:border-aqua-500/20 ${className}`}>
      {children}
    </div>
  );
}
