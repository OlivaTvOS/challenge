# Kairós — SoulUp Front-End

Aplicação acadêmica para o SoulUp que usa missões, pontos e ranking para incentivar ações sustentáveis. O front-end é uma SPA em React, Vite e TypeScript e consome a API Java para contas, missões e ranking global.

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
- Ranking global por pontos
- Áreas de ranking por foco sustentável preparadas para futura integração com a API de classificação

## Estrutura de pastas

```text
src/
  components/   componentes reutilizáveis de layout, interface e missões
  context/      estado compartilhado da aplicação
  data/         categorias e integrante
  pages/        componentes de página
  routes/       rotas estáticas, dinâmicas e proteções
  services/     chamadas HTTP para as APIs
  types/        tipos e interfaces TypeScript
```

O catálogo de missões é carregado do backend. Ao iniciar, a API cria as seis categorias e as 30 missões padrão somente quando necessário. O ranking global também é carregado da API Java; os rankings por foco aguardam um contrato de ranking na API de classificação.

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

## Integração de classificação

A API Python atual expõe previsões de cluster e aprovação de missão. Ela ainda não fornece dados nem endpoints de ranking por foco; por isso, o front-end mantém essas opções identificadas como futuras e não simula posições ou pontuações.

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