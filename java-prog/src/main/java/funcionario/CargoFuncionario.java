package funcionario;

import java.util.Set;

public enum CargoFuncionario {
    ADMIN(Set.of(Permissao.CONSULTAR_USUARIO, Permissao.EDITAR_USUARIO,
            Permissao.EXCLUIR_USUARIO, Permissao.CONSULTAR_FUNCIONARIO, Permissao.CONSULTAR_RESUMO)),
    TECH(Set.of(Permissao.CONSULTAR_USUARIO, Permissao.EDITAR_USUARIO)),
    CEO(Set.of(Permissao.CONSULTAR_USUARIO, Permissao.CONSULTAR_RESUMO));

    private final Set<Permissao> permissoes;

    CargoFuncionario(Set<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    public boolean temPermissao(Permissao permissao) {
        return permissoes.contains(permissao);
    }
}
