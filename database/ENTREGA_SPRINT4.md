# Banco de dados - entrega da Sprint 4

## Arquivos

- `modelo.sql`: cria o modelo Oracle e suas restricoes.
- `sprint4_carga_inicial.sql`: insere os dados demonstrativos do SoulUp.
- `sprint4_consultas.sql`: contem cinco consultas ligadas ao funcionamento do sistema.

## Ordem de execucao

1. Execute `modelo.sql` no schema Oracle da equipe.
2. Execute `sprint4_carga_inicial.sql`.
3. Execute cada consulta de `sprint4_consultas.sql` e confira os resultados.

Os scripts da Sprint 4 foram escritos com os nomes reais das tabelas e colunas definidos em `modelo.sql`. A carga preenche as nove tabelas de negocio do modelo: CATEGORIA, CREDENCIAIS, INTERACAO, MISSAO, PERFIL_USUARIO, PUBLICACAO, RANKING, USUARIO e USUARIO_MISSAO. As tabelas SPRING_SESSION e SPRING_SESSION_ATTRIBUTES sao estruturas tecnicas do Spring Session e nao tabelas principais do dominio.

A carga contem registros demonstrativos, criados para testar as relacoes e as consultas. Os usuarios nao sao pessoas reais. As contas de usuario usam a senha de demonstracao `demo123`; troque-a antes de qualquer uso fora do ambiente academico.

## Finalidade das consultas

1. **Ranking geral:** consulta posicao e pontuacao de cada usuario.
2. **Historico de missoes:** relaciona usuario, missao, categoria, status e pontos recebidos.
3. **Publicacoes e interacoes:** mostra conteudos publicados e as curtidas/comentarios associados.
4. **Estatisticas por categoria:** usa JOIN, GROUP BY, COUNT, SUM e AVG para resumir o catalogo.
5. **Perfis registrados:** consulta a categoria e o nivel de confianca guardados para cada usuario.

## Evidencias que ainda precisam ser produzidas

Depois de executar os scripts no Oracle, salve capturas de tela da execucao dos INSERTs e do resultado de cada consulta. Inclua essas imagens e a identificacao completa da equipe no PDF final. O codigo SQL, sozinho, nao comprova que a execucao ocorreu no banco.
