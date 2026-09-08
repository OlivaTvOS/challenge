package missao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MissaoDAO {

    private final Connection connection;

    public MissaoDAO(Connection connection) {
        this.connection = connection;
    }

    // CRIAR MISSÃO
    public boolean criarMissao(Missao missao) {

        String sql = "INSERT INTO MISSAO (ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[] { "ID_MISSAO" })) {

            preparedStatement.setInt(1, missao.getIdCategoria());
            preparedStatement.setString(2, missao.getTitulo());
            preparedStatement.setString(3, missao.getDescricao());
            preparedStatement.setInt(4, missao.getPontosRecompensa());
            preparedStatement.setString(5, missao.getAtiva());

            int novosDados = preparedStatement.executeUpdate();

            if (novosDados > 0) {

                try (ResultSet getGeneratedKeys = preparedStatement.getGeneratedKeys()) {

                    if (getGeneratedKeys.next()) {
                        int idGerado = getGeneratedKeys.getInt(1);
                        missao.setIdMissao(idGerado);
                    }
                }

                System.out.println("Missão criada com sucesso!");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar missão: " + e.getMessage());
        }

        return false;
    }

    // BUSCAR MISSÃO PELO ID
    public Missao buscarMissao(int idMissao) {

        String sql = "SELECT ID_MISSAO, ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA FROM MISSAO WHERE ID_MISSAO = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idMissao);

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
            System.out.println("Erro ao buscar missão: " + e.getMessage());
        }

        return null;
    }

    // LISTAR TODAS AS MISSÕES ATIVAS
    public List<Missao> listarMissoes() {

        List<Missao> missoes = new ArrayList<>();

        String sql = "SELECT ID_MISSAO, ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA FROM MISSAO WHERE ATIVA = 'S' ORDER BY ID_MISSAO";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

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

        } catch (SQLException e) {
            System.out.println("Erro ao listar missões: " + e.getMessage());
        }

        return missoes;
    }

    // LISTAR MISSÕES POR CATEGORIA
    public List<Missao> listarMissoesPorCategoria(int idCategoria) {

        List<Missao> missoes = new ArrayList<>();

        String sql = "SELECT ID_MISSAO, ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA FROM MISSAO WHERE ID_CATEGORIA = ? AND ATIVA = 'S' ORDER ID_MISSAO ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idCategoria);

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
            System.out.println("Erro ao listar missões da categoria: " + e.getMessage());
        }

        return missoes;
    }

    // ATUALIZAR MISSÃO
    public boolean alterarMissao(Missao missao) {

        String sql = " UPDATE MISSAO SET ID_CATEGORIA = ?, TITULO = ?, DESCRICAO = ?, PONTOS_RECOMPENSA = ?, ATIVA = ? WHERE ID_MISSAO = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, missao.getIdCategoria());
            preparedStatement.setString(2, missao.getTitulo());
            preparedStatement.setString(3, missao.getDescricao());
            preparedStatement.setInt(4, missao.getPontosRecompensa());
            preparedStatement.setString(5, missao.getAtiva());
            preparedStatement.setInt(6, missao.getIdMissao());

            int linhasAfetadas = preparedStatement.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Missão alterada com sucesso!");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao alterar missão: " + e.getMessage());
        }

        return false;
    }

    // DELETAR MISSÃO
    public boolean deletarMissao(int idMissao) {

        String sql = "DELETE FROM MISSAO WHERE ID_MISSAO = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idMissao);

            int linhasAfetadas = preparedStatement.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Missão deletada com sucesso!");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao deletar missão: " + e.getMessage());
        }

        return false;
    }
}