package com.artur.slup.controller;

import com.artur.slup.dao.UsuarioDAO;
import com.artur.slup.dto.ConfirmacaoExclusao;
import com.artur.slup.dto.RankingItem;
import com.artur.slup.dto.RequisicaoLogin;
import com.artur.slup.dto.RequsicaoExclusao;
import com.artur.slup.dto.Cadastro;
import com.artur.slup.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Cadastro cadastro) {
        try {

            usuarioService.cadastrarUsuario(cadastro);

            return ResponseEntity.status(HttpStatus.CREATED).body(cadastro);

        } catch (IllegalArgumentException e ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor");
        }
    }

    @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody RequisicaoLogin dadosLogin) {
        try {
            Cadastro perfilBanco = usuarioDAO.buscaPorEmail(dadosLogin.getEmail(), dadosLogin.getSenha());

            if (perfilBanco == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha incorretos!");
            }

            return ResponseEntity.ok(perfilBanco);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor");
        }
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingItem>> listarRankingGlobal() {
        return ResponseEntity.ok(usuarioDAO.listarRankingGlobal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cadastro> buscarPorId(@PathVariable int id) {
        try {

            Cadastro perfilBanco = usuarioDAO.carregarDadosUsuario(id);

            Cadastro perfilValido = usuarioService.statusPerfil(perfilBanco);

            return ResponseEntity.ok(perfilValido);

        } catch (IllegalArgumentException e ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarPerfil(@PathVariable int id, @RequestBody Cadastro novosDados) {
        try {

            novosDados.setId(id);

            usuarioService.alterarDadosPerfil(novosDados);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Perfil atualizado com sucesso");

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar perfil.");
        }
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> removerPerfil(@PathVariable int id, @RequestBody ConfirmacaoExclusao confirmacao) {
        try {

            RequsicaoExclusao reqExclusao = new RequsicaoExclusao(id, confirmacao.getSenha());

            usuarioService.removerPerfil(reqExclusao);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Perfil excluído com sucesso");

            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (IllegalArgumentException e ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor");
        }
    }
}
