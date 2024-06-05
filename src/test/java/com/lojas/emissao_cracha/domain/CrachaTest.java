package com.lojas.emissao_cracha.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void testCrachaValido() {
        Cracha cracha = new Cracha(1L, "Nome Teste", "Cargo Teste", "foto.jpg");
        Set<ConstraintViolation<Cracha>> violations = validator.validate(cracha);
        assertTrue(violations.isEmpty(), "Esperado que não haja violações");
    }

    @Test
    public void testNomeNull() {
        Cracha cracha = new Cracha(1L, null, "Cargo Teste", "foto.jpg");
        Set<ConstraintViolation<Cracha>> violations = validator.validate(cracha);
        assertEquals(1, violations.size(), "Esperado que haja uma violação");
        assertEquals("Por favor escreva o nome que deseja ser chamado.", violations.iterator().next().getMessage());
    }

    @Test
    public void testCargoNull() {
        Cracha cracha = new Cracha(1L, "Nome Teste", null, "foto.jpg");
        Set<ConstraintViolation<Cracha>> violations = validator.validate(cracha);
        assertEquals(1, violations.size(), "Esperado que haja uma violação");
        assertEquals("Por favor insira o seu cargo. ", violations.iterator().next().getMessage());
    }

    @Test
    public void testFotoNull() {
        Cracha cracha = new Cracha(1L, "Nome Teste", "Cargo Teste", null);
        Set<ConstraintViolation<Cracha>> violations = validator.validate(cracha);
        assertEquals(1, violations.size(), "Esperado que haja uma violação");
        assertEquals("Por favor insira a foto.", violations.iterator().next().getMessage());
    }

    @Test
    public void testMultiplosCamposNulos() {
        Cracha cracha = new Cracha(1L, null, null, null);
        Set<ConstraintViolation<Cracha>> violations = validator.validate(cracha);
        assertEquals(3, violations.size(), "Esperado que haja três violações");

        for (ConstraintViolation<Cracha> violation : violations) {
            String message = violation.getMessage();
            assertTrue(
                    message.equals("Por favor escreva o nome que deseja ser chamado.") ||
                            message.equals("Por favor insira o seu cargo. ") ||
                            message.equals("Por favor insira a foto."),
                    "Mensagem de erro inesperada: " + message
            );
        }
    }
}
