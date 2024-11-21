package com.task.TaskTest.dto.request;

import com.task.dto.request.UserDTO;
import com.task.model.User;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserDTO() {
        UserDTO userDTO = new UserDTO("luizs felipe", "luisadzs2ss@gmail.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // Não deve haver violações de validação
        assertTrue(violations.isEmpty(), "Não deve haver violações de validação.");
    }

    @Test
    void testInvalidName() {
        UserDTO userDTO = new UserDTO(null, "luisadzs2ss@gmail.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o nome.");
        assertEquals(1, violations.size());
        assertEquals("O nome não pode ser nulo", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidEmail() {
        UserDTO userDTO = new UserDTO("luizs felipe", "invalid-email");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o email.");
        assertEquals(1, violations.size());
        assertEquals("Formato de e-mail inválido", violations.iterator().next().getMessage());
    }

    @Test
    void testNameTooLong() {
        String longName = "a".repeat(101);
        UserDTO userDTO = new UserDTO(longName, "luisadzs2ss@gmail.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o nome.");
        assertEquals(1, violations.size());
        assertEquals("O nome deve ter no máximo 100 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void testEmailTooLong() {
        String longEmail = "a".repeat(151) + "@gmail.com";
        UserDTO userDTO = new UserDTO("luizs felipe", longEmail);

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o email.");
        assertEquals(2, violations.size(), "Deve haver apenas uma violação.");
    }

    @Test
    void testDTOToEntityConversionWithAllFields() {
        UserDTO userDTO = new UserDTO("luizs felipe", "luisadzs2ss@gmail.com");

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    void testEntityToDTOConversion() {
        User user = new User();
        user.setName("luizs felipe");
        user.setEmail("luisadzs2ss@gmail.com");

        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());

        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void testValidUserDTOWithAllFields() {
        UserDTO userDTO = new UserDTO("João Silva", "joao.silva@example.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação para dados válidos.");
    }

    @Test
    void testNameEmpty() {
        UserDTO userDTO = new UserDTO(null, "luisadzs2ss@gmail.com");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o nome vazio.");
        assertEquals(1, violations.size());
        assertEquals("O nome não pode ser nulo", violations.iterator().next().getMessage());
    }

    @Test
    void testEmailEmpty() {
        UserDTO userDTO = new UserDTO("João Silva", null);

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o email vazio.");
        assertEquals(1, violations.size());
        assertEquals("Email não pode ser nulo", violations.iterator().next().getMessage());
    }
}
