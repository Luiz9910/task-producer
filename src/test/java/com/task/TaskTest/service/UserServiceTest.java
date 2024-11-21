package com.task.TaskTest.service;

import com.task.dto.request.UserDTO;
import com.task.dto.request.UserUpdateDTO;
import com.task.exception.BadRequestException;
import com.task.exception.ConflictException;
import com.task.exception.NotFoundException;
import com.task.model.Task;
import com.task.model.User;
import com.task.repository.TaskRepository;
import com.task.repository.UserRepository;
import com.task.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaTemplate<String, Serializable> kafkaTemplate;

    @Mock
    private TaskRepository taskRepository;  // Certificando que taskRepository está mockado

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(kafkaTemplate, userRepository, taskRepository);  // Passando o taskRepository
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        user.setCreatedDate(new Date());
        return user;
    }

    @Test
    void testGetAllUsers() {
        User user = createTestUser();
        when(userRepository.findAll()).thenReturn(List.of(user));
    }


    @Test
    void testCreateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("New User");
        userDTO.setEmail("newuser@example.com");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(null);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        verify(kafkaTemplate).send(eq("userCreate-topic"), any(User.class));
        assertEquals(userDTO.getName(), result.getName());
        assertEquals(userDTO.getEmail(), result.getEmail());
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("New User");
        userDTO.setEmail("existinguser@example.com");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(createTestUser());

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            userService.createUser(userDTO);
        });

        assertEquals("Já existe um usuário com esse email", exception.getMessage());
    }

    @Test
    void testDeleteUser() {
        User user = createTestUser();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        when(taskRepository.findByCdUser(1)).thenReturn(null);

        userService.deleteUser(1);

        verify(kafkaTemplate).send("userDelete-topic", 1);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.deleteUser(1);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void testDeleteUser_UserHasAssignedTasks() {
        User user = createTestUser();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        List<Task> tarefas = List.of(new Task());
        when(taskRepository.findByCdUser(1)).thenReturn(null);
    }

    @Test
    void testUpdateUser() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setId(1);
        userUpdateDTO.setName("Updated User");
        userUpdateDTO.setEmail("updateduser@example.com");

        User existingUser = createTestUser();
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(userUpdateDTO.getEmail())).thenReturn(null);

        UserDTO result = userService.updateUser(userUpdateDTO);

        assertNotNull(result);
        assertEquals(userUpdateDTO.getName(), result.getName());
        assertEquals(userUpdateDTO.getEmail(), result.getEmail());
        verify(kafkaTemplate).send(eq("userUpdate-topic"), any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setId(1);
        userUpdateDTO.setName("Updated User");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.updateUser(userUpdateDTO);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void testUpdateUser_EmailAlreadyExists() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setId(1);
        userUpdateDTO.setEmail("existinguser@example.com");

        User existingUser = createTestUser();
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(userUpdateDTO.getEmail())).thenReturn(createTestUser());

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            userService.updateUser(userUpdateDTO);
        });

        assertEquals("Já existe um usuário com esse email", exception.getMessage());
    }
}
