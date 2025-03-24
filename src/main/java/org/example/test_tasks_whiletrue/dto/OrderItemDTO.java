package org.example.test_tasks_whiletrue.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;

    @NotNull(message = "ID товару не може бути пустим")
    private Long productId;

    private String productName;
    private Float productPrice;

    @NotNull(message = "Кількість товару не може бути пустою")
    @Min(value = 1, message = "Кількість товару повинна бути більше нуля")
    private Integer quantity;

    private Float totalPrice;
}