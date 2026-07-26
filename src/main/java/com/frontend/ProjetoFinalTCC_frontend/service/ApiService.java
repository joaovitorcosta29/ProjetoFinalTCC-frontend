package com.frontend.ProjetoFinalTCC_frontend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final RestTemplate restTemplate;
    
    private final String BASE_URL = "http://localhost:8080"; 

    public ApiService() {
        this.restTemplate = new RestTemplate();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }
}