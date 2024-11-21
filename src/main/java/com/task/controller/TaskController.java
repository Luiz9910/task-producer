package com.task.controller;

import com.task.dto.request.TaskDTO;
import com.task.dto.request.TaskUpdateDTO;
import com.task.dto.response.TaskResponseDTO;
import com.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("tasks")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTask() , HttpStatus.OK);
    }

    @GetMapping("filter")
    public ResponseEntity<List<TaskResponseDTO>> getFilteredTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer userId) {

        List<TaskResponseDTO> tasks = taskService.getFilteredTasks(status, userId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<String> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        taskService.createTask(taskDTO);
        return new ResponseEntity<>("Tarefa criada com sucesso!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TaskDTO> updateTask(@Valid @RequestBody TaskUpdateDTO taskUpdateDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskUpdateDTO));
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Tarefa deletada com sucesso!");
    }
}
