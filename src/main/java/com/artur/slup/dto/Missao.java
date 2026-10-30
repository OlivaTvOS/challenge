package com.artur.slup.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor

public class Missao {


    private int idMissao;
    private int idCategoria;
    private String titulo;
    private String descricao;
    private int pontosRecompensa;
    private String ativa;

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
}