package com.artur.slup.dao;

import com.artur.slup.dto.Cadastro;
import com.artur.slup.dto.RankingItem;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UsuarioDAO {

    private final Connection connection;

    @Autowired
    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean existeUsuarioRegistro(String tabela, String coluna, String valor) {
        String sql = "SELECT 1 FROM " + tabela + " WHERE " + coluna + " = ? FETCH FIRST 1 ROWS ONLY";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, valor);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar no registro: ", e);
        }
    }

    public boolean existeEmailEmOutroUsuario(String email, int idUsuario) {
        String sql = "SELECT 1 FROM USUARIO WHERE EMAIL = ? AND ID_USUARIO <> ? FETCH FIRST 1 ROWS ONLY";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, idUsuario);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar o email de outro usuário: ", e);
        }
    }


    public boolean criarUsuario(Cadastro cadastro) {

        String sql = "INSERT INTO USUARIO (NOME_USUARIO, NOME, EMAIL, SENHA) VALUES (?,?,?,?)";

        // Tratamento novo para retornar o ID e manter a persistência da sessão em uma
        // conta criada
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[] { "ID_USUARIO" })) {

            preparedStatement.setString(1, cadastro.getNomeUsuario());
            preparedStatement.setString(2, cadastro.getNomeCompleto());
            preparedStatement.setString(3, cadastro.getEmail());
            preparedStatement.setString(4, cadastro.getSenha());

            // VERIFICAÇÃO 1: Executa o comando de inserção no banco de dados
            int novosDados = preparedStatement.executeUpdate();

            if (novosDados > 0) {
                try (ResultSet getGeneratedKeys = preparedStatement.getGeneratedKeys()) {
                    if (getGeneratedKeys.next()) {
                        int idGerado = getGeneratedKeys.getInt(1);

                        cadastro.setId(idGerado);
                    }
                }
            }

            // VERIFICAÇÃO 2: Retorna true se pelo menos uma linha foi inserida
            return novosDados > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar usuário: ", e);
        }
    }

    public Cadastro buscaPorEmail(String email, String senha){
        String sql = "SELECT ID_USUARIO, NOME_USUARIO, NOME, EMAIL, PONTOS FROM USUARIO WHERE EMAIL = ? AND SENHA = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, senha);

            try (ResultSet rs = preparedStatement.executeQuery()){
                if (rs.next()) {
                    Cadastro c = new Cadastro();
                    c.setId(rs.getInt("ID_USUARIO"));
                    c.setNomeUsuario(rs.getString("NOME_USUARIO"));
                    c.setNomeCompleto(rs.getString("NOME"));
                    c.setEmail(rs.getString("EMAIL"));
                    c.setPontos(rs.getInt("PONTOS"));
                    return c;
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Erro ao autenticar usuário");
        }
        return null;
    }


    public Cadastro carregarDadosUsuario(int id) {
        String sql = "SELECT ID_USUARIO, NOME_USUARIO, NOME, EMAIL, PONTOS FROM USUARIO WHERE ID_USUARIO = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Cadastro c = new Cadastro();

                    c.setId(rs.getInt("ID_USUARIO"));
                    c.setNomeUsuario(rs.getString("NOME_USUARIO"));
                    c.setNomeCompleto(rs.getString("NOME"));
                    c.setEmail(rs.getString("EMAIL"));
                    c.setPontos(rs.getInt("PONTOS"));

                    return c;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar dados do perfil!");
        }
        return null;
    }


    public boolean alterarDadosUsuario(@NonNull Cadastro alterar) {

        String sqlUpdate = "UPDATE USUARIO SET NOME_USUARIO = ?, NOME = ?, EMAIL = ?, SENHA = ? WHERE ID_USUARIO = ?";

            // --- ETAPA 2: ALTERAÇÃO DOS DADOS (Só executa se a senha bateu) ---
            try (PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate)) {
                stmtUpdate.setString(1, alterar.getNomeUsuario());
                stmtUpdate.setString(2, alterar.getNomeCompleto());
                stmtUpdate.setString(3, alterar.getEmail());
                stmtUpdate.setString(4, alterar.getSenha()); // Nova senha
                stmtUpdate.setInt(5, alterar.getId());

                int linhasAfetadas = stmtUpdate.executeUpdate();

                return linhasAfetadas > 0;

            } catch (SQLException e) {
            throw new RuntimeException("Erro crítico no banco de dados: ", e);
        }
    }


    public boolean removerPerfil(int idUsuario, String senhaDigitada){
        String sqlRemoverPerfil = "DELETE FROM USUARIO WHERE ID_USUARIO = ? AND SENHA = ?";

        try (PreparedStatement removerPerfil = connection.prepareStatement(sqlRemoverPerfil)) {
            removerPerfil.setInt(1, idUsuario);
            removerPerfil.setString(2, senhaDigitada);

            int linhasAfetadas = removerPerfil.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar no banco! ", e);
        }
    }


    public List<RankingItem> listarRankingGlobal() {
        List<RankingItem> ranking = new java.util.ArrayList<>();
        String sql = """
                SELECT POSICAO, ID_USUARIO, NOME_USUARIO, PONTOS
                FROM (
                    SELECT ROW_NUMBER() OVER (ORDER BY PONTOS DESC, ID_USUARIO) AS POSICAO,
                           ID_USUARIO,
                           NOME_USUARIO,
                           PONTOS
                    FROM USUARIO
                )
                ORDER BY POSICAO
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                ranking.add(new RankingItem(
                        rs.getInt("POSICAO"),
                        rs.getInt("ID_USUARIO"),
                        rs.getString("NOME_USUARIO"),
                        rs.getInt("PONTOS")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar o ranking global: ", e);
        }
        return ranking;
    }
}
