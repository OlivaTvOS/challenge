package com.artur.slup.service;

import com.artur.slup.dao.IInteligenciaArtificialDAO;
import com.artur.slup.dto.CaracteristicasIA;
import com.artur.slup.dto.ClassificacaoIA;
import com.artur.slup.dto.GrupoIA;
import com.artur.slup.dto.ResultadoAgrupamentoIA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InteligenciaArtificialService {

    private final IInteligenciaArtificialDAO iaDAO;
    private final RestClient restClient;
    private final String versaoModelo;

    public InteligenciaArtificialService(
            IInteligenciaArtificialDAO iaDAO,
            @Value("${soulup.ia.url:http://localhost:5000}") String urlIA,
            @Value("${soulup.ia.versao:1.0}") String versaoModelo) {
        this.iaDAO = iaDAO;
        this.restClient = RestClient.builder().baseUrl(urlIA).build();
        this.versaoModelo = versaoModelo;
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

        Map<?, ?> resposta = restClient.post()
                .uri("/predict/clusters")
                .contentType(MediaType.APPLICATION_JSON)
                .body(corpo)
                .retrieve()
                .body(Map.class);

        if (resposta == null || !(resposta.get("grupos") instanceof List<?> gruposApi)) {
            throw new IllegalStateException("A API de IA retornou uma resposta inválida.");
        }

        List<ClassificacaoIA> classificacoes = new ArrayList<>();
        List<GrupoIA> grupos = new ArrayList<>();
        for (Object item : gruposApi) {
            if (!(item instanceof Map<?, ?> grupo)) {
                continue;
            }
            int clusterId = ((Number) grupo.get("cluster_id")).intValue();
            String perfil = String.valueOf(grupo.get("perfil_recomendado"));
            int quantidade = ((Number) grupo.get("quantidade_usuarios")).intValue();
            List<Integer> idsGrupo = new ArrayList<>();
            Object ids = grupo.get("usuarios");
            if (ids instanceof List<?> listaIds) {
                for (Object id : listaIds) {
                    int idUsuario = ((Number) id).intValue();
                    idsGrupo.add(idUsuario);
                    CaracteristicasIA caracteristicas = localizarUsuario(usuarios, idUsuario);
                    if (caracteristicas != null) {
                        classificacoes.add(new ClassificacaoIA(idUsuario, clusterId, perfil, caracteristicas));
                    }
                }
            }
            grupos.add(new GrupoIA(clusterId, perfil, quantidade, idsGrupo));
        }

        iaDAO.salvarClassificacoes(classificacoes, versaoModelo);
        return new ResultadoAgrupamentoIA(usuarios.size(), grupos, "KMeans", versaoModelo);
    }

    private CaracteristicasIA localizarUsuario(List<CaracteristicasIA> usuarios, int idUsuario) {
        for (CaracteristicasIA usuario : usuarios) {
            if (usuario.getIdUsuario() == idUsuario) {
                return usuario;
            }
        }
        return null;
    }
}
