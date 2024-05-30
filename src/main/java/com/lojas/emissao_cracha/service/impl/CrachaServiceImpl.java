package com.lojas.emissao_cracha.service.impl;

import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.repository.CrachaRepository;
import com.lojas.emissao_cracha.service.CrachaService;
import com.lojas.emissao_cracha.util.FotoUploadUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class CrachaServiceImpl implements CrachaService {

    @Autowired
    private CrachaRepository crachaRepository;

    @Autowired
    private FotoUploadUtil fotoUploadUtil;


    @Override
    public Cracha emitirCracha(CrachaDtoRequest crachaDtoRequest) throws IOException {
        validadorDeFotos(crachaDtoRequest.getFoto());
        String fotoNome = fotoUploadUtil.salvarFoto(crachaDtoRequest.getFoto());
        Cracha cracha = new Cracha();
        cracha.setNome(crachaDtoRequest.getNome());
        cracha.setCargo(crachaDtoRequest.getCargo());
        cracha.setFoto(fotoNome);
        return crachaRepository.save(cracha);
    }


    @Override
    public Optional<Cracha> buscarCrachaPorId(Long id) {
        return crachaRepository.findById(id);
    }


    @Override
    public Cracha atualizarCracha(Long id, CrachaDtoRequest crachaDtoRequest) throws IOException {
        Cracha cracha = crachaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cracha não encontrado"));
        cracha.setNome(crachaDtoRequest.getNome());
        cracha.setCargo(crachaDtoRequest.getCargo());
        if (crachaDtoRequest.getFoto() != null && !crachaDtoRequest.getFoto().isEmpty()) {
            validadorDeFotos(crachaDtoRequest.getFoto());
            String fotoNome = fotoUploadUtil.salvarFoto(crachaDtoRequest.getFoto());
            cracha.setFoto(fotoNome);
        }
        return crachaRepository.save(cracha);
    }

    private void validadorDeFotos(MultipartFile foto) {
        if (foto == null || foto.isEmpty()) {
            throw new IllegalArgumentException("Por favor insira a foto.");
        }

    }
}
