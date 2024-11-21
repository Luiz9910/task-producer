package com.task.TaskTest.repository;

import com.task.model.Task;
import com.task.model.User;
import com.task.repository.TaskRepository;
import com.task.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class TaskRepositoryTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private Task task1;
    private Task task2;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User(100, "Test User", "testuser@example.com", new Date());
        task1 = new Task(1, "Task 1", "Description 1", "P", new Date(), new Date(), 100);
        task2 = new Task(2, "Task 2", "Description 2", "C", new Date(), new Date(), 100);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(taskRepository.save(any(Task.class))).thenReturn(task1, task2);
    }

    @Test
    void testFindByCdUser() {
        when(taskRepository.findByCdUser(100)).thenReturn(task1);

        Task result = taskRepository.findByCdUser(100);
        assertNotNull(result);
        assertEquals("Task 1", result.getTitle());
    }

    @Test
    void testFindByStatusAndUserId() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        when(taskRepository.findByStatusAndUserId("P", 100)).thenReturn(tasks);

        List<Task> result = taskRepository.findByStatusAndUserId("P", 100);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Task 1", result.getFirst().getTitle());
    }

    @Test
    void testFindByStatus() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        when(taskRepository.findByStatus("P")).thenReturn(tasks);

        List<Task> result = taskRepository.findByStatus("P");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Task 1", result.getFirst().getTitle());
    }

    @Test
    void testFindByUserId() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        when(taskRepository.findByUserId(100)).thenReturn(tasks);

        List<Task> result = taskRepository.findByUserId(100);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindByCdUserNotFound() {
        when(taskRepository.findByCdUser(999)).thenReturn(null);

        Task result = taskRepository.findByCdUser(999);
        assertNull(result);
    }

    @Test
    void testFindByStatusAndUserIdNoResult() {
        List<Task> tasks = new ArrayList<>();
        when(taskRepository.findByStatusAndUserId("A", 100)).thenReturn(tasks);

        List<Task> result = taskRepository.findByStatusAndUserId("A", 100);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByUserIdNoResult() {
        List<Task> tasks = new ArrayList<>();
        when(taskRepository.findByUserId(999)).thenReturn(tasks);

        List<Task> result = taskRepository.findByUserId(999);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
