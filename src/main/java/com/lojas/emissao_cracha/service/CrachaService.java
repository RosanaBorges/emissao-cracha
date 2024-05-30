package com.lojas.emissao_cracha.service;

import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface CrachaService {

    Cracha emitirCracha(CrachaDtoRequest crachaDtoRequest) throws IOException;

    Optional<Cracha> buscarCrachaPorId(Long id);

    Cracha atualizarCracha(Long id, CrachaDtoRequest crachaDtoRequest) throws IOException;
}
