package missao;

public class Missao {
    private int idMissao;
    private int idCategoria;
    private String titulo;
    private String descricao;
    private int pontosRecompensa;
    private boolean ativa;

    public Missao() {
    }

    public Missao(int idMissao, int idCategoria, String titulo, String descricao, int pontosRecompensa, boolean ativa) {
        this.idMissao = idMissao;
        this.idCategoria = idCategoria;
        this.titulo = titulo;
        this.descricao = descricao;
        this.pontosRecompensa = pontosRecompensa;
        this.ativa = ativa;
    }

    public int getIdMissao() {
        return idMissao;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getPontosRecompensa() {
        return pontosRecompensa;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setIdMissao(int idMissao) {
        this.idMissao = idMissao;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPontosRecompensa(int pontosRecompensa) {
        this.pontosRecompensa = pontosRecompensa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }  

}
