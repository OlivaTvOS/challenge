package usuario;

/**
 * Exceção específica para quando uma operação espera encontrar um usuário
 * (por e-mail, id ou nome de usuário) e ele não existe.
 * Não é usada nas DAOs ainda — hoje elas sinalizam esse caso retornando
 * "false" ou "null". Fica pronta caso vocês decidam adotar exceções
 * customizadas em vez de retorno booleano/nulo no futuro.
 */
public class UsuarioNaoEncontradoException extends Exception {

    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
