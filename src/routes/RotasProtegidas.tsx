import { Navigate, Outlet } from 'react-router-dom';
import { useAppData } from '../core/AppDataContext';

export function RotaUsuario() {
  const { usuarioLogado } = useAppData();

  return usuarioLogado
    ? <Outlet />
    : <Navigate to="/login" replace />;
}
