package com.lojas.emissao_cracha.unit.service.impl;

import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.dto.CrachaDtoResponse;
import com.lojas.emissao_cracha.exception.InserirFotoException;
import com.lojas.emissao_cracha.repository.CrachaRepository;
import com.lojas.emissao_cracha.service.impl.CrachaServiceImpl;
import com.lojas.emissao_cracha.util.FotoUploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CrachaServiceImplTest {

    @Mock
    private CrachaRepository crachaRepository;

    @Mock
    private FotoUploadUtil fotoUploadUtil;

    @InjectMocks
    private CrachaServiceImpl crachaServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEmitirCracha() throws IOException {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest();
        crachaDtoRequest.setNome("Sheldon Cooper");
        crachaDtoRequest.setCargo("Fisico");
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        crachaDtoRequest.setFoto(mockFile);

        when(fotoUploadUtil.salvarFoto(mockFile)).thenReturn("photo.jpg");

        Cracha crachaSalvo = new Cracha();
        crachaSalvo.setId(1L);
        crachaSalvo.setNome("Sheldon Cooper");
        crachaSalvo.setCargo("Fisico");
        crachaSalvo.setFoto("photo.jpg");

        when(crachaRepository.save(any(Cracha.class))).thenReturn(crachaSalvo);

        CrachaDtoResponse result = crachaServiceImpl.emitirCracha(crachaDtoRequest);

        assertNotNull(result);
        assertEquals("Sheldon Cooper", result.getNome());
        assertEquals("Fisico", result.getCargo());
        assertEquals("photo.jpg", result.getFoto());
    }

   /* @Test
    public void testBuscarCrachaPorId() {
        Cracha cracha = new Cracha();
        cracha.setId(1L);
        cracha.setNome("John Doe");

        when(crachaRepository.findById(1L)).thenReturn(Optional.of(cracha));

        Cracha result = crachaServiceImpl.buscarCrachaPorId(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getNome());
    }

    @Test
    public void testAtualizarCracha() throws IOException {
        Cracha existeCracha = new Cracha();
        existeCracha.setId(1L);
        existeCracha.setNome("Rajesh Koothrappali");
        existeCracha.setCargo("Astrofisico");

        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest();
        crachaDtoRequest.setNome("Leonard Hofstadter");
        crachaDtoRequest.setCargo("Fisico teorico");

        when(crachaRepository.findById(1L)).thenReturn(Optional.of(existeCracha));

        Cracha atualizarCracha = new Cracha();
        atualizarCracha.setId(1L);
        atualizarCracha.setNome("Leonard Hofstadter");
        atualizarCracha.setCargo("Fisico teorico");

        when(crachaRepository.save(any(Cracha.class))).thenReturn(atualizarCracha);

        Cracha result = crachaServiceImpl.atualizarCracha(1L, crachaDtoRequest);

        assertNotNull(result);
        assertEquals("Leonard Hofstadter", result.getNome());
        assertEquals("Fisico teorico", result.getCargo());
    }*/

    @Test
    public void testEmitirCrachaComFotoNula() {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest();
        crachaDtoRequest.setNome("Howard Wolowitz");
        crachaDtoRequest.setCargo("Engenheiro");
        crachaDtoRequest.setFoto(null);

        InserirFotoException exception = assertThrows(InserirFotoException.class, () -> {
            crachaServiceImpl.emitirCracha(crachaDtoRequest);
        });

        String expectedMessage = "Por favor insira a foto.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testEmitirCrachaComFotoVazia() {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest();
        crachaDtoRequest.setNome("Penny");
        crachaDtoRequest.setCargo("Vendedora");
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(true);
        crachaDtoRequest.setFoto(mockFile);

        InserirFotoException exception = assertThrows(InserirFotoException.class, () -> {
            crachaServiceImpl.emitirCracha(crachaDtoRequest);
        });

        String expectedMessage = "Por favor insira a foto.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
