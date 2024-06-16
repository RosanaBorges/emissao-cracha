package com.lojas.emissao_cracha.unit.controller;


import com.lojas.emissao_cracha.controller.CrachaController;
import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.dto.CrachaDtoResponse;
import com.lojas.emissao_cracha.exception.CrachaNaoEncontradoException;
import com.lojas.emissao_cracha.exception.SalvarFotoException;
import com.lojas.emissao_cracha.service.CrachaService;
import com.lojas.emissao_cracha.util.FotoUploadUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*@WebMvcTest(CrachaController.class)
public class CrachaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrachaService crachaService;

    @MockBean
    private FotoUploadUtil fotoUploadUtil;


    @Test
    public void testEmitirCracha() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "Cargo Teste", new MockMultipartFile("foto", new byte[]{}));
        CrachaDtoResponse cracha = new CrachaDtoResponse(1L, "Nome Teste", "Cargo Teste", "foto.jpg", "qrCode.png");

        when(crachaService.emitirCracha(any(CrachaDtoRequest.class))).thenReturn(cracha);

        mockMvc.perform(multipart("/api/v1/crachas/emitir")
                        .file("foto", new byte[]{})
                        .param("nome", "Nome Teste")
                        .param("cargo", "Cargo Teste")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("crachaDtoRequest", crachaDtoRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.cargo").value("Cargo Teste"))
                .andExpect(jsonPath("$.foto").value("foto.jpg"))
                .andExpect(jsonPath("$.qrCode").value("qrCode.png"));
    }

    @Test
    public void testAtualizarCracha() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("John Doe Updated", "Senior Developer", new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "foto content".getBytes()));
        CrachaDtoResponse cracha = new CrachaDtoResponse(1L, "John Doe Updated", "Senior Developer", "foto_updated.jpg","qr.png");

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
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "foto content".getBytes());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .file(foto)
                        .param("nome", "")  // Nome vazio
                        .param("cargo", "") // Cargo vazio
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertTrue(responseContent.contains("\"field\":\"nome\",\"message\":\"Por favor escreva o nome que deseja ser chamado.\""));
        assertTrue(responseContent.contains("\"field\":\"cargo\",\"message\":\"Por favor insira o seu cargo.\""));
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
        when(crachaService.atualizarCracha(eq(id), any(CrachaDtoRequest.class))).thenThrow(new CrachaNaoEncontradoException("Crachá não encontrado com a matricula:" + id));

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
                .andExpect(jsonPath("$.message").value("Crachá não encontrado com a matricula:" + id));
    }

    @Test
    public void testBuscarCrachaPorId_CrachaNaoEncontrado() throws Exception {
        Long id = 1L;

        when(crachaService.buscarCrachaPorId(id)).thenThrow(new CrachaNaoEncontradoException("Crachá não encontrado pela matricula: " + id));

        mockMvc.perform(get("/api/v1/crachas/buscar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Crachá não encontrado pela matricula: " + id));
    }

    @Test
    public void testEmitirCracha_SalvarFotoException() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "Cargo Teste", new MockMultipartFile("foto", "foto.jpg", "image/jpeg", new byte[0]));

        when(crachaService.emitirCracha(any(CrachaDtoRequest.class))).thenThrow(new SalvarFotoException("Erro ao salvar a foto"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .file((MockMultipartFile) crachaDtoRequest.getFoto())
                        .param("nome", crachaDtoRequest.getNome())
                        .param("cargo", crachaDtoRequest.getCargo())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro ao salvar a foto"));
    }

    @Test
    public void testBuscarCrachaPorId_Sucesso() throws Exception {
        Long id = 1L;
        CrachaDtoResponse cracha = new CrachaDtoResponse(id, "Nome Teste", "Cargo Teste", "foto.jpg", "qrcode.png");

        when(crachaService.buscarCrachaPorId(id)).thenReturn(cracha);

        mockMvc.perform(get("/api/v1/crachas/buscar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.cargo").value("Cargo Teste"))
                .andExpect(jsonPath("$.foto").value("foto.jpg"))
                .andExpect(jsonPath("$.qrCode").value("qrcode.png"));
    }

}
