package com.task.TaskTest;

import com.task.dto.request.TaskDTO;
import com.task.dto.request.TaskUpdateDTO;
import com.task.dto.response.TaskResponseDTO;
import com.task.exception.BadRequestException;
import com.task.exception.NotFoundException;
import com.task.model.Task;
import com.task.repository.TaskRepository;
import com.task.service.TaskService;
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

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private KafkaTemplate<String, Serializable> kafkaTemplate;

    @Mock
    private UserService userService;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(kafkaTemplate, taskRepository, userService);
    }

    private Task createTestTask() {
        Task task = new Task();
        task.setUserId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus("P");
        task.setLimitDate(new Date());
        task.setCreatedDate(new Date());
        return task;
    }

    @Test
    void testGetAllTask() {
        Task task = createTestTask();
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskResponseDTO> result = taskService.getAllTask();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task.getTitle(), result.get(0).getTitle());
        assertEquals(task.getDescription(), result.get(0).getDescription());
    }

    @Test
    void testCreateTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setUserId(1);
        taskDTO.setTitle("New Task");
        taskDTO.setDescription("Finalizar o relatório de vendas do mês para a reunião de amanhã");
        taskDTO.setStatus("P");
        taskDTO.setLimitDate(new Date());

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setLimitDate(taskDTO.getLimitDate());

        when(userService.ensureUserExists(1)).thenReturn(null);

        TaskDTO result = taskService.createTask(taskDTO);

        assertNotNull(result);
        verify(kafkaTemplate).send(eq("taskCreate-topic"), any(Task.class));

        assertEquals(taskDTO.getTitle(), result.getTitle());
        assertEquals(taskDTO.getDescription(), result.getDescription());
    }

    @Test
    void testDeleteTask() {
        Task task = new Task();
        task.setId(1);

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        taskService.deleteTask(1);

        verify(kafkaTemplate).send("taskDelete-topic", 1);
    }

    @Test
    void testDeleteTask_TaskNotFound() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            taskService.deleteTask(1);
        });

        assertEquals("Tarefa não encontrada", exception.getMessage());
    }

    @Test
    void testUpdateTask() {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setId(1);
        taskUpdateDTO.setTitle("Updated Task");

        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setTitle("Old Task");
        existingTask.setStatus("P");

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));

        TaskDTO result = taskService.updateTask(taskUpdateDTO);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        verify(kafkaTemplate).send(eq("taskUpdate-topic"), any(Task.class));
    }

    @Test
    void testUpdateTask_TaskAlreadyCompleted() {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setId(1);
        taskUpdateDTO.setTitle("Updated Task");

        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setTitle("Old Task");
        existingTask.setStatus("C");

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            taskService.updateTask(taskUpdateDTO);
        });
        assertEquals("Essa tarefa foi concluída por isso não pode ser editada", exception.getMessage());
    }

    @Test
    void testUpdateTask_TaskNotFound() {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setId(1);
        taskUpdateDTO.setTitle("Updated Task");

        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            taskService.updateTask(taskUpdateDTO);
        });

        assertEquals("Tarefa não encontrada", exception.getMessage());
    }

    @Test
    void testGetFilteredTasks() {
        Task task = createTestTask();
        task.setTitle("Filtered Task");

        when(taskRepository.findByStatusAndUserId("P", 1)).thenReturn(List.of(task));
        List<TaskResponseDTO> result = taskService.getFilteredTasks("P", 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Filtered Task", result.get(0).getTitle());
    }

    @Test
    void testGetFilteredTasks_StatusNotFound() {
        when(taskRepository.findByStatusAndUserId("P", 1)).thenReturn(List.of());
        List<TaskResponseDTO> result = taskService.getFilteredTasks("P", 1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetFilteredTasks_NoTasksFound() {
        when(taskRepository.findByStatusAndUserId("P", 1)).thenReturn(List.of());
        List<TaskResponseDTO> result = taskService.getFilteredTasks("P", 1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetFilteredTasks_NoFilter() {
        Task task = createTestTask();
        task.setTitle("Filtered Task");

        when(taskRepository.findAll()).thenReturn(List.of(task));
        List<TaskResponseDTO> result = taskService.getFilteredTasks(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Filtered Task", result.getFirst().getTitle());
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
    void testModelToTaskDTOConversion() {
        Task task = new Task();
        task.setUserId(1);
        task.setTitle("Tarefa Modelo");
        task.setDescription("Descrição da tarefa no modelo.");
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
    void testTaskDTOConstructor() {
        TaskDTO taskDTO = new TaskDTO("Tarefa Exemplo", "Descrição da tarefa.", "P", new Date(), 1);

        assertEquals(1, taskDTO.getUserId());
        assertEquals("Tarefa Exemplo", taskDTO.getTitle());
        assertEquals("Descrição da tarefa.", taskDTO.getDescription());
        assertEquals("P", taskDTO.getStatus());
        assertNotNull(taskDTO.getLimitDate());
    }

    @Test
    void testModelToTaskDTOConstructor() {
        Task task = new Task();
        task.setUserId(1);
        task.setTitle("Tarefa Modelo");
        task.setDescription("Descrição da tarefa no modelo.");
        task.setStatus("P");
        task.setLimitDate(new Date());
        task.setCreatedDate(new Date());

        TaskDTO taskDTO = new TaskDTO(task.getTitle(), task.getDescription(), task.getStatus(), task.getLimitDate(), task.getUserId());

        assertEquals(task.getUserId(), taskDTO.getUserId());
        assertEquals(task.getTitle(), taskDTO.getTitle());
        assertEquals(task.getDescription(), taskDTO.getDescription());
        assertEquals(task.getStatus(), taskDTO.getStatus());
        assertEquals(task.getLimitDate(), taskDTO.getLimitDate());
    }
}
