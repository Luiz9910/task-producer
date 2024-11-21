package com.task.TaskTest.model;

import com.task.model.Task;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testTaskDefaultConstructor() {
        Task task = new Task();

        assertNull(task.getId());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertNull(task.getStatus());
        assertNull(task.getCreatedDate());
        assertNull(task.getLimitDate());
        assertNull(task.getUserId());
    }

    @Test
    void testTaskParameterizedConstructor() {
        Task task = new Task(1, "Test Title", "Test Description", "P", new Date(), new Date(), 123);

        assertEquals(1, task.getId());
        assertEquals("Test Title", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals("P", task.getStatus());
        assertNotNull(task.getCreatedDate());
        assertNotNull(task.getLimitDate());
        assertEquals(123, task.getUserId());
    }

    @Test
    void testTaskGettersAndSetters() {
        Task task = new Task();

        task.setId(1);
        task.setTitle("New Task Title");
        task.setDescription("New Task Description");
        task.setStatus("A");
        task.setCreatedDate(new Date());
        task.setLimitDate(new Date());
        task.setUserId(2);

        assertEquals(1, task.getId());
        assertEquals("New Task Title", task.getTitle());
        assertEquals("New Task Description", task.getDescription());
        assertEquals("A", task.getStatus());
        assertNotNull(task.getCreatedDate());
        assertNotNull(task.getLimitDate());
        assertEquals(2, task.getUserId());
    }

    @Test
    void testTaskSettersAndGettersNullValues() {
        Task task = new Task();

        task.setId(null);
        task.setTitle(null);
        task.setDescription(null);
        task.setStatus(null);
        task.setCreatedDate(null);
        task.setLimitDate(null);
        task.setUserId(null);

        assertNull(task.getId());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertNull(task.getStatus());
        assertNull(task.getCreatedDate());
        assertNull(task.getLimitDate());
        assertNull(task.getUserId());
    }

    @Test
    void testTaskWithBuilder() {
        Task task = new Task(1, "Test Title", "Test Description", "P", new Date(), new Date(), 123);

        assertEquals(1, task.getId());
        assertEquals("Test Title", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals("P", task.getStatus());
        assertNotNull(task.getCreatedDate());
        assertNotNull(task.getLimitDate());
        assertEquals(123, task.getUserId());
    }
}
