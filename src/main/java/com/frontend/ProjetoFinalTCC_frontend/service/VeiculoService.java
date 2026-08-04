package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.VeiculoDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.VeiculoDTO.StatusVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class VeiculoService {

    private final RestClient restClient;

    @Autowired
    public VeiculoService(ApiService apiService) {
        this.restClient = RestClient.builder()
                .baseUrl(apiService.getBaseUrl())
                .build();
    }

    public void cadastrar(VeiculoDTO veiculo) {
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("A placa do veículo não pode estar vazia.");
        }

        restClient.post()
                .uri("/veiculos/cadastrar")
                .body(veiculo)
                .retrieve()
                .toBodilessEntity();
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

        restClient.put()
                .uri("/veiculos/atualizar")
                .body(veiculo)
                .retrieve()
                .toBodilessEntity();
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