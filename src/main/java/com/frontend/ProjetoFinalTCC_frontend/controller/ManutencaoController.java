/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.ProjetoFinalTCC_frontend.controller;

/**
 *
 * @author joaov
 */

import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO.StatusManutencao;
import com.frontend.ProjetoFinalTCC_frontend.service.ManutencaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/manutencoes")
public class ManutencaoController {

    @Autowired
    private ManutencaoService manutencaoService;

    @GetMapping("/listar")
    public String listarManutencoes(Model model) {
        model.addAttribute("manutencoes", manutencaoService.listarTodas());
        if (!model.containsAttribute("manutencao")) {
            model.addAttribute("manutencao", new ManutencaoDTO());
        }
        model.addAttribute("statusOptions", StatusManutencao.values());
        return "listar-manutencoes";
    }

    @PostMapping("/salvar")
    public String salvarManutencao(@ModelAttribute("manutencao") ManutencaoDTO manutencao, RedirectAttributes redirectAttributes) {
        try {
            manutencaoService.salvar(manutencao);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Manutenção salva com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao comunicar com o backend: " + e.getMessage());
        }
        return "redirect:/manutencoes";
    }

    @GetMapping("/editar/{id}")
    public String editarManutencao(@PathVariable Integer id, Model model) {
        try {
            ManutencaoDTO manutencao = manutencaoService.buscarPorId(id);
            model.addAttribute("manutencao", manutencao);
            model.addAttribute("manutencoes", manutencaoService.listarTodas());
            model.addAttribute("statusOptions", StatusManutencao.values());
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao carregar manutenção para edição.");
        }
        return "manutencoes";
    }

    @GetMapping("/deletar/{id}")
    public String deletarManutencao(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            manutencaoService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Manutenção excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao deletar manutenção.");
        }
        return "redirect:/manutencoes";
    }
}
