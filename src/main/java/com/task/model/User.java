package com.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_users", schema = "DBATK")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "DBATK.seq_users", allocationSize = 1)
    @Column(name = "cd_user")
    private Integer id;

    @Column(name = "nm_user", nullable = false, length = 100)
    private String name;

    @Column(name = "ds_email", nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "dt_created", nullable = false)
    private Date createdDate;
}
