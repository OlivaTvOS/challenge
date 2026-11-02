package com.artur.slup.dao;

import com.artur.slup.dto.Cadastro;
import com.artur.slup.dto.RankingItem;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UsuarioDAO {

    private final DataSource dataSource;

    public UsuarioDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean existeUsuarioRegistro(String tabela, String coluna, String valor) {
        if (!"USUARIO".equals(tabela)
                || !"NOME_USUARIO".equals(coluna) && !"EMAIL".equals(coluna)) {
            throw new IllegalArgumentException("Consulta de duplicidade nao permitida.");
        }

        String sql = "SELECT 1 FROM " + tabela + " WHERE " + coluna + " = ? FETCH FIRST 1 ROWS ONLY";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, valor);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao verificar registro.", erro);
        }
    }

    public boolean existeNomeUsuarioEmOutroUsuario(String nomeUsuario, int idUsuario) {
        String sql = "SELECT 1 FROM USUARIO WHERE NOME_USUARIO = ? AND ID_USUARIO <> ? FETCH FIRST 1 ROWS ONLY";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nomeUsuario);
            statement.setInt(2, idUsuario);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao verificar nome de usuario.", erro);
        }
    }

    public boolean existeEmailEmOutroUsuario(String email, int idUsuario) {
        String sql = "SELECT 1 FROM USUARIO WHERE EMAIL = ? AND ID_USUARIO <> ? FETCH FIRST 1 ROWS ONLY";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setInt(2, idUsuario);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao verificar e-mail.", erro);
        }
    }

    public boolean criarUsuario(Cadastro cadastro) {
        String sql = "INSERT INTO USUARIO (NOME_USUARIO, NOME, EMAIL, SENHA) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, new String[] { "ID_USUARIO" })) {
            statement.setString(1, cadastro.getNomeUsuario());
            statement.setString(2, cadastro.getNomeCompleto());
            statement.setString(3, cadastro.getEmail());
            statement.setString(4, cadastro.getSenha());

            int linhas = statement.executeUpdate();
            if (linhas == 0) {
                return false;
            }

            try (ResultSet chaves = statement.getGeneratedKeys()) {
                if (chaves.next()) {
                    cadastro.setId(chaves.getInt(1));
                }
            }
            return true;
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao cadastrar usuario.", erro);
        }
    }

    public Cadastro buscarPorEmail(String email) {
        String sql = "SELECT ID_USUARIO, NOME_USUARIO, NOME, EMAIL, SENHA, PONTOS FROM USUARIO WHERE EMAIL = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return montarCadastro(rs, true);
                }
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao buscar usuario pelo e-mail.", erro);
        }
        return null;
    }

    public Cadastro carregarDadosUsuario(int id) {
        String sql = "SELECT ID_USUARIO, NOME_USUARIO, NOME, EMAIL, PONTOS FROM USUARIO WHERE ID_USUARIO = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return montarCadastro(rs, false);
                }
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao buscar dados do perfil.", erro);
        }
        return null;
    }

    public Cadastro buscarUsuarioComSenha(int idUsuario) {
        String sql = "SELECT ID_USUARIO, NOME_USUARIO, NOME, EMAIL, SENHA, PONTOS FROM USUARIO WHERE ID_USUARIO = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return montarCadastro(rs, true);
                }
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao buscar usuario.", erro);
        }
        return null;
    }

    public boolean atualizarSenha(int idUsuario, String senhaHash) {
        String sql = "UPDATE USUARIO SET SENHA = ? WHERE ID_USUARIO = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, senhaHash);
            statement.setInt(2, idUsuario);
            return statement.executeUpdate() > 0;
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao atualizar senha.", erro);
        }
    }

    public boolean alterarDadosUsuario(Cadastro cadastro) {
        String sql = "UPDATE USUARIO SET NOME_USUARIO = ?, NOME = ?, EMAIL = ?, SENHA = NVL(?, SENHA) WHERE ID_USUARIO = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cadastro.getNomeUsuario());
            statement.setString(2, cadastro.getNomeCompleto());
            statement.setString(3, cadastro.getEmail());
            statement.setString(4, cadastro.getSenha());
            statement.setInt(5, cadastro.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao atualizar dados do usuario.", erro);
        }
    }

    public boolean removerPerfil(int idUsuario) {
        String sql = "DELETE FROM USUARIO WHERE ID_USUARIO = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);
            return statement.executeUpdate() > 0;
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao excluir usuario.", erro);
        }
    }

    public List<RankingItem> listarRankingGlobal() {
        List<RankingItem> ranking = new ArrayList<>();
        String sql = "SELECT POSICAO, ID_USUARIO, NOME_USUARIO, PONTOS FROM ("
                + " SELECT ROW_NUMBER() OVER (ORDER BY PONTOS DESC, ID_USUARIO) AS POSICAO,"
                + " ID_USUARIO, NOME_USUARIO, PONTOS FROM USUARIO) ORDER BY POSICAO";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                ranking.add(new RankingItem(
                        rs.getInt("POSICAO"),
                        rs.getInt("ID_USUARIO"),
                        rs.getString("NOME_USUARIO"),
                        rs.getInt("PONTOS")));
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao listar ranking.", erro);
        }
        return ranking;
    }


    public List<RankingItem> listarRankingPorCategoria(String categoria) {
        Map<String, Integer> categorias = Map.of(
                "RECICLAGEM", 1, "MOBILIDADE", 2, "AGUA", 3,
                "ENERGIA", 4, "NATUREZA", 5, "CONSUMO", 6);
        Integer idCategoria = categorias.get(categoria);
        if (idCategoria == null) {
            throw new IllegalArgumentException("Categoria de ranking invalida.");
        }

        List<RankingItem> ranking = new ArrayList<>();
        String sql = "SELECT POSICAO, ID_USUARIO, NOME_USUARIO, PONTOS FROM ("
                + " SELECT ROW_NUMBER() OVER (ORDER BY PONTOS DESC, ID_USUARIO) AS POSICAO,"
                + " ID_USUARIO, NOME_USUARIO, PONTOS FROM ("
                + " SELECT U.ID_USUARIO, U.NOME_USUARIO,"
                + " NVL(SUM(CASE WHEN UM.STATUS = 'CONCLUIDA' THEN M.PONTOS_RECOMPENSA ELSE 0 END), 0) AS PONTOS"
                + " FROM USUARIO U"
                + " LEFT JOIN USUARIO_MISSAO UM ON UM.ID_USUARIO = U.ID_USUARIO"
                + " LEFT JOIN MISSAO M ON M.ID_MISSAO = UM.ID_MISSAO AND M.ID_CATEGORIA = ?"
                + " WHERE U.TIPO_USUARIO = 'USUARIO'"
                + " GROUP BY U.ID_USUARIO, U.NOME_USUARIO"
                + " )) ORDER BY POSICAO";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCategoria);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ranking.add(new RankingItem(rs.getInt("POSICAO"),
                            rs.getInt("ID_USUARIO"), rs.getString("NOME_USUARIO"),
                            rs.getInt("PONTOS")));
                }
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao listar ranking por categoria.", erro);
        }
        return ranking;
    }

    private Cadastro montarCadastro(ResultSet rs, boolean incluirSenha) throws SQLException {
        Cadastro cadastro = new Cadastro();
        cadastro.setId(rs.getInt("ID_USUARIO"));
        cadastro.setNomeUsuario(rs.getString("NOME_USUARIO"));
        cadastro.setNomeCompleto(rs.getString("NOME"));
        cadastro.setEmail(rs.getString("EMAIL"));
        cadastro.setPontos(rs.getInt("PONTOS"));
        if (incluirSenha) {
            cadastro.setSenha(rs.getString("SENHA"));
        }
        return cadastro;
    }
}
