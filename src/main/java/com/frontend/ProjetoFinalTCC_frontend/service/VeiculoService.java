package com.frontend.ProjetoFinalTCC_frontend.service;

import com.projetofinalTCC.backendTCC.model.VeiculoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    private ApiService apiService;

    public void cadastrar(VeiculoDTO veiculo) {
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("A placa do veículo não pode estar vazia.");
        }

        String url = apiService.getBaseUrl() + "/veiculos/cadastrar";
        apiService.getRestTemplate().postForObject(url, veiculo, String.class);
    }

    public List<VeiculoDTO> listarTodos() {
        String url = apiService.getBaseUrl() + "/veiculos/listar";
        VeiculoDTO[] veiculos = apiService.getRestTemplate().getForObject(url, VeiculoDTO[].class);
        
        return Arrays.asList(veiculos != null ? veiculos : new VeiculoDTO[0]);
    }
}