package com.task.TaskTest.dto.response;

import com.task.dto.response.TaskResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskResponseDTOTest {

    @Test
    void testTaskResponseDTO() {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(1);
        taskResponseDTO.setTitle("Task Response");
        taskResponseDTO.setDescription("Description of the task response");
        taskResponseDTO.setStatus("P");
        taskResponseDTO.setCreatedDate(new Date());
        taskResponseDTO.setLimitDate(new Date());
        taskResponseDTO.setUserId(1);

        assertEquals(1, taskResponseDTO.getId());
        assertEquals("Task Response", taskResponseDTO.getTitle());
        assertEquals("Description of the task response", taskResponseDTO.getDescription());
        assertEquals("P", taskResponseDTO.getStatus());
        assertNotNull(taskResponseDTO.getCreatedDate());
        assertNotNull(taskResponseDTO.getLimitDate());
        assertEquals(1, taskResponseDTO.getUserId());
    }
}

