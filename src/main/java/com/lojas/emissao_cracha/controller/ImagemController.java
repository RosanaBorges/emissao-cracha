package com.lojas.emissao_cracha.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImagemController {

    @Value("${file.upload-dir}")
    private String diretorioUpload;

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> servirArquivo(@PathVariable String filename) {
        try {
            Path arquivo = Paths.get(diretorioUpload).resolve(filename);
            Resource recurso = new UrlResource(arquivo.toUri());
            if (recurso.exists() || recurso.isReadable()) {
                return ResponseEntity.ok(recurso);
            } else {
                throw new RuntimeException("Não foi possível ler o arquivo: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível ler o arquivo: " + filename, e);
        }
    }
}
