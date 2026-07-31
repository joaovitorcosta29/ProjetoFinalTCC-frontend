/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author joaov
 */

@Service
public class ManutencaoService {

    @Autowired
    private ApiService apiService;

    private String getEndpoint() {
        return apiService.getBaseUrl() + "/api/manutencoes";
    }

    public void salvar(ManutencaoDTO manutencao) {
        if (manutencao.getIdManutencao() != null && manutencao.getIdManutencao() > 0) {
            // Edição via PUT
            apiService.getRestTemplate().put(
                getEndpoint() + "/" + manutencao.getIdManutencao(), 
                manutencao
            );
        } else {
            // Cadastro via POST
            apiService.getRestTemplate().postForObject(
                getEndpoint(), 
                manutencao, 
                String.class
            );
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

    public void deletar(Integer id) {
        apiService.getRestTemplate().delete(getEndpoint() + "/" + id);
    }
}
