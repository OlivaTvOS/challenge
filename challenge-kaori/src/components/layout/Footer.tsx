import { Link } from 'react-router-dom';

// Rodapé fixo com links rápidos e informações do projeto
export default function Footer() {
  return (
    <footer className="glass border-t border-white/5 mt-auto">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-8">
          {/* Logo e descrição */}
          <div className="flex flex-col gap-2">
            <div className="flex items-center gap-2">
              <svg className="w-6 h-6 text-aqua-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M12 2.69l5.66 5.66a8 8 0 1 1-11.31 0z" />
              </svg>
              <span className="text-lg font-bold text-aqua-400">SoulUp</span>
            </div>
            <p className="text-sm text-dark-400 max-w-xs">
              Gamificação da sustentabilidade. Cada ação conta para um planeta melhor.
            </p>
          </div>

          {/* Links rápidos */}
          <div className="flex flex-col gap-2">
            <h3 className="text-sm font-semibold text-dark-200 mb-1">Navegação</h3>
            <Link to="/" className="text-sm text-dark-400 hover:text-aqua-400 transition-colors">Home</Link>
            <Link to="/sobre" className="text-sm text-dark-400 hover:text-aqua-400 transition-colors">Sobre</Link>
            <Link to="/solucao" className="text-sm text-dark-400 hover:text-aqua-400 transition-colors">Solução</Link>
            <Link to="/faq" className="text-sm text-dark-400 hover:text-aqua-400 transition-colors">FAQ</Link>
          </div>

          {/* Links de conta */}
          <div className="flex flex-col gap-2">
            <h3 className="text-sm font-semibold text-dark-200 mb-1">Conta</h3>
            <Link to="/login" className="text-sm text-dark-400 hover:text-aqua-400 transition-colors">Entrar</Link>
            <Link to="/cadastro" className="text-sm text-dark-400 hover:text-aqua-400 transition-colors">Criar conta</Link>
            <Link to="/contato" className="text-sm text-dark-400 hover:text-aqua-400 transition-colors">Contato</Link>
            <Link to="/integrantes" className="text-sm text-dark-400 hover:text-aqua-400 transition-colors">Integrantes</Link>
          </div>
        </div>

        <div className="mt-8 pt-4 border-t border-white/5">
          <p className="text-xs text-dark-500 text-center">
            Kairós — Challenge de Análise e Desenvolvimento de Sistemas · 2026
          </p>
        </div>
      </div>
    </footer>
  );
}
