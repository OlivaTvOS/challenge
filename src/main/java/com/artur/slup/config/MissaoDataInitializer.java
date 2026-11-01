package com.artur.slup.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MissaoDataInitializer implements CommandLineRunner {

    private final DataSource dataSource;

    public MissaoDataInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) {
        try (Connection connection = dataSource.getConnection()) {
            boolean autoCommitAnterior = connection.getAutoCommit();
            try {
                connection.setAutoCommit(false);
                garantirCategorias(connection);
                garantirSequencia(connection, "CATEGORIA_ID_CATEGORIA_SEQ", "CATEGORIA", "ID_CATEGORIA");

                if (contarMissoes(connection) == 0) {
                    inserirMissoes(connection);
                }
                connection.commit();
            } catch (SQLException | RuntimeException erro) {
                connection.rollback();
                throw new IllegalStateException("Nao foi possivel inicializar categorias e missoes.", erro);
            } finally {
                connection.setAutoCommit(autoCommitAnterior);
            }
        } catch (SQLException erro) {
            throw new IllegalStateException("Nao foi possivel abrir a conexao para inicializar missoes.", erro);
        }
    }

    private void garantirCategorias(Connection connection) throws SQLException {
        String[][] categorias = {
                {"1", "RECICLAGEM", "Acoes de separacao e descarte correto de residuos reciclaveis."},
                {"2", "MOBILIDADE", "Uso de transporte sustentavel como bicicleta e transporte publico."},
                {"3", "AGUA", "Economia e reuso consciente de agua."},
                {"4", "ENERGIA", "Reducao do consumo de energia eletrica e uso eficiente."},
                {"5", "NATUREZA", "Contato, preservacao e plantio de arvores e areas verdes."},
                {"6", "CONSUMO", "Consumo consciente e reducao de desperdicios."}
        };

        for (int i = 0; i < categorias.length; i++) {
            int idEsperado = Integer.parseInt(categorias[i][0]);
            Integer idExistente = buscarIdCategoria(connection, categorias[i][1]);
            if (idExistente == null) {
                String sql = "INSERT INTO CATEGORIA (ID_CATEGORIA, NOME, DESCRICAO) VALUES (?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, idEsperado);
                    statement.setString(2, categorias[i][1]);
                    statement.setString(3, categorias[i][2]);
                    statement.executeUpdate();
                }
            } else if (idExistente != idEsperado) {
                throw new IllegalStateException("A categoria " + categorias[i][1]
                        + " precisa usar o ID " + idEsperado + " para corresponder ao Front-end.");
            }
        }
    }

    private Integer buscarIdCategoria(Connection connection, String nome) throws SQLException {
        String sql = "SELECT ID_CATEGORIA FROM CATEGORIA WHERE NOME = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nome);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID_CATEGORIA");
                }
            }
        }
        return null;
    }

    private int contarMissoes(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM MISSAO");
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    private void inserirMissoes(Connection connection) throws SQLException {
        String sql = "INSERT INTO MISSAO (ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA) VALUES (?, ?, ?, ?, 'S')";
        String[][] missoes = {
                {"1", "Separe seus reciclaveis", "Separe papel, plastico, vidro e metal em recipientes distintos para facilitar a coleta seletiva.", "30"},
                {"1", "Leve reciclaveis a um ponto de coleta", "Reuna seus reciclaveis e leve a um ponto de coleta proximo da sua casa.", "60"},
                {"1", "Recicle uma embalagem de vidro", "Lave e destine corretamente uma embalagem de vidro para reciclagem.", "40"},
                {"1", "Separe residuos eletronicos", "Separe pilhas, baterias e pequenos eletronicos para descarte em local adequado.", "80"},
                {"1", "Monte um ponto de separacao em casa", "Crie um espaco organizado em casa com lixeiras para cada tipo de reciclavel.", "100"},
                {"2", "Use transporte publico", "Substitua o carro particular por transporte publico em um trajeto do dia.", "50"},
                {"2", "Faca um trajeto de bicicleta", "Pedale em vez de usar carro ou onibus para um trajeto curto do dia.", "70"},
                {"2", "Caminhe em um trajeto curto", "Va a pe em um trajeto que voce faria de carro ou transporte.", "50"},
                {"2", "Compartilhe uma rota sustentavel", "Compartilhe uma rota com amigos ou colegas para reduzir o numero de veiculos.", "35"},
                {"2", "Desafio de mobilidade da semana", "Use apenas transporte sustentavel durante uma semana inteira.", "140"},
                {"3", "Banho consciente", "Reduza o tempo de banho para no maximo 5 minutos e feche a torneira enquanto se ensaboa.", "30"},
                {"3", "Feche a torneira", "Nao deixe a torneira aberta enquanto escova os dentes ou lava a louca.", "20"},
                {"3", "Reutilize agua", "Reaproveite agua da maquina de lavar ou do banho para regar plantas ou limpar o quintal.", "60"},
                {"3", "Verifique vazamentos", "Inspecione torneiras, canos e vasos sanitarios e conserte qualquer vazamento.", "80"},
                {"3", "Desafio de economia de agua", "Reduza o consumo de agua da sua casa em pelo menos 20 por cento durante uma semana.", "130"},
                {"4", "Apague luzes desnecessarias", "Apague as luzes de comodos vazios e aproveite a luz natural sempre que possivel.", "20"},
                {"4", "Desligue um aparelho", "Desligue da tomada um aparelho que nao esta em uso para evitar o consumo fantasma.", "25"},
                {"4", "Use luz natural", "Abra as janelas e use a luz do sol em vez de acender lampadas durante o dia.", "30"},
                {"4", "Reduza o uso do ar-condicionado", "Diminua o uso do ar-condicionado ou ajuste a temperatura para 24 graus ou mais.", "70"},
                {"4", "Desafio de economia de energia", "Mantenha o consumo de energia da casa no minimo durante uma semana.", "130"},
                {"5", "Cuide de uma planta", "Regue, adube e cuide de uma planta em casa ou no trabalho por uma semana.", "30"},
                {"5", "Plante uma muda", "Plante uma muda de arvore ou horta em um local adequado e registre o plantio.", "100"},
                {"5", "Participe de uma acao verde", "Participe de um mutirao de plantio, limpeza ou preservacao ambiental na comunidade.", "120"},
                {"5", "Limpe uma area verde", "Recolha lixo de uma praca, parque ou area verde perto de voce.", "90"},
                {"5", "Desafio de cuidado com a natureza", "Durante uma semana, realize uma acao de cuidado com a natureza todos os dias.", "150"},
                {"6", "Evite um descartavel", "Evite usar copos, talheres ou canudos descartaveis em um dia.", "25"},
                {"6", "Use uma sacola reutilizavel", "Leve sua propria sacola reutilizavel ao fazer compras e recuse sacolas plasticas.", "25"},
                {"6", "Planeje uma compra", "Faca uma lista de compras e compre apenas o necessario para evitar desperdicio.", "40"},
                {"6", "Reaproveite um objeto", "Reaproveite ou conserte um objeto em vez de descarta-lo e comprar novo.", "60"},
                {"6", "Desafio de consumo consciente", "Durante uma semana, evite compras desnecessarias e priorize produtos sustentaveis.", "120"}
        };

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < missoes.length; i++) {
                statement.setInt(1, Integer.parseInt(missoes[i][0]));
                statement.setString(2, missoes[i][1]);
                statement.setString(3, missoes[i][2]);
                statement.setInt(4, Integer.parseInt(missoes[i][3]));
                statement.executeUpdate();
            }
        }
    }

    private void garantirSequencia(Connection connection, String sequencia,
                                   String tabela, String coluna) throws SQLException {
        String sqlMaximo = "SELECT NVL(MAX(" + coluna + "), 0) FROM " + tabela;
        long maiorId;
        try (PreparedStatement statement = connection.prepareStatement(sqlMaximo);
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            maiorId = rs.getLong(1);
        }

        long proximoId;
        String sqlSequencia = "SELECT " + sequencia + ".NEXTVAL FROM DUAL";
        do {
            try (PreparedStatement statement = connection.prepareStatement(sqlSequencia);
                 ResultSet rs = statement.executeQuery()) {
                rs.next();
                proximoId = rs.getLong(1);
            }
        } while (proximoId <= maiorId);
    }
}
