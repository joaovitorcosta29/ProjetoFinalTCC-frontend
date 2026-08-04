package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.FinalizarViagemDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.ViagemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.RestClient;

@Service
public class ViagemService {

    private final RestClient restClient;

    @Autowired
    public ViagemService(ApiService apiService) {
        this.restClient = RestClient.builder()
                .baseUrl(apiService.getBaseUrl())
                .build();
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

    public boolean finalizarViagem(Long idViagem, Double kmFinal) {
        try {
            FinalizarViagemDTO dto = new FinalizarViagemDTO(idViagem, kmFinal);

            restClient.post()
                    .uri("/viagens/finalizar")
                    .body(dto)
                    .retrieve()
                    .toBodilessEntity();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}