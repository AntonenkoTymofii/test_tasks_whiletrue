package org.example.test_tasks_whiletrue.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.test_tasks_whiletrue.converter.OrderConverter;
import org.example.test_tasks_whiletrue.dto.CreateOrderDTO;
import org.example.test_tasks_whiletrue.dto.OrderDTO;
import org.example.test_tasks_whiletrue.exception.orderException.OrderNotFoundException;
import org.example.test_tasks_whiletrue.exception.productExceptions.ProductNotFoundException;
import org.example.test_tasks_whiletrue.exception.userExceptions.UserNotFoundException;
import org.example.test_tasks_whiletrue.model.OrderModel;
import org.example.test_tasks_whiletrue.service.OrderService;
import org.example.test_tasks_whiletrue.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Order Controller", description = "API для управління замовленнями")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Operation(summary = "Створення нового замовлення",
            description = "Створює нове замовлення для поточного користувача")
    @ApiResponse(responseCode = "201", description = "Замовлення успішно створене",
            content = @Content(schema = @Schema(implementation = OrderDTO.class)))
    @ApiResponse(responseCode = "400", description = "Некоректні дані замовлення")
    @ApiResponse(responseCode = "404", description = "Користувача або товар не знайдено")
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderDTO orderDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            Long userId = userService.getUserIdByEmail(currentUserEmail);

            OrderModel order = orderService.createOrder(userId, orderDTO);
            OrderDTO responseDTO = OrderConverter.toDTO(order);

            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Отримання замовлення за ID",
            description = "Повертає інформацію про замовлення з вказаним ID")
    @ApiResponse(responseCode = "200", description = "Інформація про замовлення успішно отримана",
            content = @Content(schema = @Schema(implementation = OrderDTO.class)))
    @ApiResponse(responseCode = "404", description = "Замовлення не знайдено")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            OrderModel order = orderService.getOrderById(id);
            OrderDTO orderDTO = OrderConverter.toDTO(order);
            return ResponseEntity.ok(orderDTO);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Отримання всіх замовлень поточного користувача",
            description = "Повертає список всіх замовлень поточного користувача")
    @ApiResponse(responseCode = "200", description = "Список замовлень успішно отриманий")
    @ApiResponse(responseCode = "404", description = "Користувача не знайдено")
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            Long userId = userService.getUserIdByEmail(currentUserEmail);

            List<OrderModel> orders = orderService.getOrdersByUser(userId);
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderConverter::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(orderDTOs);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Скасування замовлення",
            description = "Видаляє замовлення за вказаним ID")
    @ApiResponse(responseCode = "204", description = "Замовлення успішно скасовано")
    @ApiResponse(responseCode = "403", description = "Доступ заборонено")
    @ApiResponse(responseCode = "404", description = "Замовлення не знайдено")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            OrderModel order = orderService.getOrderById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (!order.getUser().getEmail().equals(currentUserEmail) && !isAdmin) {
                return new ResponseEntity<>("Доступ заборонено", HttpStatus.FORBIDDEN);
            }

            orderService.cancelOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Отримання всіх замовлень (для адміністраторів)",
            description = "Повертає список всіх замовлень в системі")
    @ApiResponse(responseCode = "200", description = "Список замовлень успішно отриманий")
    @ApiResponse(responseCode = "403", description = "Доступ заборонено")
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return new ResponseEntity<>("Доступ заборонено", HttpStatus.FORBIDDEN);
        }

        List<OrderModel> orders = orderService.getAllOrders();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(OrderConverter::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderDTOs);
    }

    @Operation(summary = "Отримання всіх замовлень конкретного користувача (для адміністраторів)",
            description = "Повертає список всіх замовлень вказаного користувача")
    @ApiResponse(responseCode = "200", description = "Список замовлень успішно отриманий")
    @ApiResponse(responseCode = "403", description = "Доступ заборонено")
    @ApiResponse(responseCode = "404", description = "Користувача не знайдено")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUser(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return new ResponseEntity<>("Доступ заборонено", HttpStatus.FORBIDDEN);
        }

        try {
            List<OrderModel> orders = orderService.getOrdersByUser(userId);
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderConverter::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(orderDTOs);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}