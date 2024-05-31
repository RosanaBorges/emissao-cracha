package com.lojas.emissao_cracha.controller;

import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.service.CrachaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/crachas")
public class CrachaController {

    @Autowired
    private CrachaService crachaService;

    @PostMapping("emitir")
    public ResponseEntity<Cracha> emitirCracha(@Valid @ModelAttribute CrachaDtoRequest crachaDtoRequest) {
        Cracha novoCracha = crachaService.emitirCracha(crachaDtoRequest);
        return ResponseEntity.ok(novoCracha);
    }

    @GetMapping("buscar/{id}")
    public ResponseEntity<Cracha> buscarCrachaPorId(@PathVariable Long id) {
        Cracha cracha = crachaService.buscarCrachaPorId(id);
        return ResponseEntity.ok(cracha);
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<Cracha> atualizarCracha(
            @PathVariable Long id,
            @Valid @ModelAttribute CrachaDtoRequest crachaDtoRequest) {
        Cracha crachaAtualizado = crachaService.atualizarCracha(id, crachaDtoRequest);
        return ResponseEntity.ok(crachaAtualizado);
    }

}
