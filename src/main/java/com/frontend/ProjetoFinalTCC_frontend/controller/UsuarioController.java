package com.frontend.ProjetoFinalTCC_frontend.controller;

import com.frontend.ProjetoFinalTCC_frontend.model.UsuarioDTO;
import com.frontend.ProjetoFinalTCC_frontend.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {
 
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String home(HttpSession session) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null || usuarioLogado.getCargo() == null) {
            return "redirect:/login";
        }

        switch (usuarioLogado.getCargo()) {
            case ADMIN:
                return "redirect:/admin/dashboard";
            case GESTOR_FROTA:
                return "redirect:/gestor/dashboard";
            case MOTORISTA:
                return "redirect:/motorista/dashboard";
            default:
                return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String exibirFormularioLogin(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "login";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam String email, 
                                 @RequestParam String senha, 
                                 HttpSession session, 
                                 Model model) {
        
        UsuarioDTO usuario = usuarioService.autenticar(email, senha);

        if (usuario != null) {
            session.setAttribute("usuario", usuario);
            return "redirect:/";
        }

        model.addAttribute("usuario", new UsuarioDTO());
        model.addAttribute("erroServidor", "E-mail ou senha inválidos.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/admin/dashboard")
    public String dashboardAdmin(HttpSession session, Model model) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");
        if (usuarioLogado == null || usuarioLogado.getCargo() != UsuarioDTO.Cargo.ADMIN) {
            return "redirect:/";
        }
        model.addAttribute("usuario", usuarioLogado);
        return "dashboard-admin";
    }

    @GetMapping("/gestor/dashboard")
    public String dashboardGestor(HttpSession session, Model model) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");
        if (usuarioLogado == null || usuarioLogado.getCargo() != UsuarioDTO.Cargo.GESTOR_FROTA) {
            return "redirect:/";
        }
        model.addAttribute("usuario", usuarioLogado);
        return "dashboard-gestor";
    }

    @GetMapping("/motorista/dashboard")
    public String dashboardMotorista(HttpSession session, Model model) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");
        if (usuarioLogado == null || usuarioLogado.getCargo() != UsuarioDTO.Cargo.MOTORISTA) {
            return "redirect:/";
        }
        model.addAttribute("usuario", usuarioLogado);
        return "dashboard-motorista";
    }
}