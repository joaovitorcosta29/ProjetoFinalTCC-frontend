package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO.StatusManutencao;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManutencaoService {

    @Autowired
    private ApiService apiService;

    private String getEndpoint() {
        return apiService.getBaseUrl() + "/api/manutencoes";
    }

    public String salvar(ManutencaoDTO manutencao) {
        try {
            if (manutencao.getIdManutencao() != null && manutencao.getIdManutencao() > 0) {
                apiService.getRestTemplate().put(
                        getEndpoint() + "/" + manutencao.getIdManutencao(),
                        manutencao
                );
                return "Manutenção salva com sucesso!";
            } else {
                manutencao.setStatusManutencao(StatusManutencao.PENDENTE);
                apiService.getRestTemplate().postForObject(
                        getEndpoint(),
                        manutencao,
                        String.class
                );
                return "Manutenção salva com sucesso!";
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com o backend: " + e.getMessage());
        }
    }

    public List<ManutencaoDTO> listarTodas() {
        ManutencaoDTO[] lista = apiService.getRestTemplate().getForObject(
                getEndpoint(),
                ManutencaoDTO[].class
        );
        return lista != null ? Arrays.asList(lista) : List.of();
    }

    public ManutencaoDTO buscarPorId(Integer id) {
        return apiService.getRestTemplate().getForObject(
                getEndpoint() + "/" + id,
                ManutencaoDTO.class
        );
    }

    public void alterarStatus(Integer idManutencao, StatusManutencao novoStatus) {
        ManutencaoDTO manutencao = buscarPorId(idManutencao);
        if (manutencao != null) {
            manutencao.setStatusManutencao(novoStatus);
            salvar(manutencao);
        }
    }

    public String deletar(Integer id) {
        try {
            apiService.getRestTemplate().delete(getEndpoint() + "/" + id);
            return "Manutenção excluída com sucesso!";
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar manutenção.");
        }
    }
}
