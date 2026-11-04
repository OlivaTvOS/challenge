# Kairós — SoulUp | Front-End Design Engineering

Aplicação acadêmica do projeto **Kairós / SoulUp**, desenvolvida como uma **Single Page Application (SPA)** com React, Vite e TypeScript. A plataforma apresenta a proposta do projeto e oferece fluxos funcionais de conta, perfil, missões e ranking, consumindo a API Java para operações de dados.

**Challenge 2026 | Sprint 3 e Sprint 4 — Front-End Design Engineering**

---

## Acesso ao projeto

| Recurso | Link |
|---|---|
| Aplicação publicada (Vercel) | https://soulup-frontend.vercel.app/ |
| Repositório GitHub | https://github.com/OlivaTvOS/challenge |
| Vídeo de demonstração (YouTube) | https://www.youtube.com/watch?v=0DQCjMSNQn8 |
| API Java (Render) | https://soulup-java-api.onrender.com |

---

## Objetivo e escopo

O Front-end do Kairós é uma Single Page Application (SPA) desenvolvida com React, Vite e TypeScript. A aplicação apresenta a proposta do projeto e oferece fluxos funcionais de conta, perfil, missões e ranking, consumindo a API Java para operações de dados.

A Sprint 3 exigiu a refatoração das páginas anteriores, contemplando React, componentização, navegação interna e formulários. Na Sprint 4, o escopo evoluiu para integração com API Java, rotas estáticas e dinâmicas, publicação e demonstração do funcionamento.

## Tecnologias utilizadas

| Tecnologia | Papel |
|---|---|
| React 18 | Composição da interface por componentes. |
| Vite | Servidor de desenvolvimento e build da aplicação. |
| TypeScript | Tipagem de dados, interfaces e contratos utilizados na interface. |
| Tailwind CSS | Estilização e adaptação responsiva. |
| React Router DOM | Navegação SPA, rotas estáticas/dinâmicas e proteção de páginas. |
| React Hook Form | Gerenciamento e validação de formulários. |
| Fetch API | Comunicação HTTP com a API Java. |
| Context API | Compartilhamento do estado da aplicação e dados de sessão. |

## Funcionalidades e páginas

| Página / área | Conteúdo ou função |
|---|---|
| Home | Apresentação da solução e navegação principal. |
| Sobre | Contexto e informações institucionais do projeto. |
| Solução | Explicação da proposta Kairós/SoulUp. |
| FAQ | Perguntas frequentes. |
| Contato | Informações de contato. |
| Integrantes | Identificação da equipe e links de perfil. |
| Cadastro e Login | Criação de conta e autenticação pela API Java. |
| Perfil | Consulta, atualização e exclusão de conta. |
| Missões | Catálogo, detalhes, início e conclusão por usuário. |
| Ranking | Exibição do ranking geral carregado pela API Java. |

As rotas incluem os caminhos obrigatórios fixos e rotas de aplicação que recebem identificadores, como a rota de detalhe de missão. As páginas funcionais de missões, perfil e ranking ampliam a navegação para além das páginas institucionais exigidas.

## Demonstração visual

As imagens abaixo foram preservadas do documento de entrega Front-End Design Engineering.

### Navegação e páginas

![Página inicial do SoulUp](assets/demonstracao/01-home.png)

### Categorias de missões

![Categorias de missões](assets/demonstracao/02-categorias-missoes.png)

### Cadastro de usuário

![Tela de cadastro](assets/demonstracao/03-cadastro.png)

### Login

![Tela de login](assets/demonstracao/04-login.png)

### Ranking global

![Ranking global](assets/demonstracao/05-ranking-global.png)

### Catálogo de missões

![Catálogo de missões](assets/demonstracao/06-catalogo-missoes.png)

### Ranking por categoria

![Ranking por categoria](assets/demonstracao/07-ranking-categoria.png)

### Perfil e responsividade

![Perfil em visualização responsiva](assets/demonstracao/08-perfil-responsivo.png)

---

## Organização do código

```text
src/
├── components/   # Componentes reutilizáveis de layout, interface e missões
├── context/      # Estado compartilhado, provider e sessão do usuário
├── data/         # Categorias e dados de apresentação de integrantes
├── pages/        # Páginas institucionais e funcionais
├── routes/       # Rotas da SPA e proteção de páginas
├── services/     # Funções de comunicação HTTP com o backend
└── types/        # Tipos e interfaces TypeScript
```

A arquitetura reduz duplicação de interface e separa apresentação, estado, navegação e comunicação com os serviços. O serviço HTTP concentra chamadas à API Java; o Context API compartilha dados necessários entre páginas e componentes.

