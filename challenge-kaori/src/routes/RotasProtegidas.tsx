import { Navigate, Outlet } from 'react-router-dom';
import { useAppData } from '../context/useAppData';

export function RotaUsuario() {
  const { usuarioLogado, carregandoSessao } = useAppData();

  if (carregandoSessao) {
    return <div className="py-12 text-center text-dark-400">Verificando sua sessao...</div>;
  }

  return usuarioLogado ? <Outlet /> : <Navigate to="/login" replace />;
}
