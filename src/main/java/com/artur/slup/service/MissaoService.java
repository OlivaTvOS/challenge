package com.artur.slup.service;

import com.artur.slup.dao.IUsuarioMissaoDAO;
import org.springframework.stereotype.Service;

@Service
public class MissaoService {

    private final IUsuarioMissaoDAO usuarioMissaoDAO;

    public MissaoService(IUsuarioMissaoDAO usuarioMissaoDAO) {
        this.usuarioMissaoDAO = usuarioMissaoDAO;
    }

    public void iniciarMissao(int idUsuario, int idMissao) {
        if (!usuarioMissaoDAO.iniciarMissao(idUsuario, idMissao)) {
            throw new IllegalArgumentException("Não foi possível iniciar a missão. Verifique se ela já foi iniciada ou concluída.");
        }
    }

    public void concluirMissao(int idUsuario, int idMissao) {
        if (!usuarioMissaoDAO.concluirMissao(idUsuario, idMissao)) {
            throw new IllegalArgumentException("Erro ao concluir a missão. Certifique-se de que ela está em andamento.");
        }
    }
}
