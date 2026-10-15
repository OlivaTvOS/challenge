/* ============================================================
   PROJETO - SPRINT 3
   DDL - Oracle Database
   ============================================================ */


/* ============================================================
   1. USUARIO
   ============================================================ */

CREATE TABLE USUARIO (
    ID_USUARIO        NUMBER(10)        NOT NULL,
    NOME              VARCHAR2(100)     NOT NULL,
    USERNAME          VARCHAR2(50)      NOT NULL,
    EMAIL             VARCHAR2(150)     NOT NULL,
    SENHA             VARCHAR2(255)     NOT NULL,
    DATA_CADASTRO     TIMESTAMP         DEFAULT CURRENT_TIMESTAMP NOT NULL,
    STATUS             VARCHAR2(20)      DEFAULT 'ATIVO' NOT NULL,

    CONSTRAINT PK_USUARIO
        PRIMARY KEY (ID_USUARIO),

    CONSTRAINT UK_USUARIO_USERNAME
        UNIQUE (USERNAME),

    CONSTRAINT UK_USUARIO_EMAIL
        UNIQUE (EMAIL),

    CONSTRAINT CK_USUARIO_STATUS
        CHECK (STATUS IN ('ATIVO', 'INATIVO', 'BLOQUEADO'))
);


/* ============================================================
   2. PERFIL
   ============================================================ */

CREATE TABLE PERFIL (
    ID_PERFIL         NUMBER(10)        NOT NULL,
    ID_USUARIO        NUMBER(10)        NOT NULL,
    BIO               VARCHAR2(500),
    LOCALIZACAO       VARCHAR2(150),
    FOTO_PERFIL       VARCHAR2(500),
    PONTOS_ATUAIS     NUMBER(10)        DEFAULT 0 NOT NULL,
    NIVEL             NUMBER(5)         DEFAULT 1 NOT NULL,

    CONSTRAINT PK_PERFIL
        PRIMARY KEY (ID_PERFIL),

    CONSTRAINT UK_PERFIL_USUARIO
        UNIQUE (ID_USUARIO),

    CONSTRAINT CK_PERFIL_PONTOS
        CHECK (PONTOS_ATUAIS >= 0),

    CONSTRAINT CK_PERFIL_NIVEL
        CHECK (NIVEL >= 1),

    CONSTRAINT FK_PERFIL_USUARIO
        FOREIGN KEY (ID_USUARIO)
        REFERENCES USUARIO (ID_USUARIO)
);


/* ============================================================
   3. CATEGORIA_MISSAO
   ============================================================ */

CREATE TABLE CATEGORIA_MISSAO (
    ID_CATEGORIA      NUMBER(10)        NOT NULL,
    NOME              VARCHAR2(100)     NOT NULL,
    DESCRICAO         VARCHAR2(500),

    CONSTRAINT PK_CATEGORIA_MISSAO
        PRIMARY KEY (ID_CATEGORIA),

    CONSTRAINT UK_CATEGORIA_MISSAO_NOME
        UNIQUE (NOME)
);


/* ============================================================
   4. MISSAO
   ============================================================ */

CREATE TABLE MISSAO (
    ID_MISSAO         NUMBER(10)        NOT NULL,
    ID_CATEGORIA      NUMBER(10)        NOT NULL,
    TITULO            VARCHAR2(150)     NOT NULL,
    DESCRICAO         VARCHAR2(1000)    NOT NULL,
    DIFICULDADE       VARCHAR2(20)      NOT NULL,
    PONTOS            NUMBER(10)        NOT NULL,
    STATUS            VARCHAR2(20)      DEFAULT 'ATIVA' NOT NULL,
    DATA_CRIACAO      TIMESTAMP         DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT PK_MISSAO
        PRIMARY KEY (ID_MISSAO),

    CONSTRAINT CK_MISSAO_DIFICULDADE
        CHECK (DIFICULDADE IN ('FACIL', 'MEDIA', 'DIFICIL')),

    CONSTRAINT CK_MISSAO_PONTOS
        CHECK (PONTOS > 0),

    CONSTRAINT CK_MISSAO_STATUS
        CHECK (STATUS IN ('ATIVA', 'INATIVA')),

    CONSTRAINT FK_MISSAO_CATEGORIA
        FOREIGN KEY (ID_CATEGORIA)
        REFERENCES CATEGORIA_MISSAO (ID_CATEGORIA)
);


