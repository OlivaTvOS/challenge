package funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FuncionarioDAO implements IFuncionarioDAO {
    private final Connection connection;

    public FuncionarioDAO(Connection connection) { this.connection = connection; }

    @Override
    public boolean existeId(int id) {
        return existe("SELECT 1 FROM CREDENCIAIS WHERE ID_FUNCIONARIO = ?", id, null);
    }

    @Override
    public boolean existeEmail(String email) {
        return existe("SELECT 1 FROM CREDENCIAIS WHERE LOWER(EMAIL) = ?", null, email.toLowerCase());
    }

    private boolean existe(String sql, Integer id, String email) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (id != null) stmt.setInt(1, id); else stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar credencial: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int gerarIdDisponivel() {
        List<Integer> ids = new ArrayList<>();
        for (int id = 1; id <= 200; id++) ids.add(id);
        Collections.shuffle(ids);
        for (int id : ids) if (!existeId(id)) return id;
        return -1;
    }

    @Override
    public String gerarEmail(String nome, String sobrenome) {
        String base = (nome.trim() + "." + sobrenome.trim()).toLowerCase().replace(" ", "");
        String email = base + "@kairos.com";
        int numero = 1;
        while (existeEmail(email)) email = base + numero++ + "@kairos.com";
        return email;
    }

    @Override
    public boolean cadastrar(Funcionario funcionario) {
        String sql = "INSERT INTO CREDENCIAIS (ID_FUNCIONARIO, NOME, SOBRENOME, EMAIL, SENHA, CARGO) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, funcionario.getId()); stmt.setString(2, funcionario.getNome());
            stmt.setString(3, funcionario.getSobrenome()); stmt.setString(4, funcionario.getEmail());
            stmt.setString(5, funcionario.getSenhaHash()); stmt.setString(6, funcionario.getCargo().name());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar funcionário: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Funcionario autenticar(int id, String email, String senha) {
        String sql = "SELECT NOME, SOBRENOME, EMAIL, SENHA, CARGO FROM CREDENCIAIS WHERE ID_FUNCIONARIO = ? AND LOWER(EMAIL) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id); stmt.setString(2, email.toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getString("SENHA").equals(SenhaUtil.gerarHash(senha))) {
                    return new Funcionario(id, rs.getString("NOME"), rs.getString("SOBRENOME"), rs.getString("EMAIL"), rs.getString("SENHA"), CargoFuncionario.valueOf(rs.getString("CARGO")));
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Erro ao autenticar funcionário: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Funcionario> listar() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT ID_FUNCIONARIO, NOME, SOBRENOME, EMAIL, CARGO FROM CREDENCIAIS ORDER BY ID_FUNCIONARIO";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) funcionarios.add(new Funcionario(rs.getInt("ID_FUNCIONARIO"), rs.getString("NOME"), rs.getString("SOBRENOME"), rs.getString("EMAIL"), null, CargoFuncionario.valueOf(rs.getString("CARGO"))));
        } catch (SQLException e) { System.out.println("Erro ao listar funcionários: " + e.getMessage()); }
        return funcionarios;
    }
}
