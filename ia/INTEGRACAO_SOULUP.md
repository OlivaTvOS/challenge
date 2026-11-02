# Proposta de integração da IA ao SoulUp

## 1. Situação atual

A branch `artificial-intelligence` contém:

- K-Means, que agrupa usuários usando seis quantidades de missões por categoria e a taxa de conclusão.
- Random Forest, que classifica um perfil usando as seis quantidades de missões por categoria.
- Uma API Flask com `GET /`, `POST /predict/cluster` e `POST /predict/perfil`.
- Um CSV com 400 registros. A origem dos registros e o processo de atribuição dos perfis de referência não estão documentados.

As métricas registradas no notebook (silhueta aproximada de 0,404, ARI aproximado de 0,889 e acurácia aproximada de 0,975) descrevem somente essa base. Não comprovam desempenho em dados reais do SoulUp.

## 2. Fonte dos dados reais

Não é necessário criar uma tabela para copiar todas as missões de cada usuário. O modelo relacional já possui:

- `USUARIO_MISSAO`: usuário, missão, status e datas.
- `MISSAO`: missão e sua categoria.
- `CATEGORIA`: identificação e nome da categoria.

Essas tabelas são a fonte dos eventos. Uma consulta agregada pode produzir as seis contagens e a taxa de conclusão que os modelos esperam.

### Definição que precisa ser confirmada

A tabela `USUARIO_MISSAO` permite os status `DISPONIVEL`, `EM_ANDAMENTO`, `CONCLUIDA` e `CANCELADA`.

A proposta inicial é contar como tentativa comportamental os registros em `EM_ANDAMENTO`, `CONCLUIDA` e `CANCELADA`; considerar `CONCLUIDA` como conclusão e `CANCELADA` como abandono; e calcular:

```
taxa_conclusao = missoes_concluidas / total_tentativas
```

Registros `DISPONIVEL` não seriam considerados tentativa, pois ainda não representam uma ação iniciada. Essa regra deve ser validada com o comportamento real do sistema antes de treinar novamente o modelo. O CSV atual não documenta suficientemente a regra usada para gerar `total_missoes`, `missoes_concluidas` e `missoes_abandonadas`.

## 3. Mapeamento de categorias

O modelo espera seis dimensões:

1. Reciclagem
2. Mobilidade
3. Água
4. Energia
5. Natureza
6. Consumo

O arquivo de carga demonstrativa do banco cria cinco categorias com nomes prefixados por "Demo Sprint 4", incluindo Comunidade, e não contém Energia e Natureza. Portanto, não é seguro relacionar as categorias apenas pela posição ou presumir que a carga demonstrativa representa o catálogo definitivo.

Antes de ativar previsões com usuários reais, o catálogo de produção precisa ter um mapeamento explícito e estável para as seis dimensões do modelo. O mapeamento deve usar identificadores de categoria ou códigos estáveis, nunca correspondência parcial de nomes.

## 4. Tabela de resultado da IA

A tabela `PERFIL_USUARIO` atual relaciona um usuário a uma `CATEGORIA` e guarda um nível de confiança. Os rótulos dos modelos (`GUARDIAO_RECICLAGEM`, `MOBILIZADOR_VERDE`, `GUARDIAO_RECURSOS`, `CONSUMO_CONSCIENTE` e `EXPLORADOR_SUSTENTAVEL`) são arquétipos, não IDs das seis categorias ambientais. Gravar esses rótulos diretamente em `PERFIL_USUARIO.ID_CATEGORIA` seria semanticamente incorreto.

O modelo Oracle agora inclui a tabela `IA_CLASSIFICACAO`, com uma linha por execução, contendo:

- identificador da classificação;
- identificador do usuário (FK para `USUARIO`, com exclusão em cascata);
- perfil previsto (rótulo do modelo);
- identificador do cluster K-Means;
- confiança/probabilidade, quando aplicável;
- versão do modelo;
- data da classificação;
- seis contagens de missões e taxa de conclusão usadas naquela previsão.

