package com.task.exception.message;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorMessage {
    private Instant timestamp;
    private String error;
    private String messagem;
    private String path;
}