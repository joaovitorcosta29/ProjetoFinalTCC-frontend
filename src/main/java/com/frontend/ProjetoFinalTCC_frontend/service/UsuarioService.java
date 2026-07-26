package com.frontend.ProjetoFinalTCC_frontend.service;

import com.frontend.ProjetoFinalTCC_frontend.model.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private ApiService apiService;

    public void registrar(UsuarioDTO usuario) {
        usuario.setCargo(UsuarioDTO.Cargo.MOTORISTA);

        String url = apiService.getBaseUrl() + "/usuarios/registrar";
        
        ResponseEntity<String> response = apiService.getRestTemplate().postForEntity(url, usuario, String.class);
        
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao cadastrar no banco de dados.");
        }
    }

    public UsuarioDTO logar(UsuarioDTO loginDados) {
        String url = apiService.getBaseUrl() + "/usuarios/login";
        
        ResponseEntity<UsuarioDTO> response = apiService.getRestTemplate().postForEntity(url, loginDados, UsuarioDTO.class);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("E-mail ou senha inválidos.");
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