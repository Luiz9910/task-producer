package com.task.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private Date createdDate;
    private Date limitDate;
    private Integer userId;
}
