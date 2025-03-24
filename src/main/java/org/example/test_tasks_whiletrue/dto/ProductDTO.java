package org.example.test_tasks_whiletrue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Назва товару не може бути пустою")
    private String name;

    @NotNull(message = "Ціна товару не може бути пустою")
    @Positive(message = "Ціна товару повинна бути більше нуля")
    private Float price;
}