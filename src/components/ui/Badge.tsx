import type { ReactNode } from 'react';

interface BadgeProps {
  children: ReactNode;
  cor?: string;
}

// Pequeno rótulo colorido usado para categorias e status
export default function Badge({ children, cor = 'text-aqua-400 bg-aqua-500/10' }: BadgeProps) {
  return (
    <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${cor}`}>
      {children}
    </span>
  );
}
