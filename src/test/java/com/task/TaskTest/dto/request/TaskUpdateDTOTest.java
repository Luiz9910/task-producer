package com.task.TaskTest.dto.request;

import com.task.dto.request.TaskUpdateDTO;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskUpdateDTOTest {

    @Test
    void testTaskUpdateDTO() {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setId(1);
        taskUpdateDTO.setTitle("Updated Task");
        taskUpdateDTO.setDescription("Updated description of the task.");
        taskUpdateDTO.setStatus("P");
        taskUpdateDTO.setLimitDate(new Date());

        assertEquals(1, taskUpdateDTO.getId());
        assertEquals("Updated Task", taskUpdateDTO.getTitle());
        assertEquals("Updated description of the task.", taskUpdateDTO.getDescription());
        assertEquals("P", taskUpdateDTO.getStatus());
        assertNotNull(taskUpdateDTO.getLimitDate());
    }
}