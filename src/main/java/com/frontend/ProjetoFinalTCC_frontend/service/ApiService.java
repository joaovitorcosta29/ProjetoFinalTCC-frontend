package com.frontend.ProjetoFinalTCC_frontend.service;

import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private static final String BASE_URL = "http://localhost:8081";

    public String getBaseUrl() {
        return BASE_URL;
    }
}