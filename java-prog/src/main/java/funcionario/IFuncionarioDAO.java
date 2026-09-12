package funcionario;

import java.util.List;

public interface IFuncionarioDAO {
    boolean existeId(int id);
    boolean existeEmail(String email);
    int gerarIdDisponivel();
    String gerarEmail(String nome, String sobrenome);
    boolean cadastrar(Funcionario funcionario);
    Funcionario autenticar(int id, String email, String senha);
    List<Funcionario> listar();
}
