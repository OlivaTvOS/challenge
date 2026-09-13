import type { Funcionario } from '../types';

// Contas fictícias para demonstrar os três níveis de acesso da Sprint 3.
export const seedFuncionarios: Funcionario[] = [
  { id: 10, nome: 'Aline', sobrenome: 'Admin', email: 'aline.admin@kairos.com', senha: 'Admin123A0', cargo: 'ADMIN' },
  { id: 20, nome: 'Tiago', sobrenome: 'Tech', email: 'tiago.tech@kairos.com', senha: 'Tech123Aa0', cargo: 'TECH' },
  { id: 30, nome: 'Celia', sobrenome: 'Diretora', email: 'celia.diretora@kairos.com', senha: 'Ceo123Ab90', cargo: 'CEO' },
];
