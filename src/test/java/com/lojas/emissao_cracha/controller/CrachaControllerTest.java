package com.lojas.emissao_cracha.controller;


import com.lojas.emissao_cracha.domain.Cracha;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.exception.CrachaNaoEncontradoException;
import com.lojas.emissao_cracha.exception.ErroInternoException;
import com.lojas.emissao_cracha.exception.SalvarFotoException;
import com.lojas.emissao_cracha.service.CrachaService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(crachaController).build();
    }

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

    @Test//TODO RETORNANDO ERRO
    public void testEmitirCracha_ComCamposInvalidos() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("", "", null);

        mockMvc.perform(post("/api/v1/crachas/emitir")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("crachaDtoRequest", crachaDtoRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("Por favor escreva o nome que deseja ser chamado."))
                .andExpect(jsonPath("$.cargo").value("Por favor insira o seu cargo."));
    }

    @Test//TODO RETORNANDO ERRADO
    public void testEmitirCracha_SalvarFotoException() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "Cargo Teste", new MockMultipartFile("foto", new byte[] {}));

        when(crachaService.emitirCracha(any(CrachaDtoRequest.class))).thenThrow(new SalvarFotoException("Erro ao salvar a foto", "foto"));

        mockMvc.perform(post("/api/v1/crachas/emitir")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("crachaDtoRequest", crachaDtoRequest))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro ao salvar a foto"));
    }


    @Test
    public void testBuscarCrachaPorId() throws Exception {
        Cracha cracha = new Cracha(1L, "Nome Teste", "Cargo Teste", "foto.jpg");

        when(crachaService.buscarCrachaPorId(anyLong())).thenReturn(cracha);

        mockMvc.perform(get("/api/v1/crachas/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.cargo").value("Cargo Teste"))
                .andExpect(jsonPath("$.foto").value("foto.jpg"));
    }

    @Test
    public void testBuscarCrachaPorId_CrachaNaoEncontradoException() throws Exception {
        when(crachaService.buscarCrachaPorId(anyLong())).thenThrow(new CrachaNaoEncontradoException("Crachá não encontrado para a matrícula 1"));

        mockMvc.perform(get("/api/v1/crachas/buscar/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Crachá não encontrado para a matrícula 1"));
    }


    @Test
    public void testAtualizarCracha() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Atualizado", "Cargo Atualizado", new MockMultipartFile("foto", new byte[] {}));
        Cracha crachaAtualizado = new Cracha(1L, "Nome Atualizado", "Cargo Atualizado", "fotoAtualizada.jpg");

        when(crachaService.atualizarCracha(anyLong(), any(CrachaDtoRequest.class))).thenReturn(crachaAtualizado);

        mockMvc.perform(put("/api/v1/crachas/atualizar/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("crachaDtoRequest", crachaDtoRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.cargo").value("Cargo Atualizado"))
                .andExpect(jsonPath("$.foto").value("fotoAtualizada.jpg"));
    }

    @Test//TODO RETORNANDO ERRO
    public void testEmitirCracha_ErroInternoException() throws Exception {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "Cargo Teste", new MockMultipartFile("foto", new byte[] {}));

        when(crachaService.emitirCracha(any(CrachaDtoRequest.class))).thenThrow(new ErroInternoException("Erro interno por favor entre em contato com administrador"));

        mockMvc.perform(post("/api/v1/crachas/emitir")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("crachaDtoRequest", crachaDtoRequest))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro interno por favor entre em contato com administrador"));
    }
}
