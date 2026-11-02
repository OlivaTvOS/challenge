# Módulo Python — SoulUp

## Requisitos

- Python 3.10 ou superior.
- API Java do SoulUp em execução local ou publicada.
- Oracle Database acessível para os relatórios.
- Driver Python `oracledb`.

Instale as dependências:

```bash
python -m pip install -r requirements.txt
```

## API Java

O endereço da API pode ser definido por `SOULUP_API_URL`.

Local:

```bash
SOULUP_API_URL=http://localhost:8080
```

Publicado:

```bash
SOULUP_API_URL=https://soulup-java-api.onrender.com
```

## Oracle

Configure as variáveis de ambiente com os dados do seu próprio acesso:

- `SOULUP_ORACLE_USER`
- `SOULUP_ORACLE_PASSWORD`
- `SOULUP_ORACLE_DSN`

O DSN deve conter o endereço, a porta e o serviço Oracle, no formato fornecido pelo ambiente. Não coloque usuário, senha ou DSN com credenciais no código ou no Git.

A conexão direta do Python é somente de consulta (`SELECT`). Cadastro, autenticação, alteração de perfil, exclusão e operações de missão continuam centralizados na API Java, evitando duplicar regras de negócio e gravações.

## Execução

Na pasta `python`:

```bash
python programa.py
```

Depois de entrar com um usuário, selecione **8 - Relatórios do banco Oracle**. A opção executa as quatro consultas SQL documentadas em `database/sprint4_consultas.sql` e permite exportar os resultados para JSON.

Se as variáveis Oracle não estiverem configuradas ou o banco estiver indisponível, o programa informa o problema sem encerrar a sessão.

## Exercício didático ALUNOS

O exercício de aula de listagem por aprovação/reprovação consulta uma tabela didática `ALUNOS`, com RM e notas CP1, CP2 e CP3. Essa tabela não pertence ao modelo SoulUp. Ela não foi adicionada ao banco do projeto nem misturada às consultas de negócio.
