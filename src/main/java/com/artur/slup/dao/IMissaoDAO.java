package com.artur.slup.dao;

import com.artur.slup.dto.Missao;

import java.util.List;

public interface IMissaoDAO {

    boolean criarMissao(Missao missao);

    List<Missao> listarMissoes();

    int contarMissoes();
}
