package com.lojas.emissao_cracha.controller;

import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.repository.CrachaRepository;
import com.lojas.emissao_cracha.service.CrachaService;
import com.lojas.emissao_cracha.util.FotoUploadUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/crachas")
public class CrachaController {

    @Autowired
    private CrachaService crachaService;

    @PostMapping("emitir")
    public ResponseEntity<Cracha> emitirCracha(@Valid @ModelAttribute CrachaDtoRequest crachaDtoRequest) throws IOException {
        Cracha novoCracha = crachaService.emitirCracha(crachaDtoRequest);
        return ResponseEntity.ok(novoCracha);
    }

    @GetMapping("buscar/{id}")
    public ResponseEntity<Cracha> buscarCrachaPorId(@PathVariable Long id) {
        Optional<Cracha> cracha = crachaService.buscarCrachaPorId(id);
        return cracha.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<Cracha> atualizarCracha(
            @PathVariable Long id,
            @Valid @ModelAttribute CrachaDtoRequest crachaDtoRequest) throws IOException {
        Cracha crachaAtualizado = crachaService.atualizarCracha(id, crachaDtoRequest);
        return ResponseEntity.ok(crachaAtualizado);
    }

}
