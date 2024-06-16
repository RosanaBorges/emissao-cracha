package com.lojas.emissao_cracha.unit.domain;

import com.lojas.emissao_cracha.dto.CrachaDtoRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CrachaTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCrachaDtoRequestValido() {
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "fotoConteudo".getBytes());
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "Cargo Teste", foto);
        Set<ConstraintViolation<CrachaDtoRequest>> violations = validator.validate(crachaDtoRequest);
        assertTrue(violations.isEmpty(), "Esperado que não haja violações");
    }

    @Test
    public void testNomeNull() {
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "fotoConteudo".getBytes());
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest(null, "Cargo Teste", foto);
        Set<ConstraintViolation<CrachaDtoRequest>> violations = validator.validate(crachaDtoRequest);
        assertEquals(2, violations.size(), "Esperado que haja uma violação");
        assertEquals("Por favor escreva o nome que deseja ser chamado.", violations.iterator().next().getMessage());
    }

    @Test
    public void testNomeVazio() {
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "fotoConteudo".getBytes());
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("", "Cargo Teste", foto);
        Set<ConstraintViolation<CrachaDtoRequest>> violations = validator.validate(crachaDtoRequest);
        assertEquals(1, violations.size(), "Esperado que haja uma violação");
        assertEquals("Por favor escreva o nome que deseja ser chamado.", violations.iterator().next().getMessage());
    }

    @Test
    public void testNomeApenasComEspacos() {
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "fotoConteudo".getBytes());
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("   ", "Cargo Teste", foto);
        Set<ConstraintViolation<CrachaDtoRequest>> violations = validator.validate(crachaDtoRequest);
        assertEquals(1, violations.size(), "Esperado que haja uma violação");
        assertEquals("Por favor escreva o nome que deseja ser chamado.", violations.iterator().next().getMessage());
    }

    @Test
    public void testCargoNull() {
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "fotoConteudo".getBytes());
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", null, foto);
        Set<ConstraintViolation<CrachaDtoRequest>> violations = validator.validate(crachaDtoRequest);
        assertEquals(2, violations.size(), "Esperado que haja uma violação");
        assertEquals("Por favor insira o seu cargo.", violations.iterator().next().getMessage());
    }

    @Test
    public void testCargoVazio() {
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "fotoConteudo".getBytes());
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "", foto);
        Set<ConstraintViolation<CrachaDtoRequest>> violations = validator.validate(crachaDtoRequest);
        assertEquals(1, violations.size(), "Esperado que haja uma violação");
        assertEquals("Por favor insira o seu cargo.", violations.iterator().next().getMessage());
    }

    @Test
    public void testCargoApenasComEspacos() {
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE, "fotoConteudo".getBytes());
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "   ", foto);
        Set<ConstraintViolation<CrachaDtoRequest>> violations = validator.validate(crachaDtoRequest);
        assertEquals(1, violations.size(), "Esperado que haja uma violação");
        assertEquals("Por favor insira o seu cargo.", violations.iterator().next().getMessage());
    }

    @Test
    public void testFotoNull() {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("Nome Teste", "Cargo Teste", null);
        Set<ConstraintViolation<CrachaDtoRequest>> violations = validator.validate(crachaDtoRequest);
        assertEquals(1, violations.size(), "Esperado que haja uma violação");
        assertEquals("Por favor insira a foto.", violations.iterator().next().getMessage());
    }

    @Test
    public void testMultiplosCamposInvalidos() {
        CrachaDtoRequest crachaDtoRequest = new CrachaDtoRequest("", "", null);
        Set<ConstraintViolation<CrachaDtoRequest>> violations = validator.validate(crachaDtoRequest);
        assertEquals(3, violations.size(), "Esperado que haja três violações");

        for (ConstraintViolation<CrachaDtoRequest> violation : violations) {
            String message = violation.getMessage();
            assertTrue(
                    message.equals("Por favor escreva o nome que deseja ser chamado.") ||
                            message.equals("Por favor insira o seu cargo.") ||
                            message.equals("Por favor insira a foto."),
                    "Mensagem de erro inesperada: " + message
            );
        }
    }
}
