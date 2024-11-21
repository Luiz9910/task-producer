package com.task.TaskTest.repository;

import com.task.model.User;
import com.task.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1, "Test User", "testuser@example.com", null);

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        when(userRepository.findByEmail("testuser@example.com")).thenReturn(testUser);
    }

    @Test
    void testSaveUser() {
        User savedUser = userRepository.save(testUser);

        assertNotNull(savedUser);
        assertEquals(testUser.getId(), savedUser.getId());
        assertEquals("Test User", savedUser.getName());
        assertEquals("testuser@example.com", savedUser.getEmail());
    }

    @Test
    void testFindByEmail() {
        User foundUser = userRepository.findByEmail("testuser@example.com");

        assertNotNull(foundUser);
        assertEquals("Test User", foundUser.getName());
        assertEquals("testuser@example.com", foundUser.getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        User foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertNull(foundUser);
    }

    @Test
    void testSaveUserWithNullEmail() {
        User userWithNullEmail = new User(2, "Null Email User", null, null);

        when(userRepository.save(userWithNullEmail)).thenReturn(userWithNullEmail);

        User savedUser = userRepository.save(userWithNullEmail);

        assertNotNull(savedUser);
        assertNull(savedUser.getEmail());
    }

    @Test
    void testFindByEmailWithMockitoVerification() {
        userRepository.findByEmail("testuser@example.com");

        verify(userRepository, times(1)).findByEmail("testuser@example.com");
    }
}
