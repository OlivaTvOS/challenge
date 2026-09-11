package missao;

/**
 * Exceção específica para operações inválidas sobre uma missão — por
 * exemplo, tentar concluir uma missão que não está em andamento, ou uma
 * missão inativa/inexistente.
 * Mesma observação da UsuarioNaoEncontradoException: criada como referência
 * pronta para uso, não foi conectada às DAOs existentes.
 */
public class MissaoIndisponivelException extends Exception {

    public MissaoIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
