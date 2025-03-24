package org.example.test_tasks_whiletrue.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.test_tasks_whiletrue.dto.UserDTO;
import org.example.test_tasks_whiletrue.dto.UserRegisterDTO;
import org.example.test_tasks_whiletrue.exception.userExceptions.UserEmailAlreadyExistException;
import org.example.test_tasks_whiletrue.exception.userExceptions.UserNotFoundException;
import org.example.test_tasks_whiletrue.exception.userExceptions.UserPhoneAlreadyExistException;
import org.example.test_tasks_whiletrue.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "API для управління користувачами")
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Реєстрація нового користувача",
            description = "Створює нового користувача в системі")
    @ApiResponse(responseCode = "201", description = "Користувач успішно створений")
    @ApiResponse(responseCode = "400", description = "Некоректні дані користувача")
    @ApiResponse(responseCode = "409", description = "Користувач з таким email або телефоном вже існує")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            userService.registerUser(userRegisterDTO, userRegisterDTO.getPassword());
            return new ResponseEntity<>("Користувач успішно зареєстрований", HttpStatus.CREATED);
        } catch (UserEmailAlreadyExistException | UserPhoneAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Отримання інформації про користувача",
            description = "Повертає інформацію про поточного авторизованого користувача")
    @ApiResponse(responseCode = "200", description = "Інформація про користувача успішно отримана",
            content = @Content(schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "404", description = "Користувача не знайдено")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        UserDTO user = userService.getUserByEmail(currentUserEmail);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Отримання інформації про користувача за ID",
            description = "Повертає інформацію про користувача з вказаним ID")
    @ApiResponse(responseCode = "200", description = "Інформація про користувача успішно отримана",
            content = @Content(schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "404", description = "Користувача не знайдено")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Видалення користувача",
            description = "Видаляє користувача з вказаним ID")
    @ApiResponse(responseCode = "200", description = "Користувач успішно видалений")
    @ApiResponse(responseCode = "404", description = "Користувача не знайдено")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Користувач успішно видалений");
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}