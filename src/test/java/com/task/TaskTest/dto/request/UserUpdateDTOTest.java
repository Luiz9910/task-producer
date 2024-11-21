package com.task.TaskTest.dto.request;

import com.task.dto.request.UserUpdateDTO;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserUpdateDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserUpdateDTO() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(1, "João Silva", "joao.silva@example.com");

        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(userUpdateDTO);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação.");
    }

    // Teste: nome vazio
    @Test
    void testNameEmpty() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(1, "", "joao.silva@example.com");

        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(userUpdateDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o nome vazio.");
        assertEquals(1, violations.size());
    }

    @Test
    void testEmailEmpty() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(1, "João Silva", "");

        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(userUpdateDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o e-mail vazio.");
        assertEquals(1, violations.size());
    }

    @Test
    void testInvalidEmail() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(1, "João Silva", "invalid-email");

        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(userUpdateDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o e-mail inválido.");
        assertEquals(1, violations.size());
        assertEquals("must be a well-formed email address", violations.iterator().next().getMessage());
    }

    @Test
    void testValidEmail() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(1, "João Silva", "joao.silva@example.com");

        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(userUpdateDTO);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação para o e-mail válido.");
    }

    @Test
    void testIdNull() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(null, "João Silva", "joao.silva@example.com");

        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(userUpdateDTO);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação para o ID nulo.");
    }

    @Test
    void testValidId() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(1, "João Silva", "joao.silva@example.com");

        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(userUpdateDTO);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação para o ID válido.");
    }
}
