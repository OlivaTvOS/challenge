package missao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioMissaoDAO implements IUsuarioMissaoDAO {

    private final Connection connection;

    public UsuarioMissaoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean iniciarMissao(int idUsuario, int idMissao) {

        String sqlVerificar = "SELECT ID_USUARIO_MISSAO FROM USUARIO_MISSAO WHERE ID_USUARIO = ? AND ID_MISSAO = ? AND STATUS = 'EM_ANDAMENTO'";

        String sqlInserir = "INSERT INTO USUARIO_MISSAO (ID_USUARIO, ID_MISSAO, STATUS) VALUES (?, ?, 'EM_ANDAMENTO')";

        try (PreparedStatement verificar = connection.prepareStatement(sqlVerificar)) {

            verificar.setInt(1, idUsuario);
            verificar.setInt(2, idMissao);

            try (ResultSet rs = verificar.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Você possui esta missão em andamento.");
                    return true;
                }
            }

            try (PreparedStatement inserir = connection.prepareStatement(sqlInserir)) {

                inserir.setInt(1, idUsuario);
                inserir.setInt(2, idMissao);

                int linhasAfetadas = inserir.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Missão iniciada com sucesso!");
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao iniciar missão: " + e.getMessage());
        }

        return false;
    }

    @Override
    public Missao buscarMissaoEmAndamento(int idUsuario, int idMissao) {

        String sql = """
                SELECT M.ID_MISSAO,
                       M.ID_CATEGORIA,
                       M.TITULO,
                       M.DESCRICAO,
                       M.PONTOS_RECOMPENSA,
                       M.ATIVA
                FROM USUARIO_MISSAO UM
                INNER JOIN MISSAO M ON M.ID_MISSAO = UM.ID_MISSAO
                WHERE UM.ID_USUARIO = ?
                AND UM.ID_MISSAO = ?
                AND UM.STATUS = 'EM_ANDAMENTO'
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, idMissao);

            try (ResultSet rs = preparedStatement.executeQuery()) {

                if (rs.next()) {

                    Missao missao = new Missao();

                    missao.setIdMissao(rs.getInt("ID_MISSAO"));
                    missao.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                    missao.setTitulo(rs.getString("TITULO"));
                    missao.setDescricao(rs.getString("DESCRICAO"));
                    missao.setPontosRecompensa(rs.getInt("PONTOS_RECOMPENSA"));
                    missao.setAtiva(rs.getString("ATIVA"));

                    return missao;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar missão em andamento: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean concluirMissao(int idUsuario, int idMissao) {

        String sqlMissao = "SELECT PONTOS_RECOMPENSA FROM MISSAO WHERE ID_MISSAO = ?";

        String sqlConclusao = "UPDATE USUARIO_MISSAO SET STATUS = 'CONCLUIDA', DATA_CONCLUSAO = SYSDATE, PONTOS_RECEBIDOS = ? WHERE ID_USUARIO = ? AND ID_MISSAO = ? AND STATUS = 'EM_ANDAMENTO'";

        String sqlPontos = "UPDATE USUARIO SET PONTOS = PONTOS + ? WHERE ID_USUARIO = ?";

        try {
            connection.setAutoCommit(false);

            int pontos = 0;

            try (PreparedStatement buscarMissao = connection.prepareStatement(sqlMissao)) {

                buscarMissao.setInt(1, idMissao);

                try (ResultSet rs = buscarMissao.executeQuery()) {
                    if (!rs.next()) {
                        connection.rollback();
                        System.out.println("Missão não encontrada.");
                        return false;
                    }

                    pontos = rs.getInt("PONTOS_RECOMPENSA");
                }
            }

            try (PreparedStatement concluir = connection.prepareStatement(sqlConclusao)) {

                concluir.setInt(1, pontos);
                concluir.setInt(2, idUsuario);
                concluir.setInt(3, idMissao);

                int linhasAfetadas = concluir.executeUpdate();

                if (linhasAfetadas == 0) {
                    connection.rollback();
                    System.out.println("Esta missão não está em andamento.");
                    return false;
                }
            }

            try (PreparedStatement atualizarPontos = connection.prepareStatement(sqlPontos)) {

                atualizarPontos.setInt(1, pontos);
                atualizarPontos.setInt(2, idUsuario);

                atualizarPontos.executeUpdate();
            }

            connection.commit();

            System.out.println("Missão concluída com sucesso!");
            System.out.println("Pontos recebidos: " + pontos);

            return true;

        } catch (SQLException e) {

            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                System.out.println("Erro ao desfazer operação: " + rollbackException.getMessage());
            }

            System.out.println("Erro ao concluir missão: " + e.getMessage());
            return false;

        } finally {

            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Erro ao restaurar conexão: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Missao> listarMissoesEmAndamento(int idUsuario) {

        List<Missao> missoes = new ArrayList<>();

        String sql = "SELECT M.ID_MISSAO, M.ID_CATEGORIA, M.TITULO, M.DESCRICAO, M.PONTOS_RECOMPENSA, M.ATIVA FROM USUARIO_MISSAO UM INNER JOIN MISSAO M ON M.ID_MISSAO = UM.ID_MISSAO WHERE UM.ID_USUARIO = ? AND UM.STATUS = 'EM_ANDAMENTO' ORDER BY UM.DATA_INICIO";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idUsuario);

            try (ResultSet rs = preparedStatement.executeQuery()) {

                while (rs.next()) {

                    Missao missao = new Missao();

                    missao.setIdMissao(rs.getInt("ID_MISSAO"));
                    missao.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                    missao.setTitulo(rs.getString("TITULO"));
                    missao.setDescricao(rs.getString("DESCRICAO"));
                    missao.setPontosRecompensa(rs.getInt("PONTOS_RECOMPENSA"));
                    missao.setAtiva(rs.getString("ATIVA"));

                    missoes.add(missao);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar missões em andamento: " + e.getMessage());
        }

        return missoes;
    }

    @Override
    public boolean cancelarMissao(int idUsuario, int idMissao) {

        String sql = "UPDATE USUARIO_MISSAO SET STATUS = 'CANCELADA' WHERE ID_USUARIO = ? AND ID_MISSAO = ? AND STATUS = 'EM_ANDAMENTO'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, idMissao);

            int linhasAfetadas = preparedStatement.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Missão cancelada.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cancelar missão: " + e.getMessage());
        }

        return false;
    }
}