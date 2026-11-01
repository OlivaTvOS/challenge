# Kairós — SoulUp

O SoulUp é uma aplicação acadêmica que usa missões e pontos para incentivar hábitos sustentáveis. Nesta etapa, o Front-end em React consome a API Java para cadastro, login, perfil, missões e ranking geral.

![Logo do projeto](public/logo.png)

## Tecnologias

- React 18, Vite e TypeScript.
- Tailwind CSS para a interface responsiva.
- React Router DOM para navegação entre páginas.
- React Hook Form para formulários e validação.
- `fetch` para as requisições HTTP.

Não foi adicionado Axios nem biblioteca externa de componentes visuais.

## O que está implementado

- Páginas institucionais: Home, Sobre, Solução, FAQ, Contato e Integrantes.
- Cadastro, login, perfil, atualização e exclusão de conta.
- Catálogo de missões com rota de detalhe por ID.
- Início e conclusão de missões, com atualização dos pontos no backend.
- Ranking geral carregado pela API Java.
- Estado compartilhado pelo Context API.
- Recuperação do perfil salvo no navegador quando a página é atualizada.

## Organização do código

```text
src/
  components/   componentes reutilizáveis de layout, interface e missões
  context/      estado compartilhado e sessão do usuário
  data/         categorias e dados de apresentação dos integrantes
  pages/        páginas da aplicação
  routes/       rotas e proteção de páginas
  services/     chamadas HTTP para a API Java
  types/        interfaces e tipos TypeScript
```

## Executar localmente

1. Instale o Node.js.
2. Abra o terminal na pasta que contém `package.json`.
3. Execute `npm install`.
4. Inicie a API Java na porta `8080`.
5. Execute `npm run dev` na pasta do Front-end.
6. Abra o endereço local informado pelo Vite.

Durante o desenvolvimento, o Vite encaminha as chamadas `/api` para `http://localhost:8080`. Para publicar, configure `VITE_API_BASE_URL` com a URL pública da API Java, sem acrescentar `/api` ao final.

Comandos de verificação:

```bash
npm run lint
npm run build
```

## Responsividade

O layout usa classes responsivas do Tailwind para celular (até 480 px), tablet (768 px) e desktop (992 px ou mais). Os principais grids, formulários, cards e o menu se reorganizam conforme a largura da tela. A validação final deve ser feita no navegador nessas três faixas.

## Integrante

- Artur Silva Oliveira — RM 569870 — 1º ADS / Turma TDSPI
- GitHub: https://github.com/OlivaTvOS
- LinkedIn: https://www.linkedin.com/in/artur-silva-oliveira
- Foto: será adicionada manualmente em `public/integrantes` e indicada em `src/data/integrantes.ts`.

## Entrega e publicação

- Repositório: https://github.com/OlivaTvOS/challenge
- Vídeo: ainda precisa ser gravado e publicado no YouTube.
- Aplicação Vercel: ainda não publicada.
- API Java no Render: ainda não publicada.

O arquivo `vercel.json` já prepara o redirecionamento das rotas da SPA. A publicação ainda depende da configuração das plataformas e das variáveis de ambiente.

## Integração com a IA e gráficos

A branch de IA possui os endpoints Flask `/predict/cluster` e `/predict/perfil`, mas o Front-end ainda não os consome. O conjunto de 400 linhas usado no treinamento não tem a origem documentada no repositório. A carga SQL da Sprint 4 é demonstrativa e possui apenas cinco registros de histórico de missões. Esses dados não são suficientes para afirmar que os grupos representam o comportamento real dos usuários.

Por esse motivo, os resultados do K-Means não foram ligados a gráficos ou a um ranking por categoria. Fazer isso agora daria uma aparência de precisão que os dados disponíveis não sustentam. Quando houver histórico real e suficiente, com origem e critérios de coleta registrados, será possível reavaliar essa integração.

## Contato

- GitHub: https://github.com/OlivaTvOS
- LinkedIn: https://www.linkedin.com/in/artur-silva-oliveira

## Uso de ferramentas de IA

Ferramentas de IA foram usadas como apoio para revisar requisitos, localizar inconsistências e organizar correções. O código final precisa ser revisado e compreendido pelo integrante antes da apresentação.
