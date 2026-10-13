package model;

import java.util.List;

/**
 * Contrato das operações de persistência da missão (CRUD completo).
 * Mesmo raciocínio do IUsuarioDAO: separa "o que a DAO faz" de "como ela faz",
 * deixando o código mais reaproveitável e fácil de entender por quem só
 * precisa saber quais operações existem.
 */
public interface IMissaoDAO {

    boolean criarMissao(Missao missao);

    Missao buscarMissao(int idMissao);

    List<Missao> listarMissoes();

    List<Missao> listarMissoesPorCategoria(int idCategoria);

    boolean alterarMissao(Missao missao);

    boolean deletarMissao(int idMissao);
}
