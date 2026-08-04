package com.frontend.ProjetoFinalTCC_frontend.controller;

import com.frontend.ProjetoFinalTCC_frontend.model.UsuarioDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.VeiculoDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.VeiculoDTO.StatusVeiculo;
import com.frontend.ProjetoFinalTCC_frontend.service.VeiculoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping("/listar")
    public String abrirTelaListagem(Model model, HttpSession session) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute("veiculos", veiculoService.listarTodos());
        model.addAttribute("statuses", StatusVeiculo.values());

        return "listar-veiculos";
    }

    @GetMapping("/cadastrar")
    public String abrirTelaCadastro(Model model, HttpSession session) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute("veiculo", new VeiculoDTO());
        model.addAttribute("statuses", StatusVeiculo.values());

        return "cadastrar-veiculos";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute("veiculo") VeiculoDTO veiculo, RedirectAttributes redirect) {
        try {
            veiculoService.cadastrar(veiculo);
            redirect.addFlashAttribute("mensagemSucesso", "Veículo cadastrado com sucesso!");
            return "redirect:/veiculos/listar";
        } catch (Exception e) {
            redirect.addFlashAttribute("mensagemErro", "Erro ao cadastrar veículo: " + e.getMessage());
            return "redirect:/veiculos/cadastrar";
        }
    }

    @PostMapping("/alterar-status")
    public String alterarStatus(@RequestParam("idVeiculo") Long idVeiculo,
                                @RequestParam("status") StatusVeiculo status,
                                RedirectAttributes redirect) {
        try {
            veiculoService.alterarStatus(idVeiculo, status);
            redirect.addFlashAttribute("mensagemSucesso", "Status atualizado com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensagemErro", "Erro ao atualizar status: " + e.getMessage());
        }

        return "redirect:/veiculos/listar";
    }
}