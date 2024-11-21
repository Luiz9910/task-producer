package com.task.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorMessages {
    private Instant timestamp;
    private String error;
    private List<String> messagem;
    private String path;
}