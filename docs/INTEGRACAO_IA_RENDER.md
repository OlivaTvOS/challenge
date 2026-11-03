# Integração Java–Flask no Render (plano gratuito)

## Arquitetura

- **Java / Spring Boot:** consulta o Oracle, agrega as características dos usuários, chama os modelos e grava o histórico.
- **Flask:** carrega os artefatos de IA e executa inferências; não acessa o Oracle.
- **Oracle:** permanece como fonte de verdade dos dados do produto e armazena os resultados em `IA_CLASSIFICACAO`.

## Inicialização sob demanda no Render Free

O serviço Flask pode ser suspenso após um período sem requisições recebidas. Quando chega uma nova requisição, o Render reativa a instância. Durante essa inicialização, a primeira chamada pode demorar aproximadamente um minuto.

Esse comportamento é esperado no plano gratuito e não significa, por si só, que a integração esteja quebrada. Para a entrega acadêmica, o custo do plano pago não é necessário: basta registrar essa limitação e considerar o tempo adicional na demonstração.

O endpoint `GET /` é o health check configurado no Render. Abrir a página do dashboard do Render não é requisito para reativar o serviço; uma requisição HTTP à URL pública é que pode provocar a inicialização.

## Timeouts no cliente Java

O cliente HTTP do serviço Java usa:

- timeout de conexão: **10 segundos**;
- timeout de leitura/resposta: **75 segundos**.

O timeout de leitura comporta o cold start esperado do serviço gratuito, sem deixar a aplicação aguardando indefinidamente. Se o serviço demorar mais que esse limite, a chamada falha e o controller retorna erro de gateway (HTTP 502) nas rotas de integração.

As chamadas POST de inferência **não são repetidas automaticamente**. Isso é intencional: os endpoints Java persistem os resultados em `IA_CLASSIFICACAO`, e repetir uma operação após uma resposta ambígua pode gerar registros duplicados. Uma política de repetição para POST só deve ser adicionada junto com uma chave de idempotência ou outra proteção contra duplicidade.

## Diagnóstico

1. Consultar `GET /ia/status` na API Java.
2. Conferir `api_ia_configurada`: indica se uma URL explícita foi fornecida por `soulup.ia.url` ou `IA_API_URL`; não é uma confirmação de disponibilidade.
3. Conferir `api_ia_disponivel` e `modelos_carregados`: indicam o resultado da consulta ao health check Flask.
4. Conferir `tabela_historico_disponivel`: indica se a tabela `IA_CLASSIFICACAO` existe no schema Oracle conectado.
5. Se a API de IA estiver indisponível logo após um período ocioso, aguardar a inicialização e consultar o status novamente.

## Validação funcional e segurança dos dados

O endpoint `GET /ia/status` é diagnóstico e não executa inferência nem grava resultados.

Os endpoints `POST /ia/clusters` e `POST /ia/modelos` executam inferência usando os dados agregados do Oracle e gravam resultados em `IA_CLASSIFICACAO`. Não devem ser usados como simples health checks. A validação funcional deve ser realizada em horário combinado, com ciência de que novas linhas serão inseridas na tabela de histórico.

## Limitações conhecidas

- O primeiro acesso após suspensão pode ser mais lento que os acessos seguintes.
- O plano gratuito não garante disponibilidade contínua da instância.
- As métricas dos modelos refletem a base de treinamento/avaliação disponível e não comprovam, isoladamente, desempenho em dados reais de produção.
- A regressão linear produz uma estimativa experimental; não altera a pontuação oficial calculada pelo Java.
