package com.task.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotEmpty(message = "O nome não pode ser nulo")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    String name;

    @NotEmpty(message = "Email não pode ser nulo")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 150, message = "O email deve ter no máximo 150 caracteres")
    String email;
}
