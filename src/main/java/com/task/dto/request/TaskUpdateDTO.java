package com.task.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {
    private Integer id;
    private String title;
    private String description;

    @Pattern(regexp = "^(P|C|A)$", message = "O status deve ser um dos seguintes: P, C, A")
    private String status;

    private Date limitDate;
}
