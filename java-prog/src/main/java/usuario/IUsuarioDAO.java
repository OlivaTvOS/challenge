package usuario;

import java.util.List;

/**
 * Contrato das operações de persistência do usuário.
 * Existe para que a camada de regras de negócio (funcoes.java) dependa de um
 * "o que fazer" (a interface), e não de um "como fazer" (a implementação
 * concreta com JDBC/Oracle). Isso facilita reaproveitamento e, futuramente,
 * trocar a implementação (ex: outro banco, um mock para testes) sem precisar
 * mudar quem usa a DAO.
 */
public interface IUsuarioDAO {

    boolean existeUsuarioRegistro(String tabela, String coluna, String valor);

    boolean criarUsuario(Cadastro cadastro);

    boolean carregarDadosUsuario(int id);

    boolean alterarDadosUsuario(Cadastro alterar);

    boolean removerPerfil(int idUsuario, String senhaDigitada);

    boolean logarUsuario(String emailDigitado, String senhaDigitada);

    List<Cadastro> listarUsuarios();

    boolean alterarDadosBasicos(int idUsuario, String nomeCompleto, String email);

    boolean removerUsuarioComoFuncionario(int idUsuario);
}
