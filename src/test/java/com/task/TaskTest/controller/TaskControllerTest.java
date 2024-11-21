package com.task.TaskTest.controller;

import com.task.controller.TaskController;
import com.task.dto.request.TaskDTO;
import com.task.dto.request.TaskUpdateDTO;
import com.task.dto.response.TaskResponseDTO;
import com.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private TaskDTO taskDTO;
    private TaskUpdateDTO taskUpdateDTO;
    private TaskResponseDTO taskResponseDTO;

    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO("Test Task", "Test description", "P", new Date(), 1);
        taskUpdateDTO = new TaskUpdateDTO(1, "Updated Task", "Updated description", "C", null);
        taskResponseDTO = new TaskResponseDTO(1, "Test Task", "Test description", "P", null, null, 1);
    }

    @Test
    void testGetAllTasks() throws Exception {
        List<TaskResponseDTO> taskList = new ArrayList<>();
        taskList.add(taskResponseDTO);

        when(taskService.getAllTask()).thenReturn(taskList);

        mockMvc.perform(get("/task/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void testCreateTask() throws Exception {
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(taskDTO);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Test Task\", \"description\": \"Test description\", \"status\": \"P\", \"userId\": 1 }"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Tarefa criada com sucesso!"));
    }


    @Test
    void testUpdateTask() throws Exception {
        when(taskService.updateTask(taskUpdateDTO)).thenReturn(taskDTO);

        mockMvc.perform(put("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"title\": \"Updated Task\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1);

        mockMvc.perform(delete("/task/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tarefa deletada com sucesso!"));
    }

    @Test
    void testGetFilteredTasks() throws Exception {
        List<TaskResponseDTO> filteredTasks = new ArrayList<>();
        filteredTasks.add(taskResponseDTO);

        when(taskService.getFilteredTasks("P", 1)).thenReturn(filteredTasks);

        mockMvc.perform(get("/task/filter?status=P&userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }
}
