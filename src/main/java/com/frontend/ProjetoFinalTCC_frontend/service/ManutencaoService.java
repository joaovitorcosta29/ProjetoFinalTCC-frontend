package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO.StatusManutencao;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ManutencaoService {

    private final RestClient restClient;

    @Autowired
    public ManutencaoService(ApiService apiService) {
        this.restClient = RestClient.builder()
                .baseUrl(apiService.getBaseUrl())
                .build();
    }

    public String salvar(ManutencaoDTO manutencao) {
        try {
            if (manutencao.getIdManutencao() != null && manutencao.getIdManutencao() > 0) {
                restClient.put()
                        .uri("/api/manutencoes/{id}", manutencao.getIdManutencao())
                        .body(manutencao)
                        .retrieve()
                        .toBodilessEntity();
            } else {
                manutencao.setStatusManutencao(StatusManutencao.PENDENTE);
                restClient.post()
                        .uri("/api/manutencoes")
                        .body(manutencao)
                        .retrieve()
                        .toBodilessEntity();
            }
            return "Manutenção salva com sucesso!";
        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com o backend: " + e.getMessage());
        }
    }

    public List<ManutencaoDTO> listarTodas() {
        ManutencaoDTO[] lista = restClient.get()
                .uri("/api/manutencoes")
                .retrieve()
                .body(ManutencaoDTO[].class);

        return lista != null ? Arrays.asList(lista) : List.of();
    }

    public ManutencaoDTO buscarPorId(Integer id) {
        return restClient.get()
                .uri("/api/manutencoes/{id}", id)
                .retrieve()
                .body(ManutencaoDTO.class);
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
            restClient.delete()
                    .uri("/api/manutencoes/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
            return "Manutenção excluída com sucesso!";
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar manutenção.");
        }
    }
}