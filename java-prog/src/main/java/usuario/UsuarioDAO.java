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

    public boolean existeUsuarioRegistro(String tabela, String coluna, String valor) {
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

    public boolean alterarDadosUsuario(Cadastro alterar/* , String senhaAtualDigitada */) {
        // 1. Query para buscar a senha atual direto do banco de dados
        /* String sqlBuscarSenha = "SELECT SENHA FROM USUARIO WHERE ID_USUARIO = ?"; */
        // 2. Sua Query original de atualização
        String sqlUpdate = "UPDATE USUARIO SET NOME_USUARIO = ?, NOME = ?, EMAIL = ?, SENHA = ? WHERE ID_USUARIO = ?";

        /* try {
            // --- ETAPA 1: VERIFICAÇÃO DE SENHA ---
            try (PreparedStatement stmtBusca = connection.prepareStatement(sqlBuscarSenha)) {
                stmtBusca.setInt(1, alterar.getId());

                try (ResultSet rs = stmtBusca.executeQuery()) {
                    if (rs.next()) {
                        String senhaDoBanco = rs.getString("SENHA");

                        // Compara a senha do banco com a que o usuário acabou de digitar
                        if (!senhaDoBanco.equals(senhaAtualDigitada)) {
                            System.out.println("Aviso: A senha atual digitada está incorreta.");
                            return false; // Cancela o método aqui e não altera nada
                        }
                    } else {
                        System.out.println("Aviso: Usuário não encontrado no banco de dados.");
                        return false;
                    }
                }
            } */

            // --- ETAPA 2: ALTERAÇÃO DOS DADOS (Só executa se a senha bateu) ---
            try (PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate)) {
                stmtUpdate.setString(1, alterar.getNomeUsuario());
                stmtUpdate.setString(2, alterar.getNomeCompleto());
                stmtUpdate.setString(3, alterar.getEmail());
                stmtUpdate.setString(4, alterar.getSenha()); // Nova senha
                stmtUpdate.setInt(5, alterar.getId());

                int linhasAfetadas = stmtUpdate.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Sucesso! Os dados foram alterados no banco.");

                    // Só atualiza a sessão do aplicativo após confirmar o sucesso no banco
                    SessaoUsuario.getInstance().setUsuario(alterar);
                    return true;
                } else {
                    System.out.println("Erro: Não foi possível atualizar os dados.");
                    return false;
                }
            } catch (SQLException e) {
            System.out.println("Erro crítico no banco de dados: " + e.getMessage());
            return false;
        }
    }
}