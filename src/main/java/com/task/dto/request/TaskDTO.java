package com.task.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    @NotNull(message = "O título não pode ser nulo")
    @Size(min = 3, max = 60, message = "O título deve ter entre 3 e 60 caracteres")
    private String title;

    @NotNull(message = "A descrição não pode ser nula")
    @Size(min = 3, max = 300, message = "A descrição deve ter entre 3 e 300 caracteres")
    private String description;

    @NotNull(message = "O status não pode ser nulo")
    @Pattern(regexp = "^(P|C|A)$", message = "O status deve ser um dos seguintes: P, C, A")
    private String status;

    // private Date limitDate;

    @NotNull(message = "O ID do usuário não pode ser nulo")
    private Integer userId;
}
