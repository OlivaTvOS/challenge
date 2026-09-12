package funcionario;

public final class AutorizacaoFuncionario {
    private AutorizacaoFuncionario() {}

    public static boolean autorizar(Permissao permissao) {
        Funcionario funcionario = SessaoFuncionario.getInstance().getFuncionario();
        if (funcionario == null || !funcionario.getCargo().temPermissao(permissao)) {
            System.out.println("Acesso negado.");
            return false;
        }
        return true;
    }
}
