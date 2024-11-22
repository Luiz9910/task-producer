package com.task.TaskTest.dto.request;

import com.task.dto.request.TaskDTO;
import com.task.model.Task;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskDTOTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testTaskDTOConstructor() {
        TaskDTO taskDTO = new TaskDTO("Tarefa Exemplo", "Descrição da tarefa.", "P", new Date(), 1);

        assertEquals(1, taskDTO.getUserId());
        assertEquals("Tarefa Exemplo", taskDTO.getTitle());
        assertEquals("Descrição da tarefa.", taskDTO.getDescription());
        assertEquals("P", taskDTO.getStatus());
        assertNotNull(taskDTO.getLimitDate());
    }

    @Test
    void testTaskDTOToModelMapping() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setUserId(1);
        taskDTO.setTitle("Tarefa Exemplo");
        taskDTO.setDescription("Descrição da tarefa.");
        taskDTO.setStatus("P");
        taskDTO.setLimitDate(new Date());

        Task task = new Task();
        task.setUserId(taskDTO.getUserId());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setLimitDate(new Date());
        task.setCreatedDate(new Date());

        assertEquals(taskDTO.getUserId(), task.getUserId());
        assertEquals(taskDTO.getTitle(), task.getTitle());
        assertEquals(taskDTO.getDescription(), task.getDescription());
        assertEquals(taskDTO.getStatus(), task.getStatus());
        assertEquals(taskDTO.getLimitDate(), task.getLimitDate());
        assertNotNull(task.getCreatedDate());
    }

    @Test
    void testValidTaskDTO() {
        TaskDTO taskDTO = new TaskDTO("Tarefa Exemplo", "Descrição da tarefa.", "P", new Date(), 1);

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação.");
    }

    @Test
    void testTitleNull() {
        TaskDTO taskDTO = new TaskDTO(null, "Descrição da tarefa.", "P", new Date(), 1);

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o título.");
        assertEquals(1, violations.size());
        assertEquals("O título não pode ser nulo", violations.iterator().next().getMessage());
    }

    @Test
    void testTitleSize() {
        TaskDTO taskDTO = new TaskDTO("T", "Descrição da tarefa.", "P", new Date(), 1);

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o título.");
        assertEquals(1, violations.size());
        assertEquals("O título deve ter entre 3 e 60 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void testStatusInvalid() {
        TaskDTO taskDTO = new TaskDTO("Tarefa Exemplo", "Descrição da tarefa.", "X", new Date(), 1);

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o status.");
        assertEquals(1, violations.size());
        assertEquals("O status deve ser um dos seguintes: P, C, A", violations.iterator().next().getMessage());
    }

    @Test
    void testUserIdNull() {
        TaskDTO taskDTO = new TaskDTO("Tarefa Exemplo", "Descrição da tarefa.", "P", new Date(), null);

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para o ID do usuário.");
        assertEquals(1, violations.size());
        assertEquals("O ID do usuário não pode ser nulo", violations.iterator().next().getMessage());
    }

//    @Test
//    void testLimitDateNull() {
//        TaskDTO taskDTO = new TaskDTO("Tarefa Exemplo", "Descrição da tarefa.", "P", null, 1);
//
//        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
//
//        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para a data limite.");
//        assertEquals(1, violations.size());
//        assertEquals("A data limite não pode ser nula", violations.iterator().next().getMessage());
//    }

//    @Test
//    void testLimitDateInThePast() {
//        Date pastDate = new Date(System.currentTimeMillis() - 1000000000); // Uma data no passado
//        TaskDTO taskDTO = new TaskDTO("Tarefa Exemplo", "Descrição da tarefa.", "P", pastDate, 1);
//
//        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
//
//        assertFalse(violations.isEmpty(), "Deve haver uma violação de validação para a data limite.");
//        assertEquals(1, violations.size());
//        assertEquals("A data limite não pode ser no passado", violations.iterator().next().getMessage());
//    }

    @Test
    void testDTOToEntityConversionWithAllFields() {
        TaskDTO taskDTO = new TaskDTO("Tarefa Exemplo", "Descrição da tarefa.", "P", new Date(), 1);

        Task task = new Task();
        task.setUserId(taskDTO.getUserId());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setLimitDate(taskDTO.getLimitDate());
        task.setCreatedDate(new Date());

        assertEquals(taskDTO.getUserId(), task.getUserId());
        assertEquals(taskDTO.getTitle(), task.getTitle());
        assertEquals(taskDTO.getDescription(), task.getDescription());
        assertEquals(taskDTO.getStatus(), task.getStatus());
        assertEquals(taskDTO.getLimitDate(), task.getLimitDate());
        assertNotNull(task.getCreatedDate());
    }

    @Test
    void testEntityToDTOConversion() {
        Task task = new Task();
        task.setUserId(1);
        task.setTitle("Tarefa Exemplo");
        task.setDescription("Descrição da tarefa.");
        task.setStatus("P");
        task.setLimitDate(new Date());
        task.setCreatedDate(new Date());

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setUserId(task.getUserId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setLimitDate(task.getLimitDate());

        assertEquals(task.getUserId(), taskDTO.getUserId());
        assertEquals(task.getTitle(), taskDTO.getTitle());
        assertEquals(task.getDescription(), taskDTO.getDescription());
        assertEquals(task.getStatus(), taskDTO.getStatus());
        assertEquals(task.getLimitDate(), taskDTO.getLimitDate());
    }

    @Test
    void testValidStatuses() {
        String[] validStatuses = {"P", "C", "A"};

        for (String status : validStatuses) {
            TaskDTO taskDTO = new TaskDTO("Tarefa Exemplo", "Descrição da tarefa.", status, new Date(), 1);

            Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

            assertTrue(violations.isEmpty(), "Não deve haver violações de validação para o status " + status);
        }
    }

    @Test
    void testTitleExactLength() {
        TaskDTO taskDTO = new TaskDTO("ABC", "Descrição da tarefa.", "P", new Date(), 1);

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação para o título com 3 caracteres.");
    }

    @Test
    void testDescriptionExactLength() {
        TaskDTO taskDTO = new TaskDTO("Tarefa Exemplo", "Des", "P", new Date(), 1);

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        assertTrue(violations.isEmpty(), "Não deve haver violações de validação para a descrição com 3 caracteres.");
    }
}
