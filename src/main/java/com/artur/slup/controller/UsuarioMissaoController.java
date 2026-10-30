package com.artur.slup.controller;

import com.artur.slup.dao.IUsuarioMissaoDAO;
import com.artur.slup.dto.Missao;
import com.artur.slup.service.MissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/missoes")
public class UsuarioMissaoController {

    @Autowired
    private MissaoService missaoService;

    @Autowired
    private IUsuarioMissaoDAO usuarioMissaoDAO;

    @PostMapping("/{idMissao}/iniciar")
    public ResponseEntity<?> iniciarMissao(@PathVariable int idMissao, @RequestBody int idUsuario) {
        try {

            missaoService.iniciarMissao(idUsuario, idMissao);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Missão iniciada com sucesso");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{idMissao}/concluir")
    public ResponseEntity<?> concluirMissao(@PathVariable int idMissao, @RequestParam int idUsuario){
        try {
            missaoService.concluirMissao(idUsuario, idMissao);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Missão concluída com sucesso");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}/em-andamento")
    public ResponseEntity<List<Missao>> listarMissoesEmAndamento(@PathVariable int idUsuario) {
        List<Missao> missoes = usuarioMissaoDAO.listarMissoesEmAndamento(idUsuario);
        return ResponseEntity.ok(missoes);
    }

    @GetMapping("/usuario/{idUsuario}/concluidas")
    public ResponseEntity<List<Missao>> listarMissoesConcluidas(@PathVariable int idUsuario) {
        List<Missao> missoes = usuarioMissaoDAO.listarMissoesConcluidas(idUsuario);
        return ResponseEntity.ok(missoes);
    }
}
