import { useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { useAppData } from '../../core/AppDataContext';

// Cabeçalho com logo, menu de navegação e menu hambúrguer no mobile
export default function Header() {
  const { usuarioLogado, logout } = useAppData();
  const [menuAberto, setMenuAberto] = useState(false);

  // Links que sempre aparecem (páginas institucionais)
  const linksInstitucionais = [
    { to: '/', label: 'Home' },
    { to: '/sobre', label: 'Sobre' },
    { to: '/solucao', label: 'Solução' },
    { to: '/faq', label: 'FAQ' },
    { to: '/contato', label: 'Contato' },
    { to: '/integrantes', label: 'Integrantes' },
  ];

  // Links extras que só aparecem quando o usuário está "logado"
  const linksLogado = [
    { to: '/missoes', label: 'Missões' },
    { to: '/ranking', label: 'Ranking' },
    { to: '/perfil', label: 'Perfil' },
  ];

  // Classe para o link ativo
  const classeAtivo = 'text-aqua-400 font-medium';
  const classeInativo = 'text-dark-300 hover:text-aqua-400 transition-colors duration-300';

  return (
    <header className="glass sticky top-0 z-50 border-b border-white/5">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to="/" className="flex items-center gap-2 group">
            <img src="/logo.png"
              alt="Logo"
              className="w-8 h-8 flex-shrink-0 rounded-full object-cover ring-2 ring-aqua-400/50 group-hover:scale-110 transition-transform duration-300"
            />
            <span className="text-xl font-bold text-aqua-400 neon-text">Kairós</span>
          </Link>

          {/* Menu desktop */}
          <nav className="hidden lg:flex items-center gap-6">
            {linksInstitucionais.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                className={({ isActive }) => isActive ? classeAtivo : classeInativo}
              >
                {link.label}
              </NavLink>
            ))}
            {usuarioLogado && linksLogado.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                className={({ isActive }) => isActive ? classeAtivo : classeInativo}
              >
                {link.label}
              </NavLink>
            ))}
            {usuarioLogado ? (
              <button
                onClick={logout}
                className="text-dark-300 hover:text-red-400 transition-colors duration-300 text-sm"
              >
                Sair
              </button>
            ) : (
              <NavLink
                to="/login"
                className={({ isActive }) => isActive ? classeAtivo : classeInativo}
              >
                Entrar
              </NavLink>
            )}
          </nav>

          {/* Botão hambúrguer no mobile */}
          <button
            className="lg:hidden text-dark-200 p-2"
            onClick={() => setMenuAberto(!menuAberto)}
            aria-label="Abrir menu"
          >
            {menuAberto ? (
              <svg className="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            ) : (
              <svg className="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <line x1="3" y1="12" x2="21" y2="12" />
                <line x1="3" y1="6" x2="21" y2="6" />
                <line x1="3" y1="18" x2="21" y2="18" />
              </svg>
            )}
          </button>
        </div>

        {/* Menu mobile — aparece quando menuAberto é true */}
        {menuAberto && (
          <nav className="lg:hidden flex flex-col gap-3 pb-4 animate-fade-in">
            {linksInstitucionais.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                onClick={() => setMenuAberto(false)}
                className={({ isActive }) =>
                  `text-sm py-1 ${isActive ? classeAtivo : classeInativo}`
                }
              >
                {link.label}
              </NavLink>
            ))}
            {usuarioLogado && linksLogado.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                onClick={() => setMenuAberto(false)}
                className={({ isActive }) =>
                  `text-sm py-1 ${isActive ? classeAtivo : classeInativo}`
                }
              >
                {link.label}
              </NavLink>
            ))}
            {usuarioLogado ? (
              <button
                onClick={() => {
                  logout();
                  setMenuAberto(false);
                }}
                className="text-sm text-left py-1 text-dark-300 hover:text-red-400 transition-colors duration-300"
              >
                Sair
              </button>
            ) : (
              <NavLink
                to="/login"
                onClick={() => setMenuAberto(false)}
                className={({ isActive }) =>
                  `text-sm py-1 ${isActive ? classeAtivo : classeInativo}`
                }
              >
                Entrar
              </NavLink>
            )}
          </nav>
        )}
      </div>
    </header>
  );
}
