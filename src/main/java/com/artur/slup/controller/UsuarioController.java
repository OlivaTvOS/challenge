package com.artur.slup.controller;

import com.artur.slup.dao.UsuarioDAO;
import com.artur.slup.dto.Cadastro;
import com.artur.slup.dto.ConfirmacaoExclusao;
import com.artur.slup.dto.RankingItem;
import com.artur.slup.dto.RequisicaoLogin;
import com.artur.slup.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioDAO usuarioDAO;

    public UsuarioController(UsuarioService usuarioService, UsuarioDAO usuarioDAO) {
        this.usuarioService = usuarioService;
        this.usuarioDAO = usuarioDAO;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Cadastro cadastro) {
        try {
            usuarioService.cadastrarUsuario(cadastro);
            return ResponseEntity.status(HttpStatus.CREATED).body(cadastro);
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.getMessage());
        } catch (Exception erro) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Nao foi possivel cadastrar o usuario.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequisicaoLogin dadosLogin) {
        try {
            Cadastro usuario = usuarioService.autenticar(dadosLogin.getEmail(), dadosLogin.getSenha());
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro.getMessage());
        } catch (Exception erro) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Nao foi possivel concluir o login.");
        }
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingItem>> listarRankingGlobal() {
        return ResponseEntity.ok(usuarioDAO.listarRankingGlobal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        try {
            Cadastro perfil = usuarioService.statusPerfil(usuarioDAO.carregarDadosUsuario(id));
            return ResponseEntity.ok(perfil);
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro.getMessage());
        }
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarPerfil(@PathVariable int id, @RequestBody Cadastro novosDados) {
        try {
            novosDados.setId(id);
            usuarioService.alterarDadosPerfil(novosDados);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("message", "Perfil atualizado com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.getMessage());
        } catch (Exception erro) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Nao foi possivel atualizar o perfil.");
        }
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> removerPerfil(@PathVariable int id,
                                           @RequestBody ConfirmacaoExclusao confirmacao) {
        try {
            usuarioService.removerPerfil(id, confirmacao.getSenha());
            Map<String, String> resposta = new HashMap<>();
            resposta.put("message", "Perfil excluido com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.getMessage());
        } catch (Exception erro) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Nao foi possivel excluir o perfil.");
        }
    }
}