## Integração com a API Java

O Front-end consome a API Java para:

- Cadastro e login;
- Consulta, atualização e exclusão de perfil;
- Catálogo de missões;
- Início e conclusão de missões;
- Consulta do ranking global.

### Desenvolvimento

O Vite encaminha chamadas feitas pelo prefixo `/api` para `http://localhost:8080`, removendo o prefixo antes de encaminhar a requisição.

### Produção

A aplicação publicada utiliza a API Java hospedada no Render. O arquivo `vercel.json` contém as regras de encaminhamento das chamadas `/api` para a API Java e o fallback necessário para suportar rotas da SPA em acesso direto. O backend precisa permitir a origem do domínio Vercel em `APP_CORS_ALLOWED_ORIGINS`.

**API Java:** https://soulup-java-api.onrender.com

## Rotas e navegação

A aplicação usa React Router DOM para rotas fixas e rotas com parâmetros variáveis. A passagem de identificador permite reutilizar o mesmo componente de detalhe para missões distintas.

Rotas protegidas verificam a disponibilidade dos dados de sessão no estado do aplicativo antes de renderizar conteúdos que dependem de conta autenticada. O estado do usuário pode ser recuperado do armazenamento do navegador para manter a continuidade visual após atualização da página.

## Responsividade e experiência

O projeto utiliza classes responsivas do Tailwind para reorganizar menus, cards, grids e formulários.

Faixas de referência indicadas no enunciado:

- **Dispositivos móveis:** até 480 px;
- **Tablets:** em torno de 768 px;
- **Desktops:** a partir de 992 px.

## Como executar localmente

### Pré-requisitos

- Node.js em versão compatível com as dependências do projeto;
- npm;
- API Java disponível localmente na porta `8080`, quando utilizar o proxy de desenvolvimento.

### Instalação e execução

Na pasta que contém o `package.json`, execute:

```bash
npm install
npm run dev
```

Abra no navegador a URL informada pelo Vite.

Para executar a API Java localmente, é necessário baixar o projeto Java no GitHub e executá-lo. Nesse cenário, o proxy de desenvolvimento encaminha as chamadas `/api` para `http://localhost:8080`.

### Verificações e build

```bash
npm run lint
npm run build
```

Para visualizar localmente a versão de produção gerada:

```bash
npm run preview
```

### Publicação

Na Vercel, a aplicação é publicada com o encaminhamento das chamadas à API Java e o fallback de rotas configurados em `vercel.json`.

- Front-end: https://soulup-frontend.vercel.app/
- API Java: https://soulup-java-api.onrender.com

## Estado de validação

Os fluxos principais do Front-end foram confirmados em funcionamento integrado com a API Java publicada.

O ranking global por pontuação é consumido da API Java.

## Uso de ferramentas de Inteligência Artificial

As ferramentas de IA foram utilizadas como apoio à análise das funções, das requisições realizadas pelo Front-end e da integração com a API Java (Spring Boot), além de apoiar a identificação de código que deixou de ser necessário após a integração com a API.

### Prompts utilizados

**PROMPT 1:**

> Analise as funções e requisições para identificiar erros e possíveis chamadas erradas da API trazida de Java (Spring Boot), me atualize com os possíveis erros e possíveis melhorias no front-end.

**PROMPT 2:**

> Preciso refatorar esse código front-end limpando todas as funções/classes que não são mais necessárias e que antes da API, guardavam dados na memória apenas.

## Integrante e contato

### Artur da Silva de Oliveira

![Foto de Artur da Silva de Oliveira](assets/integrante/artur-silva-oliveira.png)

| Informação | Dado |
|---|---|
| RM | 569870 |
| Curso / Turma | 1º ADS / TDSPI |
| Equipe / Solução | Kairós / SoulUp |
| GitHub | https://github.com/OlivaTvOS |
| LinkedIn | https://www.linkedin.com/in/artur-silva-oliveira/ |

## Autoria e créditos

Projeto acadêmico desenvolvido para o **FIAP Challenge 2026**, nas entregas de Sprint 3 e Sprint 4, para a disciplina **Front-End Design Engineering**.

- Integrante: Artur da Silva de Oliveira.
- Solução: Kairós / SoulUp.
- Repositório: https://github.com/OlivaTvOS/challenge.
- Aplicação: https://soulup-frontend.vercel.app/.
- Vídeo: https://www.youtube.com/watch?v=0DQCjMSNQn8.

---

**Kairós / SoulUp — Challenge 2026 | Front-End Design Engineering**
