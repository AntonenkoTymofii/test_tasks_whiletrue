package org.example.test_tasks_whiletrue.converter;

import org.example.test_tasks_whiletrue.dto.OrderDTO;
import org.example.test_tasks_whiletrue.exception.userExceptions.UserNotFoundException;
import org.example.test_tasks_whiletrue.model.OrderModel;
import org.example.test_tasks_whiletrue.model.UserModel;
import org.example.test_tasks_whiletrue.service.UserService;

import java.util.stream.Collectors;

public class OrderConverter {

    public static OrderDTO toDTO(OrderModel order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setDate(order.getDate());
        dto.setTotalPrice(order.calculateTotalPrice());
        dto.setItems(order.getItems().stream()
                .map(OrderItemConverter::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public static OrderModel toEntity(OrderDTO orderDTO, UserService userService) throws UserNotFoundException {
        OrderModel order = new OrderModel();
        order.setId(orderDTO.getId());
        order.setDate(orderDTO.getDate());

        UserModel user = UserConverter.toEntity(userService.getUserById(orderDTO.getUserId()));
        order.setUser(user);

        order.setItems(orderDTO.getItems().stream()
                .map(OrderItemConverter::toEntity)
                .collect(Collectors.toSet()));

        order.calculateTotalPrice();

        return order;
    }
}
