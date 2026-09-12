package usuario;

public class SessaoUsuario {
    private static SessaoUsuario instance;
    private Cadastro usuarioLogado;

    private SessaoUsuario() {}

    public static SessaoUsuario getInstance(){
        if (instance == null){
            instance = new SessaoUsuario();
        }
        return instance;
    }

    public void setUsuario(Cadastro cadastro) {
        this.usuarioLogado = cadastro;
    }

    public Cadastro getUsuario() {
        return this.usuarioLogado;
    }

    public boolean temUsuarioAtivo() {
        return this.usuarioLogado != null;
    }

    public void logout() {
        this.usuarioLogado = null;
    }
}
