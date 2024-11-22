package com.task.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_tasks", schema = "DBATK")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "DBATK.seq_tasks", allocationSize = 1)
    @Column(name = "cd_task")
    private Integer id;

    @Column(name = "nm_title", nullable = false, length = 60)
    private String title;

    @Column(name = "ds_task", nullable = false, length = 300)
    private String description;

    @Column(name = "tp_status", nullable = false, length = 1)
    private String status;

    @Column(name = "dt_created", nullable = false)
    private Date createdDate;

    @Column(name = "dt_limit")
    private Date limitDate;

    @Column(name = "cd_user")
    private Integer userId;
}