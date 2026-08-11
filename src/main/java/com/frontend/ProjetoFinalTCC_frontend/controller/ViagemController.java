package com.frontend.ProjetoFinalTCC_frontend.controller;

import com.frontend.ProjetoFinalTCC_frontend.model.UsuarioDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.VeiculoDTO;
import com.frontend.ProjetoFinalTCC_frontend.model.VeiculoDTO.StatusVeiculo;
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

        // A listagem completa (todas as viagens, de todos os motoristas) é só para quem gerencia a frota.
        if (usuarioLogado.getCargo() == UsuarioDTO.Cargo.MOTORISTA) {
            return "redirect:/viagens/minhas-viagens";
        }

        model.addAttribute("viagens", viagemService.listarTodas());
        model.addAttribute("usuario", usuarioLogado);
        return "listar-viagens";
    }

    @GetMapping("/minhas-viagens")
    public String minhasViagens(Model model, HttpSession session) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (usuarioLogado.getCargo() != UsuarioDTO.Cargo.MOTORISTA) {
            return "redirect:/viagens/listar";
        }

        Integer meuId = usuarioLogado.getIdUsuario().intValue();
        List<ViagemDTO> todas = viagemService.listarTodas();

        // Disponíveis: sem motorista vinculado ainda (qualquer motorista pode assumir).
        List<ViagemDTO> disponiveis = todas.stream()
                .filter(v -> v.getIdUsuario() == null
                        && (v.getStatusViagem() == null || v.getStatusViagem() != ViagemDTO.StatusViagem.FINALIZADA))
                .toList();

        // Em andamento: assumidas por MIM e ainda não finalizadas.
        List<ViagemDTO> emAndamento = todas.stream()
                .filter(v -> meuId.equals(v.getIdUsuario())
                        && (v.getStatusViagem() == null || v.getStatusViagem() != ViagemDTO.StatusViagem.FINALIZADA))
                .toList();

        // Concluídas: finalizadas por MIM. Viagens de outros motoristas nunca aparecem aqui.
        List<ViagemDTO> concluidas = todas.stream()
                .filter(v -> meuId.equals(v.getIdUsuario())
                        && v.getStatusViagem() == ViagemDTO.StatusViagem.FINALIZADA)
                .toList();

        model.addAttribute("usuario", usuarioLogado);
        model.addAttribute("disponiveis", disponiveis);
        model.addAttribute("emAndamento", emAndamento);
        model.addAttribute("concluidas", concluidas);

        return "minhas-viagens";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") Long id, Model model, HttpSession session,
                                          RedirectAttributes redirectAttributes) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (usuarioLogado.getCargo() != UsuarioDTO.Cargo.ADMIN
                && usuarioLogado.getCargo() != UsuarioDTO.Cargo.GESTOR_FROTA) {
            return "redirect:/";
        }

        try {
            ViagemDTO viagem = viagemService.buscarPorId(id);

            if (viagem == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Viagem não encontrada.");
                return "redirect:/viagens/listar";
            }

            List<VeiculoDTO> veiculos = veiculoService.listarTodos();

            model.addAttribute("viagem", viagem);
            model.addAttribute("veiculos", veiculos);
            model.addAttribute("estados", ViagemDTO.Estado.values());
            model.addAttribute("statusOptions", ViagemDTO.StatusViagem.values());

            return "editar-viagens";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao carregar viagem para edição: " + e.getMessage());
            return "redirect:/viagens/listar";
        }
    }

    @PostMapping("/atualizar")
    public String atualizarViagem(@ModelAttribute("viagem") ViagemDTO viagem, HttpSession session,
                                   RedirectAttributes redirect) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (usuarioLogado.getCargo() != UsuarioDTO.Cargo.ADMIN
                && usuarioLogado.getCargo() != UsuarioDTO.Cargo.GESTOR_FROTA) {
            return "redirect:/";
        }

        try {
            viagemService.editarViagem(viagem);
            redirect.addFlashAttribute("mensagemSucesso", "Viagem atualizada com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensagemErro", "Erro ao atualizar viagem: " + e.getMessage());
        }

        return "redirect:/viagens/listar";
    }

    // Depois de ações do motorista (assumir/finalizar), ele volta para a tela dele;
    // gestor/admin voltam para a listagem completa.
    private String redirectPosAcao(UsuarioDTO usuarioLogado) {
        if (usuarioLogado.getCargo() == UsuarioDTO.Cargo.MOTORISTA) {
            return "redirect:/viagens/minhas-viagens";
        }
        return "redirect:/viagens/listar";
    }

    @PostMapping("/assumir")
    public String assumirViagem(@RequestParam("idViagem") Long idViagem, HttpSession session,
                                 RedirectAttributes redirect) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (usuarioLogado.getCargo() != UsuarioDTO.Cargo.MOTORISTA) {
            return "redirect:/";
        }

        try {
            viagemService.assumirViagem(idViagem, usuarioLogado.getIdUsuario().intValue());
            redirect.addFlashAttribute("mensagemSucesso", "Viagem assumida com sucesso! Ao concluir, finalize-a na sua lista.");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensagemErro", "Erro ao assumir viagem: " + e.getMessage());
        }

        return redirectPosAcao(usuarioLogado);
    }

    @GetMapping("/finalizar/{id}")
    public String exibirFormularioFinalizar(@PathVariable("id") Long id, Model model, HttpSession session,
                                             RedirectAttributes redirect) {
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        ViagemDTO viagem = viagemService.buscarPorId(id);

        if (viagem == null) {
            redirect.addFlashAttribute("mensagemErro", "Viagem não encontrada.");
            return redirectPosAcao(usuarioLogado);
        }

        if (usuarioLogado.getCargo() == UsuarioDTO.Cargo.MOTORISTA) {
            if (viagem.getIdUsuario() == null) {
                redirect.addFlashAttribute("mensagemErro", "Você precisa assumir a viagem antes de finalizá-la.");
                return redirectPosAcao(usuarioLogado);
            }
            if (!viagem.getIdUsuario().equals(usuarioLogado.getIdUsuario().intValue())) {
                redirect.addFlashAttribute("mensagemErro", "Esta viagem já foi assumida por outro motorista.");
                return redirectPosAcao(usuarioLogado);
            }
        }

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

        try {
            viagemService.finalizarViagem(idViagem, kmFinal);
            redirect.addFlashAttribute("mensagemSucesso", "Viagem finalizada com sucesso! O veículo já está disponível novamente.");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensagemErro", "Erro ao finalizar a viagem: " + e.getMessage());
        }

        return redirectPosAcao(usuarioLogado);
    }
}