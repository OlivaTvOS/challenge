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
public class MissaoDAO implements IMissaoDAO {

    private final DataSource dataSource;

    public MissaoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean criarMissao(Missao missao) {
        String sql = "INSERT INTO MISSAO (ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, new String[] { "ID_MISSAO" })) {
            statement.setInt(1, missao.getIdCategoria());
            statement.setString(2, missao.getTitulo());
            statement.setString(3, missao.getDescricao());
            statement.setInt(4, missao.getPontosRecompensa());
            statement.setString(5, missao.getAtiva());

            if (statement.executeUpdate() == 0) {
                return false;
            }

            try (ResultSet chaves = statement.getGeneratedKeys()) {
                if (chaves.next()) {
                    missao.setIdMissao(chaves.getInt(1));
                }
            }
            return true;
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao cadastrar missao.", erro);
        }
    }

    @Override
    public List<Missao> listarMissoes() {
        List<Missao> missoes = new ArrayList<>();
        String sql = "SELECT ID_MISSAO, ID_CATEGORIA, TITULO, DESCRICAO, PONTOS_RECOMPENSA, ATIVA FROM MISSAO WHERE ATIVA = 'S' ORDER BY ID_MISSAO";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
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
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao listar missoes.", erro);
        }
        return missoes;
    }

    @Override
    public int contarMissoes() {
        String sql = "SELECT COUNT(*) FROM MISSAO";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao contar missoes.", erro);
        }
    }
}
