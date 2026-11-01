package com.artur.slup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Cadastro {

    private int id;
    private String nomeUsuario;
    private String nomeCompleto;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    private int pontos;
    private String tipoUsuario = "USUARIO";

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmarSenha;
}
