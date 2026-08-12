package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.FinalizarViagemDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.ViagemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class ViagemService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ViagemService(ApiService apiService) {
        this.restClient = RestClient.builder()
                .baseUrl(apiService.getBaseUrl())
                .build();
    }

    // Extrai a mensagem de erro real enviada pelo backend. Alguns endpoints devolvem
    // um JSON padrão do Spring (campo "message"), outros devolvem o corpo como texto puro.
    private String extrairMensagemErro(HttpStatusCodeException e) {
        String corpo = e.getResponseBodyAsString();

        if (corpo != null && !corpo.isBlank()) {
            try {
                JsonNode node = objectMapper.readTree(corpo);
                if (node.has("message") && !node.get("message").asText().isBlank()) {
                    return node.get("message").asText();
                }
            } catch (Exception ignored) {
                // corpo não é JSON -> é texto puro, usa como está
                return corpo;
            }
            return corpo;
        }

        return e.getMessage();
    }

    public String registrar(ViagemDTO viagemDTO) {
        return restClient.post()
                .uri("/viagens/registrar")
                .body(viagemDTO)
                .retrieve()
                .body(String.class);
    }

    public List<ViagemDTO> listarTodas() {
        ViagemDTO[] viagens = restClient.get()
                .uri("/viagens/listar")
                .retrieve()
                .body(ViagemDTO[].class);

        return viagens != null ? Arrays.asList(viagens) : List.of();
    }

    public ViagemDTO buscarPorId(Long id) {
        try {
            List<ViagemDTO> todas = listarTodas();
            if (todas != null) {
                return todas.stream()
                        .filter(v -> v.getIdViagem() != null && v.getIdViagem().equals(id))
                        .findFirst()
                        .orElse(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editarViagem(ViagemDTO viagem) {
        if (viagem.getIdViagem() == null) {
            throw new IllegalArgumentException("ID da viagem inválido para atualização.");
        }

        try {
            restClient.put()
                    .uri("/viagens/atualizar")
                    .body(viagem)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(extrairMensagemErro(e));
        }
    }

    public void assumirViagem(Long idViagem, Integer idUsuario) {
        if (idViagem == null || idUsuario == null) {
            throw new IllegalArgumentException("Viagem ou usuário inválido para assumir a viagem.");
        }

        try {
            restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/viagens/assumir")
                            .queryParam("idViagem", idViagem)
                            .queryParam("idUsuario", idUsuario)
                            .build())
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(extrairMensagemErro(e));
        }
    }

    public void cancelarViagem(Long idViagem, Integer idUsuario) {
        if (idViagem == null || idUsuario == null) {
            throw new IllegalArgumentException("Viagem ou usuário inválido para cancelar a viagem.");
        }

        try {
            restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/viagens/cancelar")
                            .queryParam("idViagem", idViagem)
                            .queryParam("idUsuario", idUsuario)
                            .build())
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(extrairMensagemErro(e));
        }
    }

    public void finalizarViagem(Long idViagem, Double kmFinal) {
        try {
            FinalizarViagemDTO dto = new FinalizarViagemDTO(idViagem, kmFinal);

            restClient.post()
                    .uri("/viagens/finalizar")
                    .body(dto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(extrairMensagemErro(e));
        }
    }
}