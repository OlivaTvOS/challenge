import { Routes, Route } from 'react-router-dom';
import Layout from '../components/layout/Layout';
import Home from '../pages/Home/Home';
import Sobre from '../pages/Sobre/Sobre';
import FAQ from '../pages/FAQ/FAQ';
import Contato from '../pages/Contato/Contato';
import Integrantes from '../pages/Integrantes/Integrantes';
import Solucao from '../pages/Solucao/Solucao';
import Login from '../pages/Login/Login';
import Cadastro from '../pages/Cadastro/Cadastro';
import Perfil from '../pages/Perfil/Perfil';
import Missoes from '../pages/Missoes/Missoes';
import Ranking from '../pages/Ranking/Ranking';
import FuncionarioLogin from '../pages/FuncionarioLogin/FuncionarioLogin';
import FuncionarioCadastro from '../pages/FuncionarioCadastro/FuncionarioCadastro';
import Funcionario from '../pages/Funcionario/Funcionario';
import { RotaFuncionario, RotaSemFuncionario, RotaSemUsuario, RotaUsuario } from './RotasProtegidas';

// Definição central de todas as rotas do SoulUp
export default function AppRoutes() {
  return (
    <Routes>
      {/* Todas as páginas ficam dentro do Layout (Header + Footer) */}
      <Route element={<Layout />}>
        {/* Páginas institucionais */}
        <Route path="/" element={<Home />} />
        <Route path="/sobre" element={<Sobre />} />
        <Route path="/faq" element={<FAQ />} />
        <Route path="/contato" element={<Contato />} />
        <Route path="/integrantes" element={<Integrantes />} />
        <Route path="/solucao" element={<Solucao />} />

        {/* Páginas da aplicação */}
        <Route element={<RotaSemFuncionario />}>
          <Route path="/login" element={<Login />} />
          <Route path="/cadastro" element={<Cadastro />} />
        </Route>
        <Route element={<RotaUsuario />}>
          <Route path="/perfil" element={<Perfil />} />
        </Route>
        <Route element={<RotaSemUsuario />}>
          <Route path="/login-funcionario" element={<FuncionarioLogin />} />
          <Route path="/cadastro-funcionario" element={<FuncionarioCadastro />} />
        </Route>
        <Route element={<RotaFuncionario />}>
          <Route path="/funcionario" element={<Funcionario />} />
        </Route>
        <Route path="/missoes" element={<Missoes />} />
        <Route path="/missoes/:id" element={<Missoes />} />
        <Route path="/ranking" element={<Ranking />} />

        {/* Rota não encontrada — redireciona para a Home */}
        <Route path="*" element={<Home />} />
      </Route>
    </Routes>
  );
}
