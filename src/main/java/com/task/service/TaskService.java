package com.task.service;

import com.task.dto.request.TaskDTO;
import com.task.dto.request.TaskUpdateDTO;
import com.task.dto.response.TaskResponseDTO;
import com.task.exception.NotFoundException;
import com.task.model.Task;
import com.task.repository.TaskRepository;
import com.task.repository.UserRepository;
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

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    public List<TaskResponseDTO> getAllTask() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(task -> mapper.map(task, TaskResponseDTO.class))
                .collect(Collectors.toList());
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Task task = mapper.map(taskDTO, Task.class);
        task.setCreatedDate(new Date());

        kafkaTemplate.send("taskCreate-topic", task);
        return taskDTO;
    }

    public void deleteTask(Integer taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrado para deletar"));

        kafkaTemplate.send("taskDelete-topic", taskId);
    }

    public TaskDTO updateTask(TaskUpdateDTO taskUpdateDTO) {
        taskRepository.findById(taskUpdateDTO.getId())
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrado para atualizar"));

        Task task = mapper.map(taskUpdateDTO, Task.class);
        kafkaTemplate.send("taskUpdate-topic", task);

        return mapper.map(taskUpdateDTO, TaskDTO.class);
    }
}
