package com.artur.slup.service;

import com.artur.slup.dao.IADAO;
import com.artur.slup.dto.CaracteristicasIA;
import com.artur.slup.dto.ClassificacaoIA;
import com.artur.slup.dto.GrupoIA;
import com.artur.slup.dto.ResultadoAgrupamentoIA;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IAService {

    private final IADAO iaDAO;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String urlApiIA;
    private final String versaoModelo;

    public IAService(IADAO iaDAO, ObjectMapper objectMapper,
                     @Value("${IA_API_URL:http://127.0.0.1:5000}") String urlApiIA,
                     @Value("${IA_MODEL_VERSION:1.0}") String versaoModelo) {
        this.iaDAO = iaDAO;
        this.objectMapper = objectMapper;
        this.urlApiIA = urlApiIA.replaceAll("/+$", "");
        this.versaoModelo = versaoModelo;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public ResultadoAgrupamentoIA agruparUsuarios() {
        List<CaracteristicasIA> usuarios = iaDAO.listarCaracteristicasUsuarios();
        if (usuarios.isEmpty()) {
            return new ResultadoAgrupamentoIA(0, new ArrayList<>(), "KMeans", versaoModelo);
        }

        List<Map<String, Object>> entradas = new ArrayList<>();
        for (CaracteristicasIA usuario : usuarios) {
            Map<String, Object> entrada = new HashMap<>();
            entrada.put("id_usuario", usuario.getIdUsuario());
            entrada.put("missoes_reciclagem", usuario.getMissoesReciclagem());
            entrada.put("missoes_mobilidade", usuario.getMissoesMobilidade());
            entrada.put("missoes_agua", usuario.getMissoesAgua());
            entrada.put("missoes_energia", usuario.getMissoesEnergia());
            entrada.put("missoes_natureza", usuario.getMissoesNatureza());
            entrada.put("missoes_consumo", usuario.getMissoesConsumo());
            entrada.put("taxa_conclusao", usuario.getTaxaConclusao());
            entradas.add(entrada);
        }

        Map<String, Object> corpo = new HashMap<>();
        corpo.put("usuarios", entradas);
        JsonNode resposta = chamarFlask("/predict/clusters", corpo);

        List<GrupoIA> grupos = new ArrayList<>();
        List<ClassificacaoIA> historico = new ArrayList<>();
        JsonNode listaGrupos = resposta.path("grupos");
        for (JsonNode grupoJson : listaGrupos) {
            List<Integer> ids = new ArrayList<>();
            for (JsonNode id : grupoJson.path("usuarios")) {
                ids.add(id.asInt());
            }

            int clusterId = grupoJson.path("cluster_id").asInt();
            String perfil = grupoJson.path("perfil_recomendado").asText();
            grupos.add(new GrupoIA(clusterId, perfil,
                    grupoJson.path("quantidade_usuarios").asInt(), ids));

            for (Integer idUsuario : ids) {
                CaracteristicasIA dados = encontrarUsuario(usuarios, idUsuario);
                if (dados != null) {
                    historico.add(new ClassificacaoIA(idUsuario, clusterId, perfil, dados));
                }
            }
        }

        iaDAO.salvarClassificacoes(historico, versaoModelo);
        return new ResultadoAgrupamentoIA(
                resposta.path("total_usuarios").asInt(),
                grupos,
                resposta.path("modelo").asText("KMeans"),
                versaoModelo);
    }

    private CaracteristicasIA encontrarUsuario(List<CaracteristicasIA> usuarios, int idUsuario) {
        for (CaracteristicasIA usuario : usuarios) {
            if (usuario.getIdUsuario() == idUsuario) {
                return usuario;
            }
        }
        return null;
    }

    private JsonNode chamarFlask(String caminho, Map<String, Object> corpo) {
        try {
            String json = objectMapper.writeValueAsString(corpo);
            HttpRequest requisicao = HttpRequest.newBuilder()
                    .uri(URI.create(urlApiIA + caminho))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> resposta = httpClient.send(
                    requisicao, HttpResponse.BodyHandlers.ofString());
            if (resposta.statusCode() < 200 || resposta.statusCode() >= 300) {
                throw new IllegalStateException("A API de IA retornou HTTP "
                        + resposta.statusCode() + ": " + resposta.body());
            }
            return objectMapper.readTree(resposta.body());
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("A chamada à API de IA foi interrompida.", erro);
        } catch (Exception erro) {
            throw new IllegalStateException("Não foi possível comunicar com a API Flask.", erro);
        }
    }
}
