# Kairós — SoulUp Front-End

Aplicação acadêmica para desafio que usa missões, pontos e ranking para incentivar ações sustentáveis. A Sprint 3 migra a solução para uma SPA em React, Vite e TypeScript e demonstra autenticação e autorização de funcionários com três níveis de acesso.

## Tecnologias utilizadas

- React 18, Vite e TypeScript
- Tailwind CSS
- React Router DOM
- React Hook Form
- ESLint

## Funcionalidades

- Home, Sobre, Solução, FAQ, Contato e Integrantes
- Cadastro, login, perfil e exclusão de usuário
- Catálogo de missões, rota dinâmica por ID, início, conclusão e pontos
- Ranking de usuários
- Cadastro demonstrativo e login de funcionário por ID, e-mail e senha
- Sessões exclusivas de usuário e funcionário
- Permissões diferentes para ADMIN, TECH e CEO

## Estrutura de pastas

```text
src/
  components/   componentes reutilizáveis de layout, interface e missões
  core/         contexto da aplicação e dados iniciais
  data/         categorias e integrante
  pages/        componentes de página
  routes/       rotas estáticas, dinâmicas e proteções
  types/        tipos e interfaces TypeScript
```

## Como executar localmente

1. Instale o Node.js.
2. Abra um terminal na pasta do projeto.
3. Execute `npm install`.
4. Execute `npm run dev`.
5. Abra o endereço informado pelo Vite.

Validação: `npm run lint` e `npm run build`.

## Responsividade

O layout usa Tailwind e foi estruturado para celular até 480 px, tablet a partir de 768 px e desktop a partir de 992 px. Menus, grids, formulários e cards reorganizam seu tamanho e quantidade de colunas conforme a tela.

## Integrante e créditos

- Artur Silva Oliveira — RM 569870 — 1º ADS / TDSPI
- GitHub: https://github.com/OlivaTvOS
- LinkedIn: www.linkedin.com/in/artur-silva-oliveira
- Foto: [inserir arquivo da foto em `public/integrantes` antes da entrega]

## Links da entrega

- Repositório público no GitHub: [inserir URL final]
- Vídeo público no YouTube: [inserir URL final]

## Contato

- GitHub da equipe: https://github.com/OlivaTvOS

## Limitações

Não há consumo de API nesta Sprint. Os dados ficam no estado React e são reiniciados ao recarregar a página. A proteção no navegador serve como demonstração acadêmica; o sistema publicado irá refatorar a persistência de dados na Sprint 4.

## Registro de apoio de IA

Ferramentas de IA foram usadas como apoio na revisão de requisitos, organização de etapas para desenvolvimento.

## PROMPTS USADOS:

### PROMPT 1:

ANALISE MINHA IDEIA APRESENTADA E VEJA SE É COERENTE COM O PRAZO DE 1 MÊS E MEIO PARA ENTREGA CONSIDERANDO A DIFERENÇA ENTRE AS LINGUAGENS JAVA (COM BANCO), PYTHON (SEM BANCO) E FRONT-END QUE NÃO PODE TER CONSUMO DE API POR ENQUANTO.
UMA VEZ QUE ANALISE, ME RETORNE QUANTO TEMPO EM MÉDIA DEVO GASTAR NO DESENVOLVIMENTO DE CADA UMA DELAS 

### PROMPT 2:

ANALISE MINHA PROPOSTA DE ZERO TRUST ARCHITECTURE PARA O PROJETO E VEJA SE A IMPLEMENTAÇÃO DE MANEIRA MENOS ROBUSTA É VIÁVEL

### PROMPT 3:

FAÇA A ANÁLISE DE REQUISITOS DAS MATÉRIAS PARA QUE EU POSSA REALIZAR MINHA CONFERÊNCIA DOS DOCUMENTOS E DOS DADOS. 
ANALISE QUAIS MATÉRIAS PEDEM VÍDEO E SUAS ESTRUTURAS.