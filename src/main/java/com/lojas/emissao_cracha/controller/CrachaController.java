package com.lojas.emissao_cracha.controller;

import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.dto.CrachaDtoResponse;
import com.lojas.emissao_cracha.service.CrachaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/crachas")
public class CrachaController {

    @Autowired
    private CrachaService crachaService;

    @PostMapping("/emitir")
    public String emitirCracha(@ModelAttribute CrachaDtoRequest crachaDtoRequest, Model model) {
        CrachaDtoResponse novoCracha = crachaService.emitirCracha(crachaDtoRequest);
        model.addAttribute("cracha", novoCracha);
        model.addAttribute("message", "Crachá criado com sucesso");
        return "criarCracha";
    }

    @GetMapping("/buscar/{id}")
    public String buscarCrachaPorId(@PathVariable Long id, Model model) {
        CrachaDtoResponse cracha = crachaService.buscarCrachaPorId(id);
        model.addAttribute("cracha", cracha);
        return "buscarCracha";
    }

    @PutMapping("/atualizar/{id}")
    public String atualizarCracha(@PathVariable Long id, @ModelAttribute CrachaDtoRequest crachaDtoRequest, Model model) {
        CrachaDtoResponse crachaAtualizado = crachaService.atualizarCracha(id, crachaDtoRequest);
        model.addAttribute("cracha", crachaAtualizado);
        return "buscarCracha";
    }

    @GetMapping("/template/{id}")
    public String exibirTemplateCracha(@PathVariable Long id, Model model) {
        CrachaDtoResponse cracha = crachaService.buscarCrachaPorId(id);
        model.addAttribute("cracha", cracha);
        return "crachaTemplate";
    }
}
