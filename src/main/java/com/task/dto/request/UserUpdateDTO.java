package com.task.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private Integer id;
    private String name;

    @Email(message = "Formato de e-mail inválido")
    @Size(max = 150, message = "O email deve ter no máximo 150 caracteres")
    private String email;
}
