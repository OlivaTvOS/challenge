package com.artur.slup.dao;

import com.artur.slup.dto.CaracteristicasIA;
import com.artur.slup.dto.ClassificacaoIA;

import java.util.List;

public interface IInteligenciaArtificialDAO {
    List<CaracteristicasIA> listarCaracteristicasUsuarios();
    void salvarClassificacoes(List<ClassificacaoIA> classificacoes, String versaoModelo);
    void salvarResultados(List<ClassificacaoIA> resultados, String modelo, String versaoModelo);
}
