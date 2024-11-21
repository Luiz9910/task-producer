package com.task.TaskTest.controller;

import com.task.controller.UserController;
import com.task.dto.request.UserDTO;
import com.task.dto.request.UserUpdateDTO;
import com.task.dto.response.UserResponseDTO;
import com.task.exception.ConflictException;
import com.task.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDTO userDTO;
    private UserUpdateDTO userUpdateDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setName("Test User");
        userDTO.setEmail("testuser@example.com");

        userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setId(1);
        userUpdateDTO.setName("Updated User");
        userUpdateDTO.setEmail("updateduser@example.com");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1);
        userResponseDTO.setName("Test User");
        userResponseDTO.setEmail("testuser@example.com");
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserResponseDTO> userList = new ArrayList<>();
        userList.add(userResponseDTO);

        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/user/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO("Test User", "testuser@example.com");

        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Test User\", \"email\": \"testuser@example.com\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{ \"name\": \"Test User\", \"email\": \"testuser@example.com\" }"));  // Expect the created userDTO JSON
    }


    @Test
    void testUpdateUser() throws Exception {
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setName("Updated User");
        updatedUserDTO.setEmail("updateduser@example.com");

        when(userService.updateUser(any(UserUpdateDTO.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"name\": \"Updated User\", \"email\": \"updateduser@example.com\" }"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"name\": \"Updated User\", \"email\": \"updateduser@example.com\" }"));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateUser_EmailAlreadyExists() throws Exception {
        when(userService.createUser(any(UserDTO.class)))
                .thenThrow(new ConflictException("Já existe um usuário com esse email"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Test User\", \"email\": \"existinguser@example.com\" }"))
                .andExpect(status().isConflict());
    }

}
