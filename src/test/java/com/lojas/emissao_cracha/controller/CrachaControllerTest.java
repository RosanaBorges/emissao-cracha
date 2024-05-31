package com.lojas.emissao_cracha.controller;


import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.exception.CrachaNaoEncontradoException;
import com.lojas.emissao_cracha.exception.ErroInternoException;
import com.lojas.emissao_cracha.exception.SalvarFotoException;
import com.lojas.emissao_cracha.service.CrachaService;
import com.lojas.emissao_cracha.util.FotoUploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrachaController.class)
public class CrachaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrachaService crachaService;

    @InjectMocks
    private CrachaController crachaController;

    @MockBean
    private FotoUploadUtil fotoUploadUtil;



    @Test
    public void testEmitirCracha() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "Cargo Teste", new MockMultipartFile("foto", new byte[] {}));
        Cracha cracha = new Cracha(1L, "Nome Teste", "Cargo Teste", "foto.jpg");

        when(crachaService.emitirCracha(any(CrachaDtoRequest.class))).thenReturn(cracha);

        mockMvc.perform(post("/api/v1/crachas/emitir")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("crachaDtoRequest", crachaDtoRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.cargo").value("Cargo Teste"))
                .andExpect(jsonPath("$.foto").value("foto.jpg"));
    }

    @Test
    public void testAtualizarCracha() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("John Doe Updated", "Senior Developer", new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "foto content".getBytes()));
        Cracha cracha = new Cracha(1L, "John Doe Updated", "Senior Developer", "foto_updated.jpg");

        when(crachaService.atualizarCracha(eq(1L), any(CrachaDtoRequest.class))).thenReturn(cracha);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/atualizar/1")
                        .file((MockMultipartFile) crachaDtoRequest.getFoto())
                        .param("nome", crachaDtoRequest.getNome())
                        .param("cargo", crachaDtoRequest.getCargo())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("John Doe Updated"))
                .andExpect(jsonPath("$.cargo").value("Senior Developer"))
                .andExpect(jsonPath("$.foto").value("foto_updated.jpg"));
    }

    private MultipartFile mockMultipartFile() {
        return new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "foto content".getBytes());
    }

    @Test
    public void testEmitirCracha_ComCamposNomeECargoInvalidos() throws Exception {
        // Cria uma foto válida
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "foto content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .file(foto)
                        .param("nome", "")  // Nome vazio
                        .param("cargo", "") // Cargo vazio
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("nome"))
                .andExpect(jsonPath("$[0].message").value("Por favor escreva o nome que deseja ser chamado."))
                .andExpect(jsonPath("$[1].field").value("cargo"))
                .andExpect(jsonPath("$[1].message").value("Por favor insira o seu cargo."));
    }

    @Test
    public void testEmitirCracha_ComFotoInvalida() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "Cargo Teste", null);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .param("nome", crachaDtoRequest.getNome())
                        .param("cargo", crachaDtoRequest.getCargo())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").value("Por favor insira a foto."));
    }

    @Test
    public void testAtualizarCracha_CrachaNaoEncontrado() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("John Doe Updated", "Senior Developer", new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "foto content".getBytes()));
        Long id = 1L;
        when(crachaService.atualizarCracha(eq(id), any(CrachaDtoRequest.class))).thenThrow(new CrachaNaoEncontradoException("Crachá não encontrado com a matricula:" +id));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/atualizar/1")
                        .file((MockMultipartFile) crachaDtoRequest.getFoto())
                        .param("nome", crachaDtoRequest.getNome())
                        .param("cargo", crachaDtoRequest.getCargo())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Crachá não encontrado com a matricula:" +id));
    }

    @Test//TODO RETORNANDO ERRADO
    public void testBuscarCrachaPorId_CrachaNaoEncontrado() throws Exception {
        Long id = 1L;

        when(crachaService.buscarCrachaPorId(id)).thenThrow(new CrachaNaoEncontradoException("Crachá não encontrado pela matricula: " + id));

        mockMvc.perform(get("/api/v1/crachas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Crachá não encontrado pela matricula: " + id));
    }

    @Test//TODO RETORNANDO ERRADO
    public void testEmitirCracha_SalvarFotoException() throws Exception {
        // Cria um CrachaDtoRequest com uma foto válida
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "Cargo Teste", new MockMultipartFile("foto", "foto.jpg", "image/jpeg", new byte[0]));

        // Simula o comportamento do método fotoUploadUtil.salvarFoto para lançar uma IOException
        doThrow(new IOException("Erro ao salvar a foto")).when(fotoUploadUtil).salvarFoto(any(MultipartFile.class));

        // Simula a exceção SalvarFotoException ao chamar o método emitirCracha
        when(crachaService.emitirCracha(any(CrachaDtoRequest.class))).thenThrow(new SalvarFotoException("foto", "Erro ao salvar a foto"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .file((MockMultipartFile) crachaDtoRequest.getFoto())
                        .param("nome", crachaDtoRequest.getNome())
                        .param("cargo", crachaDtoRequest.getCargo())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$[0].field").value("foto"))
                .andExpect(jsonPath("$[0].message").value("Erro ao salvar a foto"));
    }


}
