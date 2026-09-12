import { useEffect, useState } from 'react';
import { Link, NavLink, useLocation, useNavigate } from 'react-router-dom';
import { useAppData } from '../../core/AppDataContext';

export default function Header() {
  const { usuarioLogado, funcionarioLogado, logout, logoutFuncionario } = useAppData();
  const [menuAberto, setMenuAberto] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => setMenuAberto(false), [location.pathname]);

  const institucionais = [
    { to: '/', label: 'Home' }, { to: '/sobre', label: 'Sobre' },
    { to: '/solucao', label: 'Solução' }, { to: '/faq', label: 'FAQ' },
    { to: '/contato', label: 'Contato' }, { to: '/integrantes', label: 'Integrantes' },
  ];
  const usuario = [{ to: '/missoes', label: 'Missões' }, { to: '/ranking', label: 'Ranking' }, { to: '/perfil', label: 'Perfil' }];
  const funcionario = [{ to: '/funcionario', label: 'Painel do funcionário' }];
  const classeAtivo = 'text-aqua-400 font-medium';
  const classeInativo = 'text-dark-300 hover:text-aqua-400 transition-colors duration-300';
  const linksSessao = funcionarioLogado ? funcionario : usuarioLogado ? usuario : [];

  function sair() {
    if (funcionarioLogado) logoutFuncionario(); else logout();
    navigate('/');
  }

  function Links() {
    return <>
      {institucionais.map((link) => <NavLink key={link.to} to={link.to} className={({ isActive }) => isActive ? classeAtivo : classeInativo}>{link.label}</NavLink>)}
      {linksSessao.map((link) => <NavLink key={link.to} to={link.to} className={({ isActive }) => isActive ? classeAtivo : classeInativo}>{link.label}</NavLink>)}
      {usuarioLogado || funcionarioLogado ? <button onClick={sair} className="text-dark-300 hover:text-red-400 text-sm">Sair</button> : <><NavLink to="/login" className={({ isActive }) => isActive ? classeAtivo : classeInativo}>Entrar</NavLink><NavLink to="/login-funcionario" className={({ isActive }) => isActive ? classeAtivo : classeInativo}>Funcionário</NavLink></>}
    </>;
  }

  return <header className="glass sticky top-0 z-50 border-b border-white/5">
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div className="flex items-center justify-between h-16">
        <Link to="/" className="flex items-center gap-2 group"><img src="/logo.png" alt="Logo Kairós" className="w-8 h-8 rounded-full object-cover ring-2 ring-aqua-400/50"/><span className="text-xl font-bold text-aqua-400 neon-text">Kairós</span></Link>
        <nav className="hidden lg:flex items-center gap-6"><Links /></nav>
        <button className="lg:hidden text-dark-200 p-2" onClick={() => setMenuAberto(!menuAberto)} aria-label="Abrir menu"><span className="text-xl">{menuAberto ? '×' : '☰'}</span></button>
      </div>
      {menuAberto && <nav className="lg:hidden flex flex-col gap-3 pb-4 text-sm"><Links /></nav>}
    </div>
  </header>;
}
