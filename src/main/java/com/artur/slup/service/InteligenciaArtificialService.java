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
            @Value("${soulup.ia.url:${IA_API_URL:http://localhost:5000}}") String urlIA,
            @Value("${soulup.ia.versao:1.0}") String versaoModelo) {
        this.iaDAO = iaDAO;
        this.restClient = RestClient.builder().baseUrl(urlIA).build();
        this.versaoModelo = versaoModelo;
    }

    public Map<String, Object> verificarIntegracao() {
        Map<String, Object> status = new HashMap<>();
        status.put("api_ia_configurada", true);

        try {
            Map<?, ?> resposta = restClient.get().uri("/").retrieve().body(Map.class);
            status.put("api_ia_disponivel", resposta != null);
            status.put("modelos_carregados", resposta != null
                    && Boolean.TRUE.equals(resposta.get("modelos_carregados")));
            status.put("modelos", resposta == null ? Map.of() : resposta);
        } catch (RuntimeException erro) {
            status.put("api_ia_disponivel", false);
            status.put("erro_api_ia", "Nao foi possivel consultar a API de IA.");
        }

        try {
            status.put("tabela_historico_disponivel", iaDAO.verificarTabelaHistorico());
        } catch (RuntimeException erro) {
            status.put("tabela_historico_disponivel", false);
            status.put("erro_banco", "Nao foi possivel verificar a tabela de historico.");
        }

        return status;
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

    public Map<String, Object> executarModelosIndividuais() {
        List<CaracteristicasIA> usuarios = iaDAO.listarCaracteristicasUsuarios();
        List<ClassificacaoIA> classificacoes = new ArrayList<>();
        List<ClassificacaoIA> estimativas = new ArrayList<>();

        for (CaracteristicasIA usuario : usuarios) {
            Map<String, Object> dadosPerfil = montarEntrada(usuario, false);
            Map<?, ?> respostaPerfil = chamarModelo("/predict/perfil", dadosPerfil);
            String perfil = String.valueOf(respostaPerfil.get("perfil_previsto"));
            Object probabilidadesObjeto = respostaPerfil.get("probabilidades");
            double confianca = 0;
            if (probabilidadesObjeto instanceof Map<?, ?> probabilidades) {
                for (Object valor : probabilidades.values()) {
                    if (valor instanceof Number numero && numero.doubleValue() > confianca) {
                        confianca = numero.doubleValue();
                    }
                }
            }

            ClassificacaoIA classificacao = new ClassificacaoIA();
            classificacao.setIdUsuario(usuario.getIdUsuario());
            classificacao.setClusterId(-1);
            classificacao.setPerfilPrevisto(perfil);
            classificacao.setConfianca(confianca);
            classificacao.setCaracteristicas(usuario);
            classificacoes.add(classificacao);

            Map<?, ?> respostaPontos = chamarModelo("/predict/pontos", montarEntrada(usuario, true));
            Object pontosObjeto = respostaPontos.get("pontos_estimados");
            if (!(pontosObjeto instanceof Number pontos)) {
                throw new IllegalStateException("A API de IA retornou uma estimativa de pontos inválida.");
            }

            ClassificacaoIA estimativa = new ClassificacaoIA();
            estimativa.setIdUsuario(usuario.getIdUsuario());
            estimativa.setClusterId(-1);
            estimativa.setPontosEstimados(pontos.doubleValue());
            estimativa.setCaracteristicas(usuario);
            estimativas.add(estimativa);
        }

        iaDAO.salvarResultados(classificacoes, "RANDOM_FOREST", versaoModelo);
        iaDAO.salvarResultados(estimativas, "LINEAR_REGRESSION", versaoModelo);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("total_usuarios", usuarios.size());
        resultado.put("classificacoes_random_forest", classificacoes.size());
        resultado.put("estimativas_regressao", estimativas.size());
        resultado.put("modelo_classificacao", "RandomForestClassifier");
        resultado.put("modelo_regressao", "LinearRegression");
        resultado.put("aviso_regressao", "Estimativas experimentais; não alteram a pontuação oficial.");
        resultado.put("versao_modelo", versaoModelo);
        return resultado;
    }

    private Map<String, Object> montarEntrada(CaracteristicasIA usuario, boolean incluirTaxa) {
        Map<String, Object> entrada = new HashMap<>();
        entrada.put("missoes_reciclagem", usuario.getMissoesReciclagem());
        entrada.put("missoes_mobilidade", usuario.getMissoesMobilidade());
        entrada.put("missoes_agua", usuario.getMissoesAgua());
        entrada.put("missoes_energia", usuario.getMissoesEnergia());
        entrada.put("missoes_natureza", usuario.getMissoesNatureza());
        entrada.put("missoes_consumo", usuario.getMissoesConsumo());
        if (incluirTaxa) {
            entrada.put("taxa_conclusao", usuario.getTaxaConclusao());
        }
        return entrada;
    }

    private Map<?, ?> chamarModelo(String caminho, Map<String, Object> entrada) {
        Map<?, ?> resposta = restClient.post()
                .uri(caminho)
                .contentType(MediaType.APPLICATION_JSON)
                .body(entrada)
                .retrieve()
                .body(Map.class);
        if (resposta == null) {
            throw new IllegalStateException("A API de IA não retornou dados para " + caminho + ".");
        }
        return resposta;
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
