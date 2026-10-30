import { Navigate, Outlet } from 'react-router-dom';
import { useAppData } from '../context/useAppData';

export function RotaUsuario() {
  const { usuarioLogado } = useAppData();

  return usuarioLogado
    ? <Outlet />
    : <Navigate to="/login" replace />;
}
