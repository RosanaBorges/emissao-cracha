package com.lojas.emissao_cracha.integration;


import com.jayway.jsonpath.JsonPath;
import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import com.lojas.emissao_cracha.service.CrachaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CrachaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CrachaService crachaService;


    @Test
    public void testEmitirCracha() throws Exception {
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", "test image content".getBytes());
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest();
        crachaDtoRequest.setNome("Sheldon Cooper");
        crachaDtoRequest.setCargo("Fisíco Teórico");
        crachaDtoRequest.setFoto(foto);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .file(foto)
                        .param("nome", "Sheldon Cooper")
                        .param("cargo", "Físico Teórico")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Sheldon Cooper"))
                .andExpect(jsonPath("$.cargo").value("Físico Teórico"))
                .andExpect(jsonPath("$.foto").isNotEmpty());
    }

    @Test
    public void testEmitirCrachaSemNome() throws Exception {
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", "test image content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .file(foto)
                        .param("cargo", "Físico Teórico")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEmitirCrachaSemCargo() throws Exception {
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", "test image content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .file(foto)
                        .param("nome", "Sheldon Cooper")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEmitirCrachaSemFoto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .param("nome", "Sheldon Cooper")
                        .param("cargo", "Físico Teórico")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBuscarCrachaPorId() throws Exception {
        // Primeiro, crie um crachá para garantir que ele exista
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", "test image content".getBytes());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .file(foto)
                        .param("nome", "Leonard Hofstadter")
                        .param("cargo", "Físico Experimental")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);

        // Agora, busque o crachá por ID
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/crachas/buscar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Leonard Hofstadter"))
                .andExpect(jsonPath("$.cargo").value("Físico Experimental"));
    }

    @Test
    public void testBuscarCrachaPorIdInvalido() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/crachas/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAtualizarCracha() throws Exception {
        // Primeiro, crie um crachá para garantir que ele exista
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", "test image content".getBytes());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/emitir")
                        .file(foto)
                        .param("nome", "Penny")
                        .param("cargo", "Atendente de Restaurante")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);

        // Agora, atualize o crachá usando PUT
        MockMultipartFile novaFoto = new MockMultipartFile("foto", "nova_foto.jpg", "image/jpeg", "new test image content".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/atualizar/{id}", id)
                        .file(novaFoto)
                        .param("nome", "Penny")
                        .param("cargo", "Representante de Vendas")
                        .with(request -> {
                            request.setMethod("PUT"); // Alterar método para PUT
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Penny"))
                .andExpect(jsonPath("$.cargo").value("Representante de Vendas"))
                .andExpect(jsonPath("$.foto").isNotEmpty());
    }

    @Test
    public void testAtualizarCrachaIdInvalido() throws Exception {
        MockMultipartFile foto = new MockMultipartFile("foto", "nova_foto.jpg", "image/jpeg", "new test image content".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/crachas/atualizar/{id}", 999L)
                        .file(foto)
                        .param("nome", "Howard Wolowitz")
                        .param("cargo", "Engenheiro Aeroespacial")
                        .with(request -> {
                            request.setMethod("PUT"); // Alterar método para PUT
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound());
    }

}
