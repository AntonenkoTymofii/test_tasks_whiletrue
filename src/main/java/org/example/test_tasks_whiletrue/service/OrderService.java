package org.example.test_tasks_whiletrue.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_tasks_whiletrue.converter.UserConverter;
import org.example.test_tasks_whiletrue.dto.CreateOrderDTO;
import org.example.test_tasks_whiletrue.dto.OrderItemDTO;
import org.example.test_tasks_whiletrue.exception.orderException.OrderNotFoundException;
import org.example.test_tasks_whiletrue.exception.productExceptions.ProductNotFoundException;
import org.example.test_tasks_whiletrue.exception.userExceptions.UserNotFoundException;
import org.example.test_tasks_whiletrue.model.OrderItemModel;
import org.example.test_tasks_whiletrue.model.OrderModel;
import org.example.test_tasks_whiletrue.model.ProductModel;
import org.example.test_tasks_whiletrue.model.UserModel;
import org.example.test_tasks_whiletrue.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final ProductService productService;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    public OrderService(OrderRepo orderRepo, ProductService productService, @Lazy UserService userService) {
        this.orderRepo = orderRepo;
        this.productService = productService;
        this.userService = userService;
    }

    @Transactional
    public OrderModel createOrder(Long userId, CreateOrderDTO orderDTO) throws UserNotFoundException, ProductNotFoundException {
        UserModel user = UserConverter.toEntity(userService.getUserById(userId));

        OrderModel order = new OrderModel();
        order.setUser(user);

        order = orderRepo.save(order);

        Set<OrderItemModel> items = new HashSet<>();

        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            ProductModel product = productService.getProductById(itemDTO.getProductId());

            OrderItemModel item = new OrderItemModel();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.calculateTotalPrice();

            items.add(item);
        }

        order.setItems(items);
        OrderModel savedOrder = orderRepo.save(order);
        log.info("Order created: {}", savedOrder.getId());
        return savedOrder;
    }

    public OrderModel getOrderById(Long id) throws OrderNotFoundException {
        return orderRepo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Замовлення не знайдено з ID: " + id));
    }

    public List<OrderModel> getOrdersByUser(Long userId) throws UserNotFoundException {
        UserModel user = UserConverter.toEntity(userService.getUserById(userId));
        return orderRepo.findByUser(user);
    }

    @Transactional
    public void cancelOrder(Long id) throws OrderNotFoundException {
        OrderModel order = getOrderById(id);
        orderRepo.delete(order);
        log.info("Order cancelled: {}", id);
    }

    public List<OrderModel> getAllOrders() {
        return orderRepo.findAll();
    }
}
