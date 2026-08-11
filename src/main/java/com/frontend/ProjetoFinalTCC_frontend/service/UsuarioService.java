package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class UsuarioService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public UsuarioService(ApiService apiService) {
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

    public void registrar(UsuarioDTO usuario) {
        try {
            restClient.post()
                    .uri("/usuarios/registrar")
                    .body(usuario)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(extrairMensagemErro(e));
        }
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