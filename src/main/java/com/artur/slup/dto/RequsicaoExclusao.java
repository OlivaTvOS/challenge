package com.artur.slup.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class RequsicaoExclusao {
    private int idLogado;
    private String senhaPerfil;

    public RequsicaoExclusao(int idLogado, String senhaPerfil) {
        this.idLogado = idLogado;
        this.senhaPerfil = senhaPerfil;
    }
}