/* ============================================================
   5. REALIZACAO_MISSAO
   ============================================================ */

CREATE TABLE REALIZACAO_MISSAO (
    ID_REALIZACAO     NUMBER(10)        NOT NULL,
    ID_USUARIO        NUMBER(10)        NOT NULL,
    ID_MISSAO         NUMBER(10)        NOT NULL,
    DATA_INICIO       TIMESTAMP         DEFAULT CURRENT_TIMESTAMP NOT NULL,
    DATA_CONCLUSAO    TIMESTAMP,
    STATUS             VARCHAR2(20)      DEFAULT 'INICIADA' NOT NULL,
    PONTOS_OBTIDOS    NUMBER(10)        DEFAULT 0 NOT NULL,
    OBSERVACAO        VARCHAR2(500),

    CONSTRAINT PK_REALIZACAO_MISSAO
        PRIMARY KEY (ID_REALIZACAO),

    CONSTRAINT CK_REALIZACAO_STATUS
        CHECK (
            STATUS IN (
                'INICIADA',
                'CONCLUIDA',
                'CANCELADA'
            )
        ),

    CONSTRAINT CK_REALIZACAO_PONTOS
        CHECK (PONTOS_OBTIDOS >= 0),

    CONSTRAINT FK_REALIZACAO_USUARIO
        FOREIGN KEY (ID_USUARIO)
        REFERENCES USUARIO (ID_USUARIO),

    CONSTRAINT FK_REALIZACAO_MISSAO
        FOREIGN KEY (ID_MISSAO)
        REFERENCES MISSAO (ID_MISSAO)
);


/* ============================================================
   6. POSTAGEM
   ============================================================ */

CREATE TABLE POSTAGEM (
    ID_POSTAGEM       NUMBER(10)        NOT NULL,
    ID_USUARIO        NUMBER(10)        NOT NULL,
    TEXTO             VARCHAR2(2000)    NOT NULL,
    URL_MIDIA         VARCHAR2(500),
    DATA_PUBLICACAO   TIMESTAMP         DEFAULT CURRENT_TIMESTAMP NOT NULL,
    STATUS            VARCHAR2(20)      DEFAULT 'ATIVA' NOT NULL,

    CONSTRAINT PK_POSTAGEM
        PRIMARY KEY (ID_POSTAGEM),

    CONSTRAINT CK_POSTAGEM_STATUS
        CHECK (
            STATUS IN (
                'ATIVA',
                'OCULTA',
                'REMOVIDA'
            )
        ),

    CONSTRAINT FK_POSTAGEM_USUARIO
        FOREIGN KEY (ID_USUARIO)
        REFERENCES USUARIO (ID_USUARIO)
);


/* ============================================================
   7. VERIFICACAO_POSTAGEM
   ============================================================ */

CREATE TABLE VERIFICACAO_POSTAGEM (
    ID_VERIFICACAO    NUMBER(10)        NOT NULL,
    ID_POSTAGEM       NUMBER(10)        NOT NULL,
    STATUS             VARCHAR2(20)      DEFAULT 'PENDENTE' NOT NULL,
    METODO             VARCHAR2(50),
    DATA_VERIFICACAO  TIMESTAMP,
    OBSERVACAO        VARCHAR2(1000),

    CONSTRAINT PK_VERIFICACAO_POSTAGEM
        PRIMARY KEY (ID_VERIFICACAO),

    CONSTRAINT CK_VERIFICACAO_STATUS
        CHECK (
            STATUS IN (
                'PENDENTE',
                'APROVADA',
                'REJEITADA'
            )
        ),

    CONSTRAINT FK_VERIFICACAO_POSTAGEM
        FOREIGN KEY (ID_POSTAGEM)
        REFERENCES POSTAGEM (ID_POSTAGEM)
);


/* ============================================================
   8. INTERACAO_POSTAGEM
   ============================================================ */

