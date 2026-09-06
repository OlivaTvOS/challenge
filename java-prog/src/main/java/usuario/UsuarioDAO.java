package usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean existeRegistro(String tabela, String coluna, String valor) {
        String sql = "SELECT 1 FROM " + tabela + " WHERE " + coluna + " = ? FETCH FIRST 1 ROWS ONLY";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, valor);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean criarUsuario(Cadastro cadastro) {

        String sql = "INSERT INTO USUARIO (NOME_USUARIO, NOME, EMAIL, SENHA) VALUES (?,?,?,?)";

        // Tratamento novo para retornar o ID e manter a persistência da sessão em uma
        // conta criada
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"ID_USUARIO"})) {

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

            SessaoUsuario.getInstance().setUsuario(cadastro);
            System.out.println("Usuário criado e fixado!");

            // VERIFICAÇÃO 2: Retorna true se pelo menos uma linha foi inserida
            return novosDados > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false; // Retorna false caso aconteça alguma exceção do banco
        }
    }

    public boolean carregarDadosUsuario(int id) {
        String sql = "SELECT NOME_USUARIO, NOME, EMAIL FROM USUARIO WHERE ID_USUARIO = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Cadastro c = new Cadastro();

                    c.setId(rs.getInt("ID_USUARIO"));
                    c.setNomeUsuario(rs.getString("NOME_USUARIO"));
                    c.setNomeCompleto(rs.getString("NOME"));
                    c.setEmail(rs.getString("EMAIL"));

                    SessaoUsuario.getInstance().setUsuario(c);
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar dados do perfil!");
        }
        return false;
    }

    public boolean alterarDadosUsuario(Cadastro alterar){
        String sql = "UPDATE USUARIO SET NOME_USUARIO = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, alterar.getNomeUsuario());

            int novosDados = preparedStatement.executeUpdate();
            
            return novosDados > 0;
            
        } catch (SQLException e) {
            System.out.println("Erro! Não foi possível alterar os dados!");
            return false;
        }
    }
}