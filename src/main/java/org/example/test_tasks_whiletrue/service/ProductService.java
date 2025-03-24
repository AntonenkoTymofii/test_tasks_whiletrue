package org.example.test_tasks_whiletrue.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_tasks_whiletrue.exception.productExceptions.ProductNotFoundException;
import org.example.test_tasks_whiletrue.model.ProductModel;
import org.example.test_tasks_whiletrue.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;

    @Transactional
    public ProductModel addProduct(ProductModel product) {
        ProductModel savedProduct = productRepo.save(product);
        log.info("Product added: {}", savedProduct.getId());
        return savedProduct;
    }

    public ProductModel getProductById(Long id) throws ProductNotFoundException {
        return productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар не знайдено з ID: " + id));
    }

    public List<ProductModel> getAllProducts() {
        return productRepo.findAll();
    }

    public List<ProductModel> getProductsByName(String name) {
        return productRepo.findByName(name);
    }

    public List<ProductModel> getProductsByPriceRange(Float minPrice, Float maxPrice) {
        return productRepo.findByPriceBetween(minPrice, maxPrice);
    }

    @Transactional
    public void deleteProduct(Long id) throws ProductNotFoundException {
        ProductModel product = getProductById(id);
        productRepo.delete(product);
        log.info("Product deleted: {}", id);
    }

    @Transactional
    public ProductModel updateProduct(Long id, ProductModel productDetails) throws ProductNotFoundException {
        ProductModel product = getProductById(id);

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());

        ProductModel updatedProduct = productRepo.save(product);
        log.info("Product updated: {}", id);
        return updatedProduct;
    }
}