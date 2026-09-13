# Kairós - SoulUp Front-End

Aplicação acadêmica que usa missões, pontos e ranking para incentivar ações sustentáveis. A Sprint 3 também demonstra autenticação, autorização, Zero Trust e Least Privilege para três cargos de funcionário.

## Tecnologias

- React, Vite e TypeScript
- Tailwind CSS
- React Router DOM
- React Hook Form

## Funcionalidades

- Páginas Home, Sobre, Solução, FAQ, Contato e Integrantes
- Cadastro, login, perfil e exclusão de usuário
- Missões com rota dinâmica, andamento, conclusão e pontos
- Ranking de usuários
- Cadastro acadêmico e login de funcionários por ID, e-mail e senha
- Sessões exclusivas de usuário e funcionário
- Painel com permissões diferentes para ADMIN, TECH e CEO

## Organização

```text
src/
  components/   componentes de layout, interface e missões
  core/         contexto da aplicação e dados iniciais
  data/         categorias e integrantes
  pages/        uma pasta por página
  routes/       rotas e proteções de acesso
  types/        tipos e interfaces TypeScript
```

## Como executar

```text
npm install
npm run dev
```

Para validar a entrega:

```text
npm run lint
npm run build
```

## Contas fictícias de demonstração

```text
ADMIN: ID 10 | aline.admin@kairos.com | Admin123A0
TECH:  ID 20 | tiago.tech@kairos.com | Tech123Aa0
CEO:   ID 30 | celia.diretora@kairos.com | Ceo123Ab90
```

## Limitações acadêmicas

Não há API, banco ou persistência. Os dados ficam apenas no estado React e são reiniciados ao recarregar a página. As senhas aparecem em texto no código porque este Front é uma demonstração local; isso não seria adequado em produção. Controles executados somente no navegador não fornecem segurança real: um backend precisaria repetir a autenticação e todas as verificações de permissão.

## Integrante

- Artur Silva Oliveira - RM 569870 - 1º ADS / TDSPI
- GitHub: https://github.com/OlivaTvOS

## Links da entrega

- Repositório GitHub: preencher antes da entrega
- Vídeo no YouTube: preencher antes da entrega

O perfil comportamental por IA/K-Means faz parte da visão futura do produto e ainda não está implementado nesta Sprint.
