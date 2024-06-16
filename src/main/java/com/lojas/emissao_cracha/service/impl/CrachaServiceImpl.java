package com.lojas.emissao_cracha.service.impl;

import com.google.zxing.WriterException;
import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.dto.CrachaDtoResponse;
import com.lojas.emissao_cracha.exception.*;
import com.lojas.emissao_cracha.repository.CrachaRepository;
import com.lojas.emissao_cracha.service.CrachaService;
import com.lojas.emissao_cracha.util.FotoUploadUtil;
import com.lojas.emissao_cracha.util.QRCodeUtil;
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

    @Autowired
    private QRCodeUtil qrCodeUtil;

    @Override
    public CrachaDtoResponse emitirCracha(CrachaDtoRequest crachaDtoRequest) {
        validadorDeFotos(crachaDtoRequest.getFoto());
        try {
            String fotoNome = fotoUploadUtil.salvarFoto(crachaDtoRequest.getFoto());
            Cracha cracha = new Cracha();
            cracha.setNome(crachaDtoRequest.getNome());
            cracha.setCargo(crachaDtoRequest.getCargo());
            cracha.setFoto(fotoNome);
            cracha = crachaRepository.save(cracha);

            // Gerar QRCode com base no ID do crachá
            String qrCodeNome = cracha.getId() + "_qrcode.png";
            qrCodeUtil.geradorQRCode("ID: " + cracha.getId(), qrCodeNome);
            cracha.setQrCode(qrCodeNome);
            cracha = crachaRepository.save(cracha);

            // Converter para CrachaDtoResponse
            CrachaDtoResponse response = new CrachaDtoResponse();
            response.setId(cracha.getId());
            response.setNome(cracha.getNome());
            response.setCargo(cracha.getCargo());
            response.setFoto(cracha.getFoto());
            response.setQrCode(cracha.getQrCode());

            return response;
        } catch (IOException e) {
            throw new SalvarFotoException("Erro ao salvar a foto");
        } catch (WriterException e) {
            throw new ErroInternoException("Erro ao gerar o QRCode");
        } catch (Exception e) {
            throw new ErroInternoException("Erro interno por favor entre em contato com administrador", e);
        }
    }

    private void validadorDeFotos(MultipartFile foto) {
        if (foto == null || foto.isEmpty()) {
            throw new InserirFotoException("Por favor insira a foto.", "foto");
        }
    }

    @Override
    public CrachaDtoResponse buscarCrachaPorId(Long id) {
        Cracha cracha = crachaRepository.findById(id)
                .orElseThrow(() -> new CrachaNaoEncontradoException("Crachá não encontrado pelo ID: " + id));

        CrachaDtoResponse response = new CrachaDtoResponse();
        response.setId(cracha.getId());
        response.setNome(cracha.getNome());
        response.setCargo(cracha.getCargo());
        response.setFoto(cracha.getFoto());
        response.setQrCode(cracha.getQrCode());

        return response;
    }

    @Override
    public CrachaDtoResponse atualizarCracha(Long id, CrachaDtoRequest crachaDtoRequest) {
        Cracha cracha = crachaRepository.findById(id)
                .orElseThrow(() -> new CrachaNaoEncontradoException("Crachá não encontrado pelo ID: " + id));
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
        cracha = crachaRepository.save(cracha);

        CrachaDtoResponse response = new CrachaDtoResponse();
        response.setId(cracha.getId());
        response.setNome(cracha.getNome());
        response.setCargo(cracha.getCargo());
        response.setFoto(cracha.getFoto());
        response.setQrCode(cracha.getQrCode());

        return response;
    }
}