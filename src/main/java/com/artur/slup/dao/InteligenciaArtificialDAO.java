package com.artur.slup.dao;

import com.artur.slup.dto.CaracteristicasIA;
import com.artur.slup.dto.ClassificacaoIA;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InteligenciaArtificialDAO implements IInteligenciaArtificialDAO {

    private final DataSource dataSource;

    public IADAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<CaracteristicasIA> listarCaracteristicasUsuarios() {
        List<CaracteristicasIA> usuarios = new ArrayList<>();
        String sql = "SELECT U.ID_USUARIO, "
                + "SUM(CASE WHEN C.ID_CATEGORIA = 1 THEN 1 ELSE 0 END) AS MISSOES_RECICLAGEM, "
                + "SUM(CASE WHEN C.ID_CATEGORIA = 2 THEN 1 ELSE 0 END) AS MISSOES_MOBILIDADE, "
                + "SUM(CASE WHEN C.ID_CATEGORIA = 3 THEN 1 ELSE 0 END) AS MISSOES_AGUA, "
                + "SUM(CASE WHEN C.ID_CATEGORIA = 4 THEN 1 ELSE 0 END) AS MISSOES_ENERGIA, "
                + "SUM(CASE WHEN C.ID_CATEGORIA = 5 THEN 1 ELSE 0 END) AS MISSOES_NATUREZA, "
                + "SUM(CASE WHEN C.ID_CATEGORIA = 6 THEN 1 ELSE 0 END) AS MISSOES_CONSUMO, "
                + "CASE WHEN COUNT(UM.ID_USUARIO_MISSAO) = 0 THEN 0 "
                + "ELSE SUM(CASE WHEN UM.STATUS = 'CONCLUIDA' THEN 1 ELSE 0 END) "
                + "/ COUNT(UM.ID_USUARIO_MISSAO) END AS TAXA_CONCLUSAO "
                + "FROM USUARIO U "
                + "LEFT JOIN USUARIO_MISSAO UM ON UM.ID_USUARIO = U.ID_USUARIO "
                + "AND UM.STATUS IN ('EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA') "
                + "LEFT JOIN MISSAO M ON M.ID_MISSAO = UM.ID_MISSAO "
                + "LEFT JOIN CATEGORIA C ON C.ID_CATEGORIA = M.ID_CATEGORIA "
                + "WHERE U.TIPO_USUARIO = 'USUARIO' "
                + "GROUP BY U.ID_USUARIO ORDER BY U.ID_USUARIO";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                CaracteristicasIA usuario = new CaracteristicasIA();
                usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
                usuario.setMissoesReciclagem(rs.getInt("MISSOES_RECICLAGEM"));
                usuario.setMissoesMobilidade(rs.getInt("MISSOES_MOBILIDADE"));
                usuario.setMissoesAgua(rs.getInt("MISSOES_AGUA"));
                usuario.setMissoesEnergia(rs.getInt("MISSOES_ENERGIA"));
                usuario.setMissoesNatureza(rs.getInt("MISSOES_NATUREZA"));
                usuario.setMissoesConsumo(rs.getInt("MISSOES_CONSUMO"));
                usuario.setTaxaConclusao(rs.getDouble("TAXA_CONCLUSAO"));
                usuarios.add(usuario);
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao consultar caracteristicas dos usuarios para a IA.", erro);
        }
        return usuarios;
    }

    @Override
    public void salvarClassificacoes(List<ClassificacaoIA> classificacoes, String versaoModelo) {
        if (classificacoes == null || classificacoes.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO IA_CLASSIFICACAO "
                + "(ID_USUARIO, MODELO, PERFIL_PREVISTO, CLUSTER_ID, "
                + "MISSOES_RECICLAGEM, MISSOES_MOBILIDADE, MISSOES_AGUA, "
                + "MISSOES_ENERGIA, MISSOES_NATUREZA, MISSOES_CONSUMO, "
                + "TAXA_CONCLUSAO, VERSAO_MODELO) "
                + "VALUES (?, 'KMEANS', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            try {
                for (ClassificacaoIA classificacao : classificacoes) {
                    CaracteristicasIA dados = classificacao.getCaracteristicas();
                    statement.setInt(1, classificacao.getIdUsuario());
                    statement.setString(2, classificacao.getPerfilPrevisto());
                    statement.setInt(3, classificacao.getClusterId());
                    statement.setInt(4, dados.getMissoesReciclagem());
                    statement.setInt(5, dados.getMissoesMobilidade());
                    statement.setInt(6, dados.getMissoesAgua());
                    statement.setInt(7, dados.getMissoesEnergia());
                    statement.setInt(8, dados.getMissoesNatureza());
                    statement.setInt(9, dados.getMissoesConsumo());
                    statement.setDouble(10, dados.getTaxaConclusao());
                    statement.setString(11, versaoModelo);
                    statement.addBatch();
                }
                statement.executeBatch();
                connection.commit();
            } catch (SQLException | RuntimeException erro) {
                connection.rollback();
                throw erro;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao salvar classificacoes da IA.", erro);
        }
    }
}
