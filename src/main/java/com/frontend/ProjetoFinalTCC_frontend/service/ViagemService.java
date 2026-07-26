/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.FinalizarViagemDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.ViagemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author joaov
 */
@Service
public class ViagemService {

    @Autowired
    private ApiService apiService;

    public String registrar(ViagemDTO viagemDTO) {
        String url = apiService.getBaseUrl() + "/viagens/registrar";
        return apiService.getRestTemplate().postForObject(url, viagemDTO, String.class);
    }

    public List<ViagemDTO> listarTodas() {
        String url = apiService.getBaseUrl() + "/viagens/listar";
        ViagemDTO[] viagens = apiService.getRestTemplate().getForObject(url, ViagemDTO[].class);
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
            String url = apiService.getBaseUrl() + "/viagens/finalizar";
            FinalizarViagemDTO dto = new FinalizarViagemDTO(idViagem, kmFinal);
            
            apiService.getRestTemplate().postForEntity(url, dto, Void.class);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}