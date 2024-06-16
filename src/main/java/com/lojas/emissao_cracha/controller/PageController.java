package com.lojas.emissao_cracha.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/criarCracha")
    public String criarCracha() {
        return "criarCracha";
    }

    @GetMapping("/buscarCracha")
    public String buscarCracha(@RequestParam Long id, Model model) {
        return "buscarCracha";
    }

    @GetMapping("/atualizarCracha")
    public String atualizarCracha() {
        return "atualizarCracha";
    }
}
