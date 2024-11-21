package com.task.service;

import com.task.dto.request.TaskDTO;
import com.task.exception.NotFoundException;
import com.task.model.Task;
import com.task.repository.TaskRepository;
import com.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {
    @Autowired
    private KafkaTemplate<String, Serializable> kafkaTemplate;

    private final ModelMapper mapper = new ModelMapper();

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    public List<Task> getAllTask() {
        return taskRepository.findAll();
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
}
