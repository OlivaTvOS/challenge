package com.artur.slup.dao;

import com.artur.slup.dto.Missao;

import java.util.List;

public interface IUsuarioMissaoDAO {

    boolean iniciarMissao(int idUsuario, int idMissao);

    boolean concluirMissao(int idUsuario, int idMissao);

    List<Missao> listarMissoesEmAndamento(int idUsuario);

    List<Missao> listarMissoesConcluidas(int idUsuario);
}
