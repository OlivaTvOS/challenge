package com.artur.slup.controller;

import com.artur.slup.dto.ResultadoAgrupamentoIA;
import com.artur.slup.service.InteligenciaArtificialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ia")
public class InteligenciaArtificialController {

    private final InteligenciaArtificialService inteligenciaArtificialService;

    public InteligenciaArtificialController(InteligenciaArtificialService inteligenciaArtificialService) {
        this.inteligenciaArtificialService = inteligenciaArtificialService;
    }

    @PostMapping("/modelos")
    public ResponseEntity<?> executarModelosIndividuais() {
        try {
            return ResponseEntity.ok(inteligenciaArtificialService.executarModelosIndividuais());
        } catch (org.springframework.web.client.RestClientException erro) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("erro", "Nao foi possivel comunicar com a API de IA."));
        } catch (IllegalStateException erro) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("erro", erro.getMessage()));
        } catch (RuntimeException erro) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Nao foi possivel executar os modelos de IA."));
        }
    }

    @PostMapping("/clusters")
    public ResponseEntity<?> agruparUsuarios() {
        try {
            ResultadoAgrupamentoIA resultado = inteligenciaArtificialService.agruparUsuarios();
            return ResponseEntity.ok(resultado);
        } catch (org.springframework.web.client.RestClientException erro) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("erro", "Nao foi possivel comunicar com a API de IA."));
        } catch (IllegalStateException erro) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("erro", erro.getMessage()));
        }
    }
}
