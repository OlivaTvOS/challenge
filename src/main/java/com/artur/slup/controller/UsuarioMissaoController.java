package com.artur.slup.controller;

import com.artur.slup.dao.IUsuarioMissaoDAO;
import com.artur.slup.dto.Missao;
import com.artur.slup.service.MissaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/missoes")
public class UsuarioMissaoController {

    private final MissaoService missaoService;
    private final IUsuarioMissaoDAO usuarioMissaoDAO;

    public UsuarioMissaoController(MissaoService missaoService, IUsuarioMissaoDAO usuarioMissaoDAO) {
        this.missaoService = missaoService;
        this.usuarioMissaoDAO = usuarioMissaoDAO;
    }

    @PostMapping("/{idMissao}/iniciar")
    public ResponseEntity<?> iniciarMissao(@PathVariable int idMissao, @RequestBody int idUsuario) {
        try {
            missaoService.iniciarMissao(idUsuario, idMissao);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("message", "Missao iniciada com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.getMessage());
        }
    }

    @PutMapping("/{idMissao}/concluir")
    public ResponseEntity<?> concluirMissao(@PathVariable int idMissao, @RequestParam int idUsuario) {
        try {
            missaoService.concluirMissao(idUsuario, idMissao);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("message", "Missao concluida com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}/em-andamento")
    public ResponseEntity<List<Missao>> listarMissoesEmAndamento(@PathVariable int idUsuario) {
        return ResponseEntity.ok(usuarioMissaoDAO.listarMissoesEmAndamento(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/concluidas")
    public ResponseEntity<List<Missao>> listarMissoesConcluidas(@PathVariable int idUsuario) {
        return ResponseEntity.ok(usuarioMissaoDAO.listarMissoesConcluidas(idUsuario));
    }
}
