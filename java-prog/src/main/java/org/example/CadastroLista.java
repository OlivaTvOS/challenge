package org.example;

import java.util.HashMap;

public class CadastroLista {
    private HashMap<String, Cadastro> cadastroHashMap = new HashMap<>();

    public boolean criarUsuario(Cadastro cadastro){
        if (cadastroHashMap.containsKey(cadastro.getNomeUsuario())){
            return false;
        }
        cadastroHashMap.put(
                cadastro.getNomeUsuario(),
                cadastro
        );

        return true;
    }

    public Cadastro retornoLista(String nomeUsuario){
        return cadastroHashMap.get(nomeUsuario);
    }
}
