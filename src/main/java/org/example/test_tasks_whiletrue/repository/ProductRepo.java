package org.example.test_tasks_whiletrue.repository;

import org.example.test_tasks_whiletrue.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByName(String name);
    List<ProductModel> findByPriceBetween(Float minPrice, Float maxPrice);
}
