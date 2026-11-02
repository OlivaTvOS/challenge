package com.artur.slup.dto;

import java.util.List;

public class GrupoIA {

    private int clusterId;
    private String perfilRecomendado;
    private int quantidadeUsuarios;
    private List<Integer> usuarios;

    public GrupoIA() {
    }

    public GrupoIA(int clusterId, String perfilRecomendado, int quantidadeUsuarios,
                   List<Integer> usuarios) {
        this.clusterId = clusterId;
        this.perfilRecomendado = perfilRecomendado;
        this.quantidadeUsuarios = quantidadeUsuarios;
        this.usuarios = usuarios;
    }

    public int getClusterId() { return clusterId; }
    public void setClusterId(int clusterId) { this.clusterId = clusterId; }
    public String getPerfilRecomendado() { return perfilRecomendado; }
    public void setPerfilRecomendado(String perfilRecomendado) { this.perfilRecomendado = perfilRecomendado; }
    public int getQuantidadeUsuarios() { return quantidadeUsuarios; }
    public void setQuantidadeUsuarios(int quantidadeUsuarios) { this.quantidadeUsuarios = quantidadeUsuarios; }
    public List<Integer> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Integer> usuarios) { this.usuarios = usuarios; }
}
