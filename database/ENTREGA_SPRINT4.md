# Banco de dados - entrega da Sprint 4

## Arquivos da entrega

- `modelo.sql`: cria as tabelas, sequencias, gatilhos e restricoes do modelo Oracle utilizado pelo projeto.
- `sprint4_carga_inicial.sql`: insere dados ficticios de demonstracao nas nove tabelas principais do dominio.
- `sprint4_consultas.sql`: apresenta cinco consultas SQL relacionadas ao funcionamento do SoulUp/Kairos.

## Ordem de execucao

1. Execute `modelo.sql` no schema Oracle da equipe, caso o modelo ainda nao tenha sido criado.
2. Execute `sprint4_carga_inicial.sql` uma unica vez para inserir os registros demonstrativos.
3. Execute individualmente as cinco consultas de `sprint4_consultas.sql` e confira os resultados.

A carga utiliza os nomes de tabelas e colunas definidos em `modelo.sql`, respeitando as chaves estrangeiras. Os identificadores das tabelas que possuem sequencia e gatilho sao gerados pelo proprio modelo. A tabela `CREDENCIAIS`, que possui identificador limitado a 200, recebe cinco identificadores livres a partir do maior valor ja cadastrado.

A carga inclui cinco registros em cada uma das nove tabelas de negocio: `CATEGORIA`, `CREDENCIAIS`, `INTERACAO`, `MISSAO`, `PERFIL_USUARIO`, `PUBLICACAO`, `RANKING`, `USUARIO` e `USUARIO_MISSAO`. As tabelas `SPRING_SESSION` e `SPRING_SESSION_ATTRIBUTES` sao estruturas tecnicas do Spring Session e, por isso, nao recebem dados artificiais nesta carga.

## Sobre os dados demonstrativos

Os registros foram criados para demonstrar os relacionamentos e permitir a execucao das consultas. Os nomes e as atividades sao ficticios; nao representam usuarios reais nem metricas reais de uso da SoulUp. Os e-mails utilizam o dominio reservado `.invalid`.

A senha `demo123` e usada somente como valor demonstrativo nos registros de usuario e credenciais. Nao reutilize essas contas em ambientes reais.

**Atencao:** a carga nao e um script de atualizacao nem de limpeza. Execute-a uma unica vez no schema preparado. Uma nova execucao pode gerar duplicidades ou esgotar os identificadores disponiveis em `CREDENCIAIS`. Para repetir o teste, restaure um schema limpo ou remova previamente os registros demonstrativos respeitando a ordem das chaves estrangeiras.

## O que cada consulta demonstra

1. **Ranking geral:** relaciona ranking e usuario para exibir posicao, pontuacao e nivel.
2. **Historico de missoes:** utiliza relacionamentos entre usuario, missao e categoria para mostrar status, datas e pontos recebidos.
3. **Publicacoes e interacoes:** relaciona publicacoes, autores e interacoes, preservando publicacoes sem interacoes por meio de `LEFT JOIN`.
4. **Estatisticas por categoria:** agrupa as missoes por categoria e utiliza `COUNT`, `SUM`, `AVG`, `MIN` e `MAX` para resumir quantidade e recompensas.
5. **Perfil comportamental:** relaciona usuario, categoria e perfil para exibir a classificacao registrada e seu nivel de confianca.

As consultas utilizam `JOIN`, filtros com `WHERE`, ordenacao e agrupamento conforme a necessidade de cada resultado.

## Evidencias que ainda precisam ser produzidas

Depois de executar os scripts no Oracle, salve capturas de tela que mostrem:

- a execucao bem-sucedida da carga inicial;
- o resultado de cada uma das cinco consultas;
- a identificacao do schema utilizado, sem expor senha ou outros dados de acesso.

Inclua as capturas no PDF final da entrega. Os arquivos SQL documentam o que deve ser executado, mas nao comprovam, sozinhos, que a execucao ocorreu no banco.
