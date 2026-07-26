package com.frontend.ProjetoFinalTCC_frontend.controller;

import com.frontend.ProjetoFinalTCC_frontend.service.VeiculoService;
import com.projetofinalTCC.backendTCC.model.VeiculoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping("/tela-cadastro")
    public String abrirTelaCadastro() {
        return "veiculos";
    }

    @PostMapping("/cadastrar")
    @ResponseBody
    public ResponseEntity<String> cadastrar(@RequestBody VeiculoDTO veiculo) {
        try {
            veiculoService.cadastrar(veiculo);
            return ResponseEntity.ok("Veículo cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    @ResponseBody
    public ResponseEntity<List<VeiculoDTO>> listar() {
        List<VeiculoDTO> veiculos = veiculoService.listarTodos();
        return ResponseEntity.ok(veiculos);
    }
}