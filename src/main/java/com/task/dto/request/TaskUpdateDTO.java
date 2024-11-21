package com.task.dto.request;

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
    private String status;
    private Date limitDate;
    private Date createdDate;
    private Integer userId;
}
