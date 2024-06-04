package com.lojas.emissao_cracha.service.impl;

import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.exception.CrachaNaoEncontradoException;
import com.lojas.emissao_cracha.exception.ErroInternoException;
import com.lojas.emissao_cracha.exception.InserirFotoException;
import com.lojas.emissao_cracha.exception.SalvarFotoException;
import com.lojas.emissao_cracha.repository.CrachaRepository;
import com.lojas.emissao_cracha.service.CrachaService;
import com.lojas.emissao_cracha.util.FotoUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CrachaServiceImpl implements CrachaService {

    @Autowired
    private CrachaRepository crachaRepository;

    @Autowired
    private FotoUploadUtil fotoUploadUtil;


    @Override
    public Cracha emitirCracha(CrachaDtoRequest crachaDtoRequest) {
        validadorDeFotos(crachaDtoRequest.getFoto());
        try {
            String fotoNome = fotoUploadUtil.salvarFoto(crachaDtoRequest.getFoto());
            Cracha cracha = new Cracha();
            cracha.setNome(crachaDtoRequest.getNome());
            cracha.setCargo(crachaDtoRequest.getCargo());
            cracha.setFoto(fotoNome);
            return crachaRepository.save(cracha);
        } catch (IOException e) {
            throw new SalvarFotoException("Erro ao salvar a foto");
        } catch (Exception e) {
            throw new ErroInternoException("Erro interno por favor entre em contato com administrador", e);
        }
    }


    @Override
    public Cracha buscarCrachaPorId(Long id) {
        return crachaRepository.findById(id)
                .orElseThrow(() -> new CrachaNaoEncontradoException("Cracha não encontrado pela matricula: " + id));
    }


    @Override
    public Cracha atualizarCracha(Long id, CrachaDtoRequest crachaDtoRequest) {
        Cracha cracha = crachaRepository.findById(id)
                .orElseThrow(() -> new CrachaNaoEncontradoException("Cracha não encontrado pela matricula: " + id));
        cracha.setNome(crachaDtoRequest.getNome());
        cracha.setCargo(crachaDtoRequest.getCargo());
        if (crachaDtoRequest.getFoto() != null && !crachaDtoRequest.getFoto().isEmpty()) {
            validadorDeFotos(crachaDtoRequest.getFoto());
            try {
                String fotoNome = fotoUploadUtil.salvarFoto(crachaDtoRequest.getFoto());
                cracha.setFoto(fotoNome);
            } catch (IOException e) {
                throw new SalvarFotoException("Erro ao salvar a foto");
            }
        }
        try {
            return crachaRepository.save(cracha);
        } catch (Exception e) {
            throw new ErroInternoException("Erro interno por favor entre em contato com administrador", e);
        }
    }

    private void validadorDeFotos(MultipartFile foto) {
        if (foto == null || foto.isEmpty()) {
            throw new InserirFotoException("foto", "Por favor insira a foto.");
        }

    }
}
