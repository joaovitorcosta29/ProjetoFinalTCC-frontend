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
        model.addAttribute("alertaOptions", VeiculoDTO.AlertaManutencao.values());

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

    @GetMapping("/editar/{id}")
    public String editarVeiculo(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            VeiculoDTO veiculo = veiculoService.buscarPorId(id);

            if (veiculo == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Veículo não encontrado.");
                return "redirect:/veiculos/listar";
            }

            model.addAttribute("veiculo", veiculo);
            model.addAttribute("statusOptions", StatusVeiculo.values());
            model.addAttribute("alertaOptions", VeiculoDTO.AlertaManutencao.values());

            return "editar-veiculos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao carregar veículo para edição: " + e.getMessage());
            return "redirect:/veiculos/listar";
        }
    }

    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute("veiculo") VeiculoDTO veiculo, RedirectAttributes redirect) {
        try {
            veiculoService.editarVeiculo(veiculo);
            redirect.addFlashAttribute("mensagemSucesso", "Veículo atualizado com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensagemErro", "Erro ao atualizar veículo: " + e.getMessage());
        }

        return "redirect:/veiculos/listar";
    }
}