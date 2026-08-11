package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.VeiculoDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.VeiculoDTO.StatusVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class VeiculoService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public VeiculoService(ApiService apiService) {
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

    public void cadastrar(VeiculoDTO veiculo) {
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("A placa do veículo não pode estar vazia.");
        }

        try {
            restClient.post()
                    .uri("/veiculos/cadastrar")
                    .body(veiculo)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(extrairMensagemErro(e));
        }
    }

    public List<VeiculoDTO> listarTodos() {
        VeiculoDTO[] veiculos = restClient.get()
                .uri("/veiculos/listar")
                .retrieve()
                .body(VeiculoDTO[].class);

        return veiculos != null ? Arrays.asList(veiculos) : List.of();
    }

    public VeiculoDTO buscarPorId(Long id) {
        return restClient.get()
                .uri("/veiculos/{id}", id)
                .retrieve()
                .body(VeiculoDTO.class);
    }

    public void editarVeiculo(VeiculoDTO veiculo) {
        if (veiculo.getIdVeiculo() == null) {
            throw new IllegalArgumentException("ID do veículo inválido para atualização.");
        }

        try {
            restClient.put()
                    .uri("/veiculos/atualizar")
                    .body(veiculo)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(extrairMensagemErro(e));
        }
    }

    public void alterarStatus(Long idVeiculo, StatusVeiculo status) {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setIdVeiculo(idVeiculo);
        dto.setStatus(status);

        restClient.put()
                .uri("/veiculos/status")
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }
}