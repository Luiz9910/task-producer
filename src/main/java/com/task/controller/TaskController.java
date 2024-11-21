package com.task.controller;

import com.task.dto.request.TaskDTO;
import com.task.dto.request.TaskUpdateDTO;
import com.task.dto.response.TaskResponseDTO;
import com.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("task")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("tasks")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTask() , HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<String> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        taskService.createTask(taskDTO);
        return new ResponseEntity<>("Tarefa criada com sucesso!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TaskDTO> updateTask(@Valid @RequestBody TaskUpdateDTO taskUpdateDTO) {
        return new ResponseEntity<>(taskService.updateTask(taskUpdateDTO), HttpStatus.OK);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>("Tarefa deletada com sucesso!", HttpStatus.OK);
    }
}
