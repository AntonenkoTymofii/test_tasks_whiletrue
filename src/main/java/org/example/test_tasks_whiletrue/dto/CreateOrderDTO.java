package org.example.test_tasks_whiletrue.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {
    @NotEmpty(message = "Список товарів не може бути пустим")
    @Valid
    private List<OrderItemDTO> items;
}