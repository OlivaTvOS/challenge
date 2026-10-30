package com.artur.slup.config;

import com.artur.slup.dao.IMissaoDAO;
import com.artur.slup.dto.Missao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MissaoDataInitializer implements CommandLineRunner {

    private static final List<CategoriaInicial> CATEGORIAS = List.of(
            new CategoriaInicial(1, "RECICLAGEM", "Ações de separação e descarte correto de resíduos recicláveis."),
            new CategoriaInicial(2, "MOBILIDADE", "Uso de transporte sustentável como bicicleta e transporte público."),
            new CategoriaInicial(3, "AGUA", "Economia e reuso consciente de água no dia a dia."),
            new CategoriaInicial(4, "ENERGIA", "Redução do consumo de energia elétrica e uso eficiente."),
            new CategoriaInicial(5, "NATUREZA", "Contato, preservação e plantio de árvores e áreas verdes."),
            new CategoriaInicial(6, "CONSUMO", "Consumo consciente e redução de desperdícios.")
    );

    private static final List<MissaoInicial> MISSOES = List.of(
            new MissaoInicial(1, "Separe seus recicláveis", "Separe papel, plástico, vidro e metal em recipientes distintos para facilitar a coleta seletiva.", 30),
            new MissaoInicial(1, "Leve recicláveis a um ponto de coleta", "Reúna seus recicláveis e leve a um ponto de coleta mais próximo da sua casa.", 60),
            new MissaoInicial(1, "Recicle uma embalagem de vidro", "Lave e destine corretamente uma embalagem de vidro para reciclagem.", 40),
            new MissaoInicial(1, "Separe resíduos eletrônicos", "Separe pilhas, baterias e pequenos eletrônicos para descarte em local adequado.", 80),
            new MissaoInicial(1, "Monte um ponto de separação em casa", "Crie um espaço organizado em casa com lixeiras para cada tipo de reciclável.", 100),
            new MissaoInicial(2, "Use transporte público", "Substitua o carro particular por transporte público em um trajeto do dia.", 50),
            new MissaoInicial(2, "Faça um trajeto de bicicleta", "Pedale em vez de usar carro ou ônibus para um trajeto curto do dia.", 70),
            new MissaoInicial(2, "Caminhe em um trajeto curto", "Vá a pé em um trajeto que você faria de carro ou transporte.", 50),
            new MissaoInicial(2, "Compartilhe uma rota sustentável", "Compartilhe uma rota com amigos ou colegas para reduzir o número de veículos.", 35),
            new MissaoInicial(2, "Desafio de mobilidade da semana", "Use apenas transporte sustentável (bicicleta, a pé ou público) durante uma semana inteira.", 140),
            new MissaoInicial(3, "Banho consciente", "Reduza o tempo de banho para no máximo 5 minutos e feche a torneira enquanto se ensaboa.", 30),
            new MissaoInicial(3, "Feche a torneira", "Não deixe a torneira aberta enquanto escova os dentes ou lava a louça.", 20),
            new MissaoInicial(3, "Reutilize água", "Reaproveite a água da máquina de lavar ou do banho para regar plantas ou limpar o quintal.", 60),
            new MissaoInicial(3, "Verifique vazamentos", "Inspecione torneiras, canos e vasos sanitários em casa e conserte qualquer vazamento.", 80),
            new MissaoInicial(3, "Desafio de economia de água", "Reduza o consumo de água da sua casa em pelo menos 20% durante uma semana inteira.", 130),
            new MissaoInicial(4, "Apague luzes desnecessárias", "Apague as luzes de cômodos vazios e aproveite a luz natural sempre que possível.", 20),
            new MissaoInicial(4, "Desligue um aparelho", "Desligue da tomada um aparelho que não está em uso para evitar o consumo fantasma.", 25),
            new MissaoInicial(4, "Use luz natural", "Abra as janelas e use a luz do sol em vez de acender lâmpadas durante o dia.", 30),
            new MissaoInicial(4, "Reduza o uso do ar-condicionado", "Diminua o uso do ar-condicionado ou ajuste a temperatura para 24°C ou mais.", 70),
            new MissaoInicial(4, "Desafio de economia de energia", "Mantenha o consumo de energia da casa no mínimo durante uma semana inteira com hábitos eficientes.", 130),
            new MissaoInicial(5, "Cuide de uma planta", "Regue, adube e cuide de uma planta em casa ou no trabalho por uma semana.", 30),
            new MissaoInicial(5, "Plante uma muda", "Plante uma muda de árvore ou horta em um local adequado e registre o plantio.", 100),
            new MissaoInicial(5, "Participe de uma ação verde", "Participe de um mutirão de plantio, limpeza ou preservação ambiental na sua comunidade.", 120),
            new MissaoInicial(5, "Limpe uma área verde", "Recolha lixo de uma praça, parque ou área verde perto de você.", 90),
            new MissaoInicial(5, "Desafio de cuidado com a natureza", "Durante uma semana, realize uma ação de cuidado com a natureza todos os dias.", 150),
            new MissaoInicial(6, "Evite um descartável", "Evite usar copos, talheres ou canudos descartáveis em um dia. Leve os seus reutilizáveis.", 25),
            new MissaoInicial(6, "Use uma sacola reutilizável", "Leve sua própria sacola reutilizável ao fazer compras e recuse sacolas plásticas.", 25),
            new MissaoInicial(6, "Planeje uma compra", "Faça uma lista de compras e compre apenas o necessário para evitar desperdício.", 40),
            new MissaoInicial(6, "Reaproveite um objeto", "Reaproveite ou conserte um objeto em vez de descartá-lo e comprar novo.", 60),
            new MissaoInicial(6, "Desafio de consumo consciente", "Durante uma semana, evite compras desnecessárias e priorize produtos sustentáveis.", 120)
    );

    private final IMissaoDAO missaoDAO;
    private final Connection connection;

    public MissaoDataInitializer(IMissaoDAO missaoDAO, Connection connection) {
        this.missaoDAO = missaoDAO;
        this.connection = connection;
    }

    @Override
    public void run(String... args) {
        try {
            boolean autoCommitAnterior = connection.getAutoCommit();
            try {
                connection.setAutoCommit(false);
                garantirCategorias();
                if (missaoDAO.contarMissoes() == 0) {
                    for (MissaoInicial inicial : MISSOES) {
                        Missao missao = new Missao(inicial.idCategoria(), inicial.titulo(), inicial.descricao(), inicial.pontos(), "S");
                        if (!missaoDAO.criarMissao(missao)) {
                            throw new IllegalStateException("Não foi possível recriar a missão: " + inicial.titulo());
                        }
                    }
                }
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw new IllegalStateException("Não foi possível inicializar as categorias e missões.", e);
            } finally {
                connection.setAutoCommit(autoCommitAnterior);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível inicializar as categorias e missões.", e);
        }
    }

    private void garantirCategorias() throws SQLException {
        for (CategoriaInicial categoria : CATEGORIAS) {
            Integer idExistente = buscarIdCategoria(categoria.nome());
            if (idExistente == null) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO CATEGORIA (ID_CATEGORIA, NOME, DESCRICAO) VALUES (?, ?, ?)")) {
                    statement.setInt(1, categoria.id());
                    statement.setString(2, categoria.nome());
                    statement.setString(3, categoria.descricao());
                    statement.executeUpdate();
                }
                idExistente = buscarIdCategoria(categoria.nome());
            }

            if (idExistente == null || idExistente != categoria.id()) {
                throw new IllegalStateException(
                        "A categoria " + categoria.nome() + " precisa ter ID " + categoria.id() +
                                " para corresponder ao front-end.");
            }
        }

        int proximoId;
        do {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT CATEGORIA_ID_CATEGORIA_SEQ.NEXTVAL FROM DUAL");
                 ResultSet rs = statement.executeQuery()) {
                rs.next();
                proximoId = rs.getInt(1);
            }
        } while (proximoId <= CATEGORIAS.size());
    }

    private Integer buscarIdCategoria(String nome) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT ID_CATEGORIA FROM CATEGORIA WHERE NOME = ?")) {
            statement.setString(1, nome);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt("ID_CATEGORIA") : null;
            }
        }
    }

    private record CategoriaInicial(int id, String nome, String descricao) {
    }

    private record MissaoInicial(int idCategoria, String titulo, String descricao, int pontos) {
    }
}
