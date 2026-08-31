package org.example;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection){
        this.connection = connection;
    }

    public boolean usuarioExiste(String nomeUsuario){
        String sql = """
                SELECT ID_USUARIO
                FROM USUARIO
                WHERE ID_USUARIO = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nomeUsuario);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e){
            System.out.println("Erro ao verificar usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean criarUsuario(Cadastro cadastro){
        if (usuarioExiste(cadastro.getNomeUsuario())){
            return false;
        }

        String sql = """
                INSERT INTO USUARIO
                (ID_USUARIO, NOME, EMAIL, SENHA)
                VALUES (?,?,?,?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cadastro.getNomeUsuario());
            statement.setString(2, cadastro.getNomeCompleto());
            statement.setString(3, cadastro.getEmail());
            statement.setString(4, cadastro.getSenha());
        } catch (SQLException e){
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
        return false;
    }
}
