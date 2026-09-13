// Ícones SVG simples e inline — sem bibliotecas externas

interface IconProps {
  className?: string;
}

// Ícone de reciclagem
export function IconReciclagem({ className = 'w-6 h-6' }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M7 19l-3-3 3-3" />
      <path d="M4 16h11a4 4 0 0 0 0-8" />
      <path d="M17 5l3 3-3 3" />
      <path d="M20 8H9a4 4 0 0 0 0 8" />
    </svg>
  );
}

// Ícone de mobilidade (bicicleta)
export function IconMobilidade({ className = 'w-6 h-6' }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="5.5" cy="17.5" r="3.5" />
      <circle cx="18.5" cy="17.5" r="3.5" />
      <path d="M15 6a1 1 0 1 0 0-2 1 1 0 0 0 0 2zM12 17.5L8 9l-3 2.5L2 9" />
      <path d="M5.5 17.5L9 9h5l2 4h4" />
    </svg>
  );
}

// Ícone de água (gota)
export function IconAgua({ className = 'w-6 h-6' }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M12 2.69l5.66 5.66a8 8 0 1 1-11.31 0z" />
    </svg>
  );
}

// Ícone de energia (raio)
export function IconEnergia({ className = 'w-6 h-6' }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2" />
    </svg>
  );
}

// Ícone de natureza (folha)
export function IconNatureza({ className = 'w-6 h-6' }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M11 20A7 7 0 0 1 9.8 6.1C15.5 5 17 4.48 19 2c1 2 2 4.18 2 8 0 5.5-4.78 10-10 10z" />
      <path d="M2 21c0-3 1.85-5.36 5.08-6" />
    </svg>
  );
}

// Ícone de consumo (sacola)
export function IconConsumo({ className = 'w-6 h-6' }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z" />
      <line x1="3" y1="6" x2="21" y2="6" />
      <path d="M16 10a4 4 0 0 1-8 0" />
    </svg>
  );
}

// Componente que retorna o ícone certo de acordo com o nome da categoria
export function IconeCategoria({ nome, className }: { nome: string; className?: string }) {
  switch (nome) {
    case 'reciclagem':
      return <IconReciclagem className={className} />;
    case 'mobilidade':
      return <IconMobilidade className={className} />;
    case 'agua':
      return <IconAgua className={className} />;
    case 'energia':
      return <IconEnergia className={className} />;
    case 'natureza':
      return <IconNatureza className={className} />;
    case 'consumo':
      return <IconConsumo className={className} />;
    default:
      return null;
  }
}
