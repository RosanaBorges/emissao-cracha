package com.lojas.emissao_cracha.service;

import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;

public interface CrachaService {

    Cracha emitirCracha(CrachaDtoRequest crachaDtoRequest);

    Cracha buscarCrachaPorId(Long id);

    Cracha atualizarCracha(Long id, CrachaDtoRequest crachaDtoRequest);
}
