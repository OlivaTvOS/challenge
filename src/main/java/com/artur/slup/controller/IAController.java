package com.artur.slup.controller;

import com.artur.slup.dto.ResultadoAgrupamentoIA;
import com.artur.slup.service.IAService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ia")
public class IAController {

    private final IAService iaService;

    public IAController(IAService iaService) {
        this.iaService = iaService;
    }

    @PostMapping("/clusters")
    public ResponseEntity<?> agruparUsuarios() {
        try {
            ResultadoAgrupamentoIA resultado = iaService.agruparUsuarios();
            return ResponseEntity.ok(resultado);
        } catch (IllegalStateException erro) {
            Map<String, String> resposta = new HashMap<>();
            resposta.put("erro", erro.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(resposta);
        } catch (RuntimeException erro) {
            Map<String, String> resposta = new HashMap<>();
            resposta.put("erro", "Não foi possível agrupar os usuários.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
        }
    }
}
