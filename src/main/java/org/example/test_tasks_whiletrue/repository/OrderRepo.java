package org.example.test_tasks_whiletrue.repository;

import org.example.test_tasks_whiletrue.model.OrderModel;
import org.example.test_tasks_whiletrue.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepo extends JpaRepository<OrderModel, Long> {
    List<OrderModel> findByDate(LocalDate date);
    List<OrderModel> findByUser(UserModel user);
}
