package com.task.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotNull(message = "O nome não pode ser nulo")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    String name;

    @NotNull(message = "Email não pode ser nulo")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 150, message = "O email deve ter no máximo 150 caracteres")
    String email;
}
