package usuario;

public class Cadastro {

    private int id;
    private String nomeUsuario;
    private String nomeCompleto;
    private String email;
    private String senha;
    private String tipoUsuario;

    public Cadastro() {
    }

    public Cadastro(String nomeUsuario, String nomeCompleto, String email, String senha) {

        this.nomeUsuario = nomeUsuario;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = "USUARIO";
    }

    public Cadastro(int id, String nomeUsuario, String nomeCompleto, String email, String senha) {

        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = "USUARIO";
    }

    public int getId(){
        return id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return "Usuário: " + nomeUsuario +
                "\nNome: " + nomeCompleto +
                "\nEmail: " + email +
                "\nTipo: " + tipoUsuario;
    }
}