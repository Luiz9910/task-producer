package com.task.service;

import com.task.exception.BadRequestException;
import com.task.exception.NotFoundException;
import com.task.model.Task;
import com.task.repository.TaskRepository;
import com.task.dto.request.TaskDTO;
import com.task.dto.request.TaskUpdateDTO;
import com.task.dto.response.TaskResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class TaskService {

    private final KafkaTemplate<String, Serializable> kafkaTemplate;
    private final ModelMapper mapper = new ModelMapper();
    private final TaskRepository taskRepository;
    private final UserService userService;

    public List<TaskResponseDTO> getAllTask() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(task -> mapper.map(task, TaskResponseDTO.class))
                .collect(Collectors.toList());
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        userService.ensureUserExists(taskDTO.getUserId());

        Task task = mapper.map(taskDTO, Task.class);
        task.setCreatedDate(new Date());

        kafkaTemplate.send("taskCreate-topic", task);
        return taskDTO;
    }

    public void deleteTask(Integer taskId) {
        ensureTaskExists(taskId);
        kafkaTemplate.send("taskDelete-topic", taskId);
    }

    public TaskDTO updateTask(TaskUpdateDTO taskUpdateDTO) {
        Task taskExisting = ensureTaskExists(taskUpdateDTO.getId());

        if (taskExisting.getStatus().equals("C")) {
            throw new BadRequestException("Essa tarefa foi concluída por isso não pode ser editada");
        }

        Task task = mapper.map(taskUpdateDTO, Task.class);
        copyMissingFields(task, taskUpdateDTO, taskExisting);
        task.setCreatedDate(taskExisting.getCreatedDate());
        task.setUserId(taskExisting.getUserId());

        kafkaTemplate.send("taskUpdate-topic", task);
        return mapper.map(task, TaskDTO.class);
    }

    public Task ensureTaskExists(Integer taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
    }

    private void copyMissingFields(Task task, TaskUpdateDTO taskUpdateDTO, Task taskExisting) {
        if (taskUpdateDTO.getTitle() == null) {
            task.setTitle(taskExisting.getTitle());
        }

        if (taskUpdateDTO.getDescription() == null) {
            task.setDescription(taskExisting.getDescription());
        }

        if (taskUpdateDTO.getStatus() == null) {
            task.setStatus(taskExisting.getStatus());
        }

        if (taskUpdateDTO.getLimitDate() == null) {
            task.setLimitDate(taskExisting.getLimitDate());
        }
    }

    public List<TaskResponseDTO> getFilteredTasks(String status, Integer userId) {
        List<Task> tasks = findTasksByFilters(status, userId);

        return tasks.stream()
                .map(task -> mapper.map(task, TaskResponseDTO.class))
                .collect(Collectors.toList());
    }

    private List<Task> findTasksByFilters(String status, Integer userId) {
        if (status != null && userId != null) {
            return taskRepository.findByStatusAndUserId(status, userId);
        }

        if (status != null) {
            return taskRepository.findByStatus(status);
        }

        if (userId != null) {
            return taskRepository.findByUserId(userId);
        }

        return taskRepository.findAll();
    }
}
