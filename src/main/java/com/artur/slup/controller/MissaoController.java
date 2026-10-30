package com.artur.slup.controller;

import com.artur.slup.dao.IMissaoDAO;
import com.artur.slup.dto.Missao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/missoes")
public class MissaoController {

    private final IMissaoDAO missaoDAO;

    public MissaoController(IMissaoDAO missaoDAO) {
        this.missaoDAO = missaoDAO;
    }

    @GetMapping
    public ResponseEntity<List<Missao>> listarMissoesAtivas() {
        return ResponseEntity.ok(missaoDAO.listarMissoes());
    }
}
