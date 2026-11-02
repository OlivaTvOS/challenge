package com.artur.slup.dao;

import com.artur.slup.dto.Missao;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioMissaoDAO implements IUsuarioMissaoDAO {

    private final DataSource dataSource;

    public UsuarioMissaoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean iniciarMissao(int idUsuario, int idMissao) {
        String sql = "INSERT INTO USUARIO_MISSAO (ID_USUARIO, ID_MISSAO, STATUS) "
                + "SELECT ?, M.ID_MISSAO, 'EM_ANDAMENTO' FROM MISSAO M "
                + "WHERE M.ID_MISSAO = ? AND M.ATIVA = 'S' "
                + "AND NOT EXISTS (SELECT 1 FROM USUARIO_MISSAO UM "
                + "WHERE UM.ID_USUARIO = ? AND UM.ID_MISSAO = M.ID_MISSAO "
                + "AND UM.STATUS IN ('EM_ANDAMENTO', 'CONCLUIDA'))";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);
            statement.setInt(2, idMissao);
            statement.setInt(3, idUsuario);
            return statement.executeUpdate() > 0;
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao iniciar a missao.", erro);
        }
    }

    @Override
    public boolean concluirMissao(int idUsuario, int idMissao) {
        String sqlMissao = "SELECT PONTOS_RECOMPENSA FROM MISSAO WHERE ID_MISSAO = ? AND ATIVA = 'S'";
        String sqlConclusao = "UPDATE USUARIO_MISSAO SET STATUS = 'CONCLUIDA', DATA_CONCLUSAO = SYSDATE, PONTOS_RECEBIDOS = ? WHERE ID_USUARIO = ? AND ID_MISSAO = ? AND STATUS = 'EM_ANDAMENTO'";
        String sqlPontos = "UPDATE USUARIO SET PONTOS = PONTOS + ? WHERE ID_USUARIO = ?";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int pontos;
                try (PreparedStatement buscar = connection.prepareStatement(sqlMissao)) {
                    buscar.setInt(1, idMissao);
                    try (ResultSet rs = buscar.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return false;
                        }
                        pontos = rs.getInt("PONTOS_RECOMPENSA");
                    }
                }

                int linhasAtualizadas;
                try (PreparedStatement concluir = connection.prepareStatement(sqlConclusao)) {
                    concluir.setInt(1, pontos);
                    concluir.setInt(2, idUsuario);
                    concluir.setInt(3, idMissao);
                    linhasAtualizadas = concluir.executeUpdate();
                }

                if (linhasAtualizadas == 0) {
                    connection.rollback();
                    return false;
                }

                try (PreparedStatement atualizarPontos = connection.prepareStatement(sqlPontos)) {
                    atualizarPontos.setInt(1, pontos);
                    atualizarPontos.setInt(2, idUsuario);
                    if (atualizarPontos.executeUpdate() == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();
                return true;
            } catch (SQLException | RuntimeException erro) {
                connection.rollback();
                throw erro;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao concluir a missao.", erro);
        }
    }

    @Override
    public List<Missao> listarMissoesEmAndamento(int idUsuario) {
        return listarMissoesPorStatus(idUsuario, "EM_ANDAMENTO");
    }

    @Override
    public List<Missao> listarMissoesConcluidas(int idUsuario) {
        return listarMissoesPorStatus(idUsuario, "CONCLUIDA");
    }

    private List<Missao> listarMissoesPorStatus(int idUsuario, String status) {
        List<Missao> missoes = new ArrayList<>();
        String sql = "SELECT M.ID_MISSAO, M.ID_CATEGORIA, M.TITULO, M.DESCRICAO, M.PONTOS_RECOMPENSA, M.ATIVA "
                + "FROM USUARIO_MISSAO UM INNER JOIN MISSAO M ON M.ID_MISSAO = UM.ID_MISSAO "
                + "WHERE UM.ID_USUARIO = ? AND UM.STATUS = ? ORDER BY UM.DATA_INICIO";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);
            statement.setString(2, status);
            try (ResultSet rs = statement.executeQuery()) {
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
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao listar missoes do usuario.", erro);
        }
        return missoes;
    }
}
