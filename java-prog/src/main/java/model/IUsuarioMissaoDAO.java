package model;

import java.util.List;

/**
 * Contrato das operações sobre a relação usuário x missão (iniciar, concluir,
 * cancelar, listar em andamento). Mesmo padrão das outras duas interfaces.
 */
public interface IUsuarioMissaoDAO {

    boolean iniciarMissao(int idUsuario, int idMissao);

    Missao buscarMissaoEmAndamento(int idUsuario, int idMissao);

    boolean concluirMissao(int idUsuario, int idMissao);

    List<Missao> listarMissoesEmAndamento(int idUsuario);

    boolean cancelarMissao(int idUsuario, int idMissao);
}
