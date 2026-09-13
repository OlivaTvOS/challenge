import { Navigate, Outlet } from 'react-router-dom';
import { useAppData } from '../core/AppDataContext';

export function RotaUsuario() {
  const { usuarioLogado } = useAppData();
  return usuarioLogado ? <Outlet /> : <Navigate to="/login" replace />;
}

export function RotaFuncionario() {
  const { funcionarioLogado } = useAppData();
  return funcionarioLogado ? <Outlet /> : <Navigate to="/login-funcionario" replace />;
}

export function RotaSemFuncionario() {
  const { funcionarioLogado } = useAppData();
  return funcionarioLogado ? <Navigate to="/funcionario" replace /> : <Outlet />;
}

export function RotaSemUsuario() {
  const { usuarioLogado } = useAppData();
  return usuarioLogado ? <Navigate to="/perfil" replace /> : <Outlet />;
}
