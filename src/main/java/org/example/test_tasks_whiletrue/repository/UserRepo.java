package org.example.test_tasks_whiletrue.repository;

import org.example.test_tasks_whiletrue.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByPhone(String phone);
}