CREATE TABLE INTERACAO_POSTAGEM (
    ID_INTERACAO      NUMBER(10)        NOT NULL,
    ID_USUARIO        NUMBER(10)        NOT NULL,
    ID_POSTAGEM       NUMBER(10)        NOT NULL,
    TIPO_INTERACAO    VARCHAR2(30)      NOT NULL,
    CONTEUDO          VARCHAR2(1000),
    DATA_INTERACAO    TIMESTAMP         DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT PK_INTERACAO_POSTAGEM
        PRIMARY KEY (ID_INTERACAO),

    CONSTRAINT CK_INTERACAO_TIPO
        CHECK (
            TIPO_INTERACAO IN (
                'CURTIDA',
                'COMENTARIO',
                'COMPARTILHAMENTO'
            )
        ),

    CONSTRAINT FK_INTERACAO_USUARIO
        FOREIGN KEY (ID_USUARIO)
        REFERENCES USUARIO (ID_USUARIO),

    CONSTRAINT FK_INTERACAO_POSTAGEM
        FOREIGN KEY (ID_POSTAGEM)
        REFERENCES POSTAGEM (ID_POSTAGEM)
);


/* ============================================================
   9. TIPO_RECOMPENSA
   ============================================================ */

CREATE TABLE TIPO_RECOMPENSA (
    ID_TIPO_RECOMPENSA NUMBER(10)       NOT NULL,
    NOME               VARCHAR2(100)    NOT NULL,
    DESCRICAO          VARCHAR2(500),
    PONTOS_NECESSARIOS NUMBER(10)       NOT NULL,
    STATUS             VARCHAR2(20)     DEFAULT 'ATIVA' NOT NULL,

    CONSTRAINT PK_TIPO_RECOMPENSA
        PRIMARY KEY (ID_TIPO_RECOMPENSA),

    CONSTRAINT UK_TIPO_RECOMPENSA_NOME
        UNIQUE (NOME),

    CONSTRAINT CK_RECOMPENSA_PONTOS
        CHECK (PONTOS_NECESSARIOS > 0),

    CONSTRAINT CK_RECOMPENSA_STATUS
        CHECK (
            STATUS IN (
                'ATIVA',
                'INATIVA'
            )
        )
);


/* ============================================================
   10. RESGATE_RECOMPENSA
   ============================================================ */

CREATE TABLE RESGATE_RECOMPENSA (
    ID_RESGATE         NUMBER(10)       NOT NULL,
    ID_USUARIO         NUMBER(10)       NOT NULL,
    ID_TIPO_RECOMPENSA NUMBER(10)       NOT NULL,
    DATA_RESGATE       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PONTOS_UTILIZADOS  NUMBER(10)       NOT NULL,
    STATUS             VARCHAR2(20)     DEFAULT 'SOLICITADO' NOT NULL,

    CONSTRAINT PK_RESGATE_RECOMPENSA
        PRIMARY KEY (ID_RESGATE),

    CONSTRAINT CK_RESGATE_PONTOS
        CHECK (PONTOS_UTILIZADOS > 0),

    CONSTRAINT CK_RESGATE_STATUS
        CHECK (
            STATUS IN (
                'SOLICITADO',
                'APROVADO',
                'RECUSADO',
                'CONCLUIDO'
            )
        ),

    CONSTRAINT FK_RESGATE_USUARIO
        FOREIGN KEY (ID_USUARIO)
        REFERENCES USUARIO (ID_USUARIO),

    CONSTRAINT FK_RESGATE_TIPO
        FOREIGN KEY (ID_TIPO_RECOMPENSA)
        REFERENCES TIPO_RECOMPENSA (ID_TIPO_RECOMPENSA)
);


/* ============================================================
   11. USUARIO_SEGUIDOR
   Relação N:N entre usuários
   ============================================================ */

CREATE TABLE USUARIO_SEGUIDOR (
    ID_USUARIO_SEGUE  NUMBER(10)        NOT NULL,
    ID_USUARIO_SEGUIDO NUMBER(10)       NOT NULL,
    DATA_SEGUIMENTO   TIMESTAMP         DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT PK_USUARIO_SEGUIDOR
        PRIMARY KEY (
            ID_USUARIO_SEGUE,
            ID_USUARIO_SEGUIDO
        ),

    CONSTRAINT CK_USUARIO_SEGUIDOR_DIFERENTE
        CHECK (
            ID_USUARIO_SEGUE <> ID_USUARIO_SEGUIDO
        ),

    CONSTRAINT FK_SEGUIDOR_USUARIO
        FOREIGN KEY (ID_USUARIO_SEGUE)
        REFERENCES USUARIO (ID_USUARIO),

    CONSTRAINT FK_SEGUIDO_USUARIO
        FOREIGN KEY (ID_USUARIO_SEGUIDO)
        REFERENCES USUARIO (ID_USUARIO)
);