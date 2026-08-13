package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO.StatusManutencao;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode; 
import tools.jackson.databind.ObjectMapper;

@Service
public class ManutencaoService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ManutencaoService(ApiService apiService) {
        this.restClient = RestClient.builder()
                .baseUrl(apiService.getBaseUrl())
                .build();
    }

    private String extrairMensagemErro(HttpStatusCodeException e) {
        String corpo = e.getResponseBodyAsString();

        if (corpo != null && !corpo.isBlank()) {
            try {
                JsonNode node = objectMapper.readTree(corpo);
                if (node.has("message") && !node.get("message").asText().isBlank()) {
                    return node.get("message").asText();
                }
            } catch (Exception ignored) {
                return corpo;
            }
            return corpo;
        }

        return e.getMessage();
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
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(extrairMensagemErro(e));
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
        try {
            restClient.patch()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/manutencoes/{id}/status")
                            .queryParam("novoStatus", novoStatus)
                            .build(idManutencao))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(extrairMensagemErro(e));
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