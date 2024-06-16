package com.lojas.emissao_cracha.service;

import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.dto.CrachaDtoResponse;

public interface CrachaService {

    CrachaDtoResponse emitirCracha(CrachaDtoRequest crachaDtoRequest);

    CrachaDtoResponse buscarCrachaPorId(Long id);

    CrachaDtoResponse atualizarCracha(Long id, CrachaDtoRequest crachaDtoRequest);
}