Guardar as características junto ao resultado cria um retrato auditável do que o modelo recebeu. A tabela seria um histórico de resultados, não uma cópia operacional de `USUARIO_MISSAO`. A tabela foi adicionada ao DDL; a implementação Java de gravação deve aguardar a confirmação do mapeamento das categorias e da regra dos status.

## 5. Fluxo de integração recomendado

1. O front-end solicita o perfil de IA ao Java.
2. O Java identifica o usuário autenticado; não deve confiar em um ID arbitrário enviado pelo cliente sem autorização.
3. Um DAO Java consulta e agrega os registros de `USUARIO_MISSAO`, `MISSAO` e `CATEGORIA`.
4. O Java envia ao serviço Flask somente as características numéricas necessárias.
5. O Flask valida os campos e executa K-Means e/ou Random Forest.
6. O Java recebe a resposta, registra uma linha em `IA_CLASSIFICACAO` e devolve o resultado ao front-end.
7. Se o serviço de IA estiver indisponível, as operações normais do SoulUp continuam funcionando.

O serviço Flask não deve acessar o Oracle diretamente nem receber senha do banco. O Java continua sendo a fronteira de acesso ao banco e a autoridade sobre o usuário.

## 6. Desempenho e frequência

Para o volume atual de um projeto acadêmico, uma consulta agregada por usuário é suficiente. Não é necessário manter uma tabela de contagens atualizada a cada missão nesta primeira versão.

A classificação pode ser solicitada sob demanda e o resultado pode ser armazenado como histórico. Se o sistema crescer, pode-se avaliar cache ou atualização assíncrona. Só vale adicionar esse custo quando houver medição que demonstre necessidade.

## 7. Etapas antes da ativação

1. Confirmar os seis IDs/códigos reais de categoria no Oracle.
2. Confirmar a semântica de `DISPONIVEL`, `EM_ANDAMENTO`, `CONCLUIDA` e `CANCELADA`.
3. Reunir dados reais suficientes, com uso autorizado, e documentar como os rótulos de referência são definidos.
4. Reavaliar o treinamento e a validação com dados reais; evitar medir o modelo somente contra rótulos artificiais derivados das mesmas regras que ele tenta aprender.
5. Criar a tabela de histórico de IA.
6. Implementar o DAO/serviço Java e a chamada HTTP para Flask.
7. Testar com usuários de demonstração e verificar indisponibilidade, ausência de histórico e usuários sem dados suficientes.

## 8. Decisão arquitetural

A recomendação é manter Java como orquestrador e proprietário dos dados, Flask como serviço de inferência e Oracle como fonte de verdade. Não duplicar `USUARIO_MISSAO` em outra tabela de eventos. Criar apenas uma tabela de histórico de classificações quando as regras de dados e categorias estiverem confirmadas.

Até a validação com dados reais, a resposta deve ser identificada como experimental e não usada para decisões relevantes sobre usuários.


## 9. Escopo de postagens

As tabelas `PUBLICACAO` e `INTERACAO` foram removidas do script de criação do modelo, da carga demonstrativa e das consultas da Sprint 4 porque a funcionalidade de postagens não está implementada no produto. As tabelas técnicas `SPRING_SESSION` e `SPRING_SESSION_ATTRIBUTES` foram mantidas por serem estruturas utilizadas pelo Spring Session.

A remoção dos scripts não apaga automaticamente tabelas que já existam em um Oracle previamente criado. A limpeza de um schema existente deve ser feita com backup e validação das dependências antes de executar qualquer `DROP TABLE`.

## 10. Terceiro modelo e agrupamento coletivo

A API Flask agora oferece três modelos: K-Means para agrupamento, Random Forest para classificação e Regressão Linear para estimar pontos. O endpoint `POST /predict/clusters` permite enviar vários usuários na mesma requisição e obter a distribuição por cluster, que atende ao objetivo de analisar grupos em conjunto. O endpoint individual permanece disponível. A Regressão Linear usa a coluna `pontos` do CSV e é experimental; ela não deve substituir a pontuação oficial calculada pelo Java.
