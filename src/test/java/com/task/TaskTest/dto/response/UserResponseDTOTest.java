package com.task.TaskTest.dto.response;

import com.task.dto.response.UserResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UserResponseDTOTest {
    @Test
    void testUserResponseDTOGettersAndSetters() {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        Integer id = 1;
        String name = "João Silva";
        String email = "joao.silva@example.com";
        Date createdDate = new Date();

        userResponseDTO.setId(id);
        userResponseDTO.setName(name);
        userResponseDTO.setEmail(email);
        userResponseDTO.setCreatedDate(createdDate);

        assertEquals(id, userResponseDTO.getId());
        assertEquals(name, userResponseDTO.getName());
        assertEquals(email, userResponseDTO.getEmail());
        assertEquals(createdDate, userResponseDTO.getCreatedDate());
    }

    @Test
    void testUserResponseDTOAllArgsConstructor() {
        Date createdDate = new Date();
        UserResponseDTO userResponseDTO = new UserResponseDTO(1, "João Silva", "joao.silva@example.com", createdDate);

        assertEquals(1, userResponseDTO.getId());
        assertEquals("João Silva", userResponseDTO.getName());
        assertEquals("joao.silva@example.com", userResponseDTO.getEmail());
        assertEquals(createdDate, userResponseDTO.getCreatedDate());
    }

    @Test
    void testUserResponseDTONoArgsConstructor() {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        assertNull(userResponseDTO.getId());
        assertNull(userResponseDTO.getName());
        assertNull(userResponseDTO.getEmail());
        assertNull(userResponseDTO.getCreatedDate());
    }
}
