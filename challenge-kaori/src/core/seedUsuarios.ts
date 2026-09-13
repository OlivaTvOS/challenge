import type { Usuario } from '../types';

// Usuários iniciais (seed) — nomes reais, pontuações variadas entre 0 e 300
// Senhas em texto puro (proposital no escopo acadêmico, igual ao back-end atual)
export const seedUsuarios: Usuario[] = [
  {
    id: 'u1',
    nomeUsuario: 'ana_verde',
    nomeCompleto: 'Ana Silva Oliveira',
    email: 'ana.silva@email.com',
    senha: 'senha123',
    pontos: 280,
  },
  {
    id: 'u2',
    nomeUsuario: 'bruno_eco',
    nomeCompleto: 'Bruno Costa Santos',
    email: 'bruno.costa@email.com',
    senha: 'senha123',
    pontos: 150,
  },
  {
    id: 'u3',
    nomeUsuario: 'carla_planta',
    nomeCompleto: 'Carla Mendes Ferreira',
    email: 'carla.mendes@email.com',
    senha: 'senha123',
    pontos: 300,
  },
  {
    id: 'u4',
    nomeUsuario: 'daniel_recicle',
    nomeCompleto: 'Daniel Lima Souza',
    email: 'daniel.lima@email.com',
    senha: 'senha123',
    pontos: 90,
  },
  {
    id: 'u5',
    nomeUsuario: 'eduarda_agua',
    nomeCompleto: 'Eduarda Rocha Alves',
    email: 'eduarda.rocha@email.com',
    senha: 'senha123',
    pontos: 0,
  },
];
