package com.frontend.ProjetoFinalTCC_frontend.controller;

import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.ManutencaoDTO.StatusManutencao;
import com.frontend.ProjetoFinalTCC_frontend.service.ManutencaoService;
import com.frontend.ProjetoFinalTCC_frontend.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/manutencoes")
public class ManutencaoController {

    @Autowired
    private ManutencaoService manutencaoService;

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping("/listar")
    public String listarManutencoes(Model model) {
        model.addAttribute("manutencoes", manutencaoService.listarTodas());
        model.addAttribute("statusOptions", StatusManutencao.values()); // Alimenta o <select>
        return "listar-manutencoes";
    }

    @GetMapping("/cadastrar")
    public String cadastrarManutencao(Model model) {
        model.addAttribute("manutencao", new ManutencaoDTO());
        model.addAttribute("veiculosDisponiveis", veiculoService.listarTodos().stream()
                .filter(v -> "DISPONIVEL".equalsIgnoreCase(v.getStatus().name()))
                .toList());
        model.addAttribute("statusOptions", StatusManutencao.values());
        return "cadastrar-manutencoes";
    }

    @PostMapping("/salvar")
    public String salvarManutencao(@ModelAttribute("manutencao") ManutencaoDTO manutencao, RedirectAttributes redirectAttributes) {
        try {
            manutencaoService.salvar(manutencao);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Manutenção salva com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar manutenção: " + e.getMessage());
        }
        return "redirect:/manutencoes/listar";
    }

    @PostMapping("/alterar-status")
    public String alterarStatus(@RequestParam("idManutencao") Integer idManutencao,
                                @RequestParam("novoStatus") StatusManutencao novoStatus,
                                RedirectAttributes redirectAttributes) {
        try {
            manutencaoService.alterarStatus(idManutencao, novoStatus);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Status alterado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao alterar status: " + e.getMessage());
        }
        return "redirect:/manutencoes/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarManutencao(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ManutencaoDTO manutencao = manutencaoService.buscarPorId(id);

            if (manutencao == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Manutenção não encontrada.");
                return "redirect:/manutencoes/listar";
            }

            if (manutencao.getStatusManutencao() != null && manutencao.getStatusManutencao() != StatusManutencao.PENDENTE) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Só é possível editar manutenções que ainda estão pendentes.");
                return "redirect:/manutencoes/listar";
            }

            model.addAttribute("manutencao", manutencao);
            model.addAttribute("veiculosDisponiveis", veiculoService.listarTodos().stream()
                    .filter(v -> "DISPONIVEL".equalsIgnoreCase(v.getStatus().name()) || v.getIdVeiculo().equals(manutencao.getIdVeiculo()))
                    .toList());
            model.addAttribute("statusOptions", StatusManutencao.values());
            return "editar-manutencoes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao carregar manutenção para edição.");
            return "redirect:/manutencoes/listar";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarManutencao(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            manutencaoService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Manutenção excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao deletar manutenção.");
        }
        return "redirect:/manutencoes/listar";
    }
}