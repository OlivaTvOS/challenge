package com.artur.slup.dto;

public class CaracteristicasIA {

    private int idUsuario;
    private int missoesReciclagem;
    private int missoesMobilidade;
    private int missoesAgua;
    private int missoesEnergia;
    private int missoesNatureza;
    private int missoesConsumo;
    private double taxaConclusao;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getMissoesReciclagem() { return missoesReciclagem; }
    public void setMissoesReciclagem(int valor) { this.missoesReciclagem = valor; }
    public int getMissoesMobilidade() { return missoesMobilidade; }
    public void setMissoesMobilidade(int valor) { this.missoesMobilidade = valor; }
    public int getMissoesAgua() { return missoesAgua; }
    public void setMissoesAgua(int valor) { this.missoesAgua = valor; }
    public int getMissoesEnergia() { return missoesEnergia; }
    public void setMissoesEnergia(int valor) { this.missoesEnergia = valor; }
    public int getMissoesNatureza() { return missoesNatureza; }
    public void setMissoesNatureza(int valor) { this.missoesNatureza = valor; }
    public int getMissoesConsumo() { return missoesConsumo; }
    public void setMissoesConsumo(int valor) { this.missoesConsumo = valor; }
    public double getTaxaConclusao() { return taxaConclusao; }
    public void setTaxaConclusao(double taxaConclusao) { this.taxaConclusao = taxaConclusao; }
}
