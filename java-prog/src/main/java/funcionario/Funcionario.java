package funcionario;

public class Funcionario {
    private int id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senhaHash;
    private CargoFuncionario cargo;

    public Funcionario(int id, String nome, String sobrenome, String email, String senhaHash, CargoFuncionario cargo) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.cargo = cargo;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getSobrenome() { return sobrenome; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
    public CargoFuncionario getCargo() { return cargo; }

    @Override
    public String toString() {
        return "Funcionario{id=" + id + ", nome='" + nome + " " + sobrenome + "', email='" + email + "', cargo=" + cargo + "}";
    }
}
