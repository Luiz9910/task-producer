package com.task.exception;

import com.task.exception.message.ApiErrorMessage;
import com.task.exception.message.ApiErrorMessages;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorMessages handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        return new ApiErrorMessages(
                Instant.now(),
                "Validação falhou",
                Collections.singletonList(String.join(", ", errors)),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorMessage handleBadRequestException(BadRequestException e, HttpServletRequest request) {
        String messageError = e.getMessage();
        return new ApiErrorMessage(Instant.now(), "Ocorreu um erro", messageError, request.getRequestURI());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorMessage handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        String messageError = e.getMessage();
        return new ApiErrorMessage(Instant.now(), "Ocorreu um erro", messageError, request.getRequestURI());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorMessage handleConflictException(ConflictException e, HttpServletRequest request) {
        String messageError = e.getMessage();
        return new ApiErrorMessage(Instant.now(), "Ocorreu um erro", messageError, request.getRequestURI());
    }
}
