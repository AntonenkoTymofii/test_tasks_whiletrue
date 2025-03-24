package org.example.test_tasks_whiletrue.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_tasks_whiletrue.config.SecurityConfig;
import org.example.test_tasks_whiletrue.converter.UserConverter;
import org.example.test_tasks_whiletrue.dto.UserDTO;
import org.example.test_tasks_whiletrue.dto.UserRegisterDTO;
import org.example.test_tasks_whiletrue.exception.userExceptions.*;
import org.example.test_tasks_whiletrue.model.UserModel;
import org.example.test_tasks_whiletrue.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepo userRepo;
    @Autowired
    @Lazy
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserService(UserRepo userRepo, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO registerUser(@Valid UserRegisterDTO userDTO, String password) throws UserEmailAlreadyExistException, UserPhoneAlreadyExistException {
        if (userRepo.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserEmailAlreadyExistException("Користувач з такою електронною поштою вже існує");
        }
        if (userRepo.findByPhone(userDTO.getPhone()).isPresent()) {
            throw new UserPhoneAlreadyExistException("Користувач з таким номером телефону вже існує");
        }

        UserModel user = UserConverter.toEntity(userDTO);
        user.setHash(passwordEncoder.encode(password));
        userRepo.save(user);

        log.info("User registered: {}", user.getEmail());
        return UserConverter.toDTO(user);
    }


    public UserDTO getUserById(Long id) throws UserNotFoundException {
        UserModel user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Користувача не знайдено"));
        return UserConverter.toDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) throws UserNotFoundException {
        UserModel user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Користувача не знайдено"));
        userRepo.delete(user);
        log.info("User deleted: {}", id);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) throws UserNotFoundException, UserEmailAlreadyExistException, UserPhoneAlreadyExistException {
        UserModel existingUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Користувача не знайдено"));

        existingUser.setFirstname(userDTO.getFirstname());
        existingUser.setLastname(userDTO.getLastname());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());

        userRepo.save(existingUser);
        log.info("User updated: {}", id);
        return UserConverter.toDTO(existingUser);
    }

    public @Valid UserRegisterDTO getUserByEmail(String email) throws UserNotFoundException {
        UserModel user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Користувача з такою електронною поштою не знайдено"));
        return (UserRegisterDTO) UserConverter.toDTO(user);
    }

    public Long getUserIdByEmail(String email) throws UserNotFoundException {
        UserModel user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Користувача з такою електронною поштою не знайдено"));
        return user.getId();
    }

    public UserModel getUserModelByEmail(String email) throws UserNotFoundException {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Користувача з такою електронною поштою не знайдено"));
    }

}
