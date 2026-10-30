package com.artur.slup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Cadastro {

    private int id;

    private String nomeUsuario;
    private String nomeCompleto;
    private String email;
    private String senha;
    private int pontos;
    private String tipoUsuario = "USUARIO";

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmarSenha;
}