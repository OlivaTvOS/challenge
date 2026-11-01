package com.artur.slup.service;

import com.artur.slup.dao.UsuarioDAO;
import com.artur.slup.dto.Cadastro;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void cadastrarUsuario(Cadastro cadastro) {
        validarDados(cadastro, true);
        if (usuarioDAO.existeUsuarioRegistro("USUARIO", "NOME_USUARIO", cadastro.getNomeUsuario())) {
            throw new IllegalArgumentException("Este nome de usuario ja existe. Escolha outro.");
        }
        if (usuarioDAO.existeUsuarioRegistro("USUARIO", "EMAIL", cadastro.getEmail())) {
            throw new IllegalArgumentException("Este e-mail ja esta cadastrado. Escolha outro.");
        }
        cadastro.setSenha(PasswordUtils.gerarHash(cadastro.getSenha()));
        if (!usuarioDAO.criarUsuario(cadastro)) {
            throw new IllegalStateException("Nao foi possivel criar o perfil.");
        }
        cadastro.setSenha(null);
        cadastro.setConfirmarSenha(null);
    }

    public Cadastro autenticar(String email, String senha) {
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o e-mail e a senha.");
        }

        Cadastro usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario == null || !PasswordUtils.verificar(senha, usuario.getSenha())) {
            throw new IllegalArgumentException("E-mail ou senha incorretos.");
        }

        // Contas antigas da carga inicial sao atualizadas para hash no primeiro login.
        if (!PasswordUtils.jaEstaEmHash(usuario.getSenha())) {
            usuarioDAO.atualizarSenha(usuario.getId(), PasswordUtils.gerarHash(senha));
        }

        usuario.setSenha(null);
        usuario.setConfirmarSenha(null);
        return usuario;
    }

    public Cadastro statusPerfil(Cadastro perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("Usuario nao encontrado.");
        }
        return perfil;
    }

    public void alterarDadosPerfil(Cadastro cadastro) {
        validarDados(cadastro, false);

        if (usuarioDAO.existeNomeUsuarioEmOutroUsuario(cadastro.getNomeUsuario(), cadastro.getId())) {
            throw new IllegalArgumentException("Este nome de usuario ja esta cadastrado por outra conta.");
        }
        if (usuarioDAO.existeEmailEmOutroUsuario(cadastro.getEmail(), cadastro.getId())) {
            throw new IllegalArgumentException("Este e-mail ja esta cadastrado por outra conta.");
        }

        if (cadastro.getSenha() == null || cadastro.getSenha().trim().isEmpty()) {
            // NVL no DAO conserva a senha atual quando o campo vem vazio.
            cadastro.setSenha(null);
        } else {
            cadastro.setSenha(PasswordUtils.gerarHash(cadastro.getSenha()));
        }

        if (!usuarioDAO.alterarDadosUsuario(cadastro)) {
            throw new IllegalArgumentException("Usuario nao encontrado para atualizacao.");
        }
    }

    public void removerPerfil(int idUsuario, String senhaDigitada) {
        if (senhaDigitada == null || senhaDigitada.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a senha para confirmar a exclusao.");
        }
        Cadastro usuario = usuarioDAO.buscarUsuarioComSenha(idUsuario);
        if (usuario == null || !PasswordUtils.verificar(senhaDigitada, usuario.getSenha())) {
            throw new IllegalArgumentException("Senha incorreta ou usuario inexistente.");
        }
        if (!usuarioDAO.removerPerfil(idUsuario)) {
            throw new IllegalStateException("Nao foi possivel excluir o perfil.");
        }
    }

    private void validarDados(Cadastro cadastro, boolean exigirSenha) {
        if (cadastro == null) {
            throw new IllegalArgumentException("Os dados do usuario nao foram enviados.");
        }
        if (cadastro.getNomeUsuario() == null || cadastro.getNomeUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome de usuario e obrigatorio.");
        }
        if (cadastro.getNomeCompleto() == null || cadastro.getNomeCompleto().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome completo e obrigatorio.");
        }
        if (cadastro.getEmail() == null || cadastro.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail e obrigatorio.");
        }
        if (exigirSenha && (cadastro.getSenha() == null || cadastro.getSenha().trim().isEmpty())) {
            throw new IllegalArgumentException("A senha e obrigatoria.");
        }
        if (exigirSenha && cadastro.getConfirmarSenha() != null
                && !cadastro.getSenha().equals(cadastro.getConfirmarSenha())) {
            throw new IllegalArgumentException("A confirmacao da senha nao confere.");
        }
    }
}
