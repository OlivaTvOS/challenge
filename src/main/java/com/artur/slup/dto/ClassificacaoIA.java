package com.artur.slup.dto;

public class ClassificacaoIA {

    private int idUsuario;
    private int clusterId;
    private String perfilPrevisto;
    private CaracteristicasIA caracteristicas;
    private String modelo;
    private Double confianca;
    private Double pontosEstimados;

    public ClassificacaoIA() {
    }

    public ClassificacaoIA(int idUsuario, int clusterId, String perfilPrevisto,
                           CaracteristicasIA caracteristicas) {
        this.idUsuario = idUsuario;
        this.clusterId = clusterId;
        this.perfilPrevisto = perfilPrevisto;
        this.caracteristicas = caracteristicas;
        this.modelo = "KMEANS";
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getClusterId() { return clusterId; }
    public void setClusterId(int clusterId) { this.clusterId = clusterId; }
    public String getPerfilPrevisto() { return perfilPrevisto; }
    public void setPerfilPrevisto(String perfilPrevisto) { this.perfilPrevisto = perfilPrevisto; }
    public CaracteristicasIA getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(CaracteristicasIA caracteristicas) { this.caracteristicas = caracteristicas; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public Double getConfianca() { return confianca; }
    public void setConfianca(Double confianca) { this.confianca = confianca; }
    public Double getPontosEstimados() { return pontosEstimados; }
    public void setPontosEstimados(Double pontosEstimados) { this.pontosEstimados = pontosEstimados; }
}
