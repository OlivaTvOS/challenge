package com.artur.slup.service;

import com.artur.slup.dao.UsuarioDAO;
import com.artur.slup.dto.Cadastro;
import com.artur.slup.dto.RequsicaoExclusao;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void cadastrarUsuario(@NonNull Cadastro cadastro) {
        validarDados(cadastro);

        if (usuarioDAO.existeUsuarioRegistro("USUARIO", "NOME_USUARIO", cadastro.getNomeUsuario())) {
            throw new IllegalArgumentException("Este nome de usuário já existe! Escolha outro.");
        }
        if (usuarioDAO.existeUsuarioRegistro("USUARIO", "EMAIL", cadastro.getEmail())) {
            throw new IllegalArgumentException("Este email já está vinculado a uma conta! Escolha outro.");
        }
        if (!usuarioDAO.criarUsuario(cadastro)) {
            throw new RuntimeException("Não foi possível criar o perfil por um erro interno.");
        }
    }

    public Cadastro statusPerfil(Cadastro perfil) {
        if (perfil == null) throw new IllegalArgumentException("Usuários não encontrados!");
        return perfil;
    }

    public void alterarDadosPerfil(@NonNull Cadastro cadastro) {
        validarDados(cadastro);

        if (usuarioDAO.existeUsuarioRegistro("USUARIO", "NOME_USUARIO", cadastro.getNomeUsuario())) {
            throw new IllegalArgumentException("Este nome de usuário já existe! Escolha outro.");
        }
        if (usuarioDAO.existeEmailEmOutroUsuario(cadastro.getEmail(), cadastro.getId())) {
            throw new IllegalArgumentException("Este email já está vinculado a uma conta! Escolha outro.");
        }
        if (!usuarioDAO.alterarDadosUsuario(cadastro)) {
            throw new RuntimeException("Não foi possível atualizar o perfil por um erro interno.");
        }
    }

    public void removerPerfil(@NonNull RequsicaoExclusao requisicao) {
        if (requisicao.getSenhaPerfil() == null || requisicao.getSenhaPerfil().trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! Você não pode deixar sua senha em branco");
        }
        if (!usuarioDAO.removerPerfil(requisicao.getIdLogado(), requisicao.getSenhaPerfil())) {
            throw new IllegalArgumentException("Não foi possível excluir o perfil! Senha incorreta ou usuário inexistente");
        }
    }

    private void validarDados(Cadastro cadastro) {
        if (cadastro.getNomeUsuario() == null || cadastro.getNomeUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! Seu usuário não pode estar vazio!");
        }
        if (cadastro.getNomeCompleto() == null || cadastro.getNomeCompleto().trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! Você não pode deixar seu nome vazio");
        }
        if (cadastro.getEmail() == null || cadastro.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! Seu email não pode estar vazio");
        }
        if (cadastro.getSenha() == null || cadastro.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! Você não pode deixar sua senha em branco");
        }
    }
}
