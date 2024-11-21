package com.task.dto.response;

import com.task.dto.request.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private String message;
    private TaskDTO taskDTO;
}
