package funcionario;

public class SessaoFuncionario {
    private static SessaoFuncionario instance;
    private Funcionario funcionarioLogado;

    private SessaoFuncionario() {}

    public static SessaoFuncionario getInstance() {
        if (instance == null) instance = new SessaoFuncionario();
        return instance;
    }

    public Funcionario getFuncionario() { return funcionarioLogado; }
    public void setFuncionario(Funcionario funcionario) { this.funcionarioLogado = funcionario; }
    public boolean estaLogado() { return funcionarioLogado != null; }
    public void logout() { funcionarioLogado = null; }
}
