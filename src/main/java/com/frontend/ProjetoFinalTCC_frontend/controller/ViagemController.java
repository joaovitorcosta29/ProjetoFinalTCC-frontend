package com.frontend.ProjetoFinalTCC_frontend.controller;

import com.frontend.ProjetoFinalTCC_frontend.model.UsuarioDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.VeiculoDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.VeiculoDTO.StatusVeiculo; // Import do Enum
import com.frontend.ProjetoFinalTCC_frontend.model.ViagemDTO;
import com.frontend.ProjetoFinalTCC_frontend.service.VeiculoService;
import com.frontend.ProjetoFinalTCC_frontend.service.ViagemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/viagens")
public class ViagemController {

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastro(Model model, HttpSession session) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (usuarioLogado.getCargo() != UsuarioDTO.Cargo.ADMIN
                && usuarioLogado.getCargo() != UsuarioDTO.Cargo.GESTOR_FROTA) {
            return "redirect:/";
        }

        // CORREÇÃO AQUI: Comparação direta com o Enum StatusVeiculo.DISPONIVEL
        List<VeiculoDTO> veiculosDisponiveis = veiculoService.listarTodos().stream()
                .filter(v -> v.getStatus() == StatusVeiculo.DISPONIVEL)
                .toList();

        model.addAttribute("viagem", new ViagemDTO());
        model.addAttribute("estados", ViagemDTO.Estado.values());
        model.addAttribute("veiculos", veiculosDisponiveis);

        return "cadastrar-viagem";
    }

    @PostMapping("/salvar")
    public String salvarViagem(@ModelAttribute("viagem") ViagemDTO viagemDTO, HttpSession session) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (usuarioLogado.getCargo() != UsuarioDTO.Cargo.ADMIN
                && usuarioLogado.getCargo() != UsuarioDTO.Cargo.GESTOR_FROTA) {
            return "redirect:/";
        }

        viagemService.registrar(viagemDTO);
        return "redirect:/viagens/listar";
    }

    @GetMapping("/listar")
    public String listarViagens(Model model, HttpSession session) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute("viagens", viagemService.listarTodas());
        model.addAttribute("usuario", usuarioLogado);
        return "listar-viagens";
    }

    @GetMapping("/minhas-viagens")
    public String listarMinhasViagens(Model model, HttpSession session) {
        return listarViagens(model, session);
    }

    @GetMapping("/finalizar/{id}")
    public String exibirFormularioFinalizar(@PathVariable("id") Long id, Model model, HttpSession session) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        ViagemDTO viagem = viagemService.buscarPorId(id);
        model.addAttribute("viagem", viagem);

        return "finalizar-viagem";
    }

    @PostMapping("/finalizar")
    public String processarFinalizacao(@RequestParam("idViagem") Long idViagem, 
                                       @RequestParam("kmFinal") Double kmFinal, 
                                       HttpSession session,
                                       RedirectAttributes redirect) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        boolean sucesso = viagemService.finalizarViagem(idViagem, kmFinal);

        if (sucesso) {
            redirect.addFlashAttribute("mensagemSucesso", "Viagem finalizada com sucesso!");
        } else {
            redirect.addFlashAttribute("mensagemErro", "Erro ao finalizar a viagem. Verifique o Km final informado.");
        }

        return "redirect:/viagens/listar";
    }
}