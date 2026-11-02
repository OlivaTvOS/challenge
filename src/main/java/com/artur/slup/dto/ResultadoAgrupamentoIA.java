package com.artur.slup.dto;

import java.util.List;

public class ResultadoAgrupamentoIA {

    private int totalUsuarios;
    private List<GrupoIA> grupos;
    private String modelo;
    private String versaoModelo;

    public ResultadoAgrupamentoIA() {
    }

    public ResultadoAgrupamentoIA(int totalUsuarios, List<GrupoIA> grupos,
                                  String modelo, String versaoModelo) {
        this.totalUsuarios = totalUsuarios;
        this.grupos = grupos;
        this.modelo = modelo;
        this.versaoModelo = versaoModelo;
    }

    public int getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(int totalUsuarios) { this.totalUsuarios = totalUsuarios; }
    public List<GrupoIA> getGrupos() { return grupos; }
    public void setGrupos(List<GrupoIA> grupos) { this.grupos = grupos; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getVersaoModelo() { return versaoModelo; }
    public void setVersaoModelo(String versaoModelo) { this.versaoModelo = versaoModelo; }
}
