package org.example.test_tasks_whiletrue.converter;

import org.example.test_tasks_whiletrue.dto.OrderItemDTO;
import org.example.test_tasks_whiletrue.model.OrderItemModel;

public class OrderItemConverter {

    public static OrderItemDTO toDTO(OrderItemModel item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }

    public static OrderItemModel toEntity(OrderItemDTO dto) {
        OrderItemModel item = new OrderItemModel();
        item.setQuantity(dto.getQuantity());
        return item;
    }
}
