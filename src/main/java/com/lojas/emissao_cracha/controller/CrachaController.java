package com.lojas.emissao_cracha.controller;

import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.service.CrachaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/v1/crachas")
public class CrachaController {

    @Autowired
    private CrachaService crachaService;

    @PostMapping("/emitir")
    public String emitirCracha(@ModelAttribute CrachaDtoRequest crachaDtoRequest, Model model) {
        Cracha novoCracha = crachaService.emitirCracha(crachaDtoRequest);
        model.addAttribute("cracha", novoCracha);
        model.addAttribute("message", "Crachá criado com sucesso");
        return "criarCracha";
    }

    @GetMapping("/buscar/{id}")
    public String buscarCrachaPorId(@PathVariable Long id, Model model) {
        Cracha cracha = crachaService.buscarCrachaPorId(id);
        model.addAttribute("cracha", cracha);
        return "buscarCracha";
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<Cracha> atualizarCracha(
            @PathVariable Long id,
            @Valid @ModelAttribute CrachaDtoRequest crachaDtoRequest) {
        Cracha crachaAtualizado = crachaService.atualizarCracha(id, crachaDtoRequest);
        return ResponseEntity.ok(crachaAtualizado);
    }

    @GetMapping("/template/{id}")
    public String exibirTemplateCracha(@PathVariable Long id, Model model) {
        Cracha cracha = crachaService.buscarCrachaPorId(id);
        model.addAttribute("cracha", cracha);
        return "crachaTemplate";
    }

}
