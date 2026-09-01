package org.example;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    private final Connection connection;

    public UsuarioDAO(Connection connection){
        this.connection =  connection;
    }

    public boolean existeRegistro(String tabela, String coluna, String valor){
        String sql = "SELECT 1 FROM " + tabela + " WHERE " + coluna + " = ? FETCH FIRST 1 ROWS ONLY";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, valor);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e){
            System.err.println("Erro ao verificar usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean criarUsuario(Cadastro cadastro){

        String sql = """
            INSERT INTO USUARIO
            (NOME_USUARIO, NOME, EMAIL, SENHA)
            VALUES (?,?,?,?)
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cadastro.getNomeUsuario());
            statement.setString(2, cadastro.getNomeCompleto());
            statement.setString(3, cadastro.getEmail());
            statement.setString(4, cadastro.getSenha());

            // CORREÇÃO 1: Executa o comando de inserção no banco de dados
            int novosDados = statement.executeUpdate();

            // CORREÇÃO 2: Retorna true se pelo menos uma linha foi inserida
            return novosDados > 0;

        } catch (SQLException e){
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false; // Retorna false caso aconteça alguma exceção do banco
        }
    }
}
