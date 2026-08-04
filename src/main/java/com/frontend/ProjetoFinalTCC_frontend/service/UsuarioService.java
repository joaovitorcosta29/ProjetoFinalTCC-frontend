package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UsuarioService {

    private final RestClient restClient;

    @Autowired
    public UsuarioService(ApiService apiService) {
        this.restClient = RestClient.builder()
                .baseUrl(apiService.getBaseUrl())
                .build();
    }

    public void registrar(UsuarioDTO usuario) {
        usuario.setCargo(UsuarioDTO.Cargo.MOTORISTA);

        restClient.post()
                .uri("/usuarios/registrar")
                .body(usuario)
                .retrieve()
                .toBodilessEntity();
    }

    public UsuarioDTO logar(UsuarioDTO loginDados) {
        try {
            return restClient.post()
                    .uri("/usuarios/login")
                    .body(loginDados)
                    .retrieve()
                    .body(UsuarioDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("E-mail ou senha inválidos.", e);
        }
    }

    public UsuarioDTO autenticar(String email, String senha) {
        try {
            UsuarioDTO loginDados = new UsuarioDTO();
            loginDados.setEmail(email);
            loginDados.setSenha(senha);
            return logar(loginDados);
        } catch (Exception e) {
            return null;
        }
    }
}