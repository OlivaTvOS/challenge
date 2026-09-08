package missao;

public class Missao {

    private int idMissao;
    private int idCategoria;
    private String titulo;
    private String descricao;
    private int pontosRecompensa;
    private String ativa;

    public Missao() {
    }

    public Missao(int idCategoria, String titulo, String descricao, int pontosRecompensa, String ativa) {
        this.idCategoria = idCategoria;
        this.titulo = titulo;
        this.descricao = descricao;
        this.pontosRecompensa = pontosRecompensa;
        this.ativa = ativa;
    }

    public Missao(int idMissao, int idCategoria, String titulo, String descricao, int pontosRecompensa, String ativa) {
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

    public void setIdMissao(int idMissao) {
        this.idMissao = idMissao;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPontosRecompensa() {
        return pontosRecompensa;
    }

    public void setPontosRecompensa(int pontosRecompensa) {
        this.pontosRecompensa = pontosRecompensa;
    }

    public String getAtiva() {
        return ativa;
    }

    public void setAtiva(String ativa) {
        this.ativa = ativa;
    }

    @Override
    public String toString() {
        return "\nID: " + idMissao +
                "\nCategoria: " + idCategoria +
                "\nTítulo: " + titulo +
                "\nDescrição: " + descricao +
                "\nPontos: " + pontosRecompensa +
                "\nAtiva: " + ativa;
    }
}