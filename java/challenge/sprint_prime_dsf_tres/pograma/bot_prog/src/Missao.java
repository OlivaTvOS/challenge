// Classe responsável por representar
// uma missão individual do sistema.
public class Missao {
    private int id;
    private String titulo;
    private String categoria;
    private String descricao;

    // Construtor responsável por inicializar
    // os atributos da missão.
    public Missao(int id, String titulo, String categoria, String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString(){
        return "ID: " + id + " | " + titulo + " | Categoria: " + categoria;
    }
}
