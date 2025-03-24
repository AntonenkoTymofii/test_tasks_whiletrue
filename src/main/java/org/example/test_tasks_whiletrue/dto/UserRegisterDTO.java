package org.example.test_tasks_whiletrue.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO extends UserDTO {
    @NotNull(message = "ID користувача не може бути пустим")
    private Long id;

    @NotBlank(message = "Пароль обов'язковий")
    @Size(min = 8, message = "Пароль повинен містити щонайменше 8 символів")
    private String password;

    @NotBlank(message = "Ім'я обов'язкове")
    @Size(max = 50, message = "Ім'я не може перевищувати 50 символів")
    private String firstname;

    @NotBlank(message = "Прізвище обов'язкове")
    @Size(max = 50, message = "Прізвище не може перевищувати 50 символів")
    private String lastname;

    @NotBlank(message = "Email обов'язковий")
    @Email(message = "Невірний формат email")
    private String email;

    @Size(min = 10, max = 15, message = "Телефон повинен містити від 10 до 15 символів")
    private String phone;

}