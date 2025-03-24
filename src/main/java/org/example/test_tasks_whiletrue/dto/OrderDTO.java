package org.example.test_tasks_whiletrue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private LocalDate date;
    private Long userId;
    private String userEmail;
    private List<OrderItemDTO> items;
    private Float totalPrice;
}