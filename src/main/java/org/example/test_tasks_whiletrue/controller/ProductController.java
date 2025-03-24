package org.example.test_tasks_whiletrue.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.test_tasks_whiletrue.dto.ProductDTO;
import org.example.test_tasks_whiletrue.exception.productExceptions.ProductNotFoundException;
import org.example.test_tasks_whiletrue.model.ProductModel;
import org.example.test_tasks_whiletrue.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Product Controller", description = "API для управління товарами")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Додавання нового товару",
            description = "Створює новий товар в системі")
    @ApiResponse(responseCode = "201", description = "Товар успішно створений",
            content = @Content(schema = @Schema(implementation = ProductDTO.class)))
    @ApiResponse(responseCode = "400", description = "Некоректні дані товару")
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductModel product = new ProductModel();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());

        ProductModel savedProduct = productService.addProduct(product);

        ProductDTO responseDTO = new ProductDTO();
        responseDTO.setId(savedProduct.getId());
        responseDTO.setName(savedProduct.getName());
        responseDTO.setPrice(savedProduct.getPrice());

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Отримання списку всіх товарів",
            description = "Повертає список всіх товарів в системі")
    @ApiResponse(responseCode = "200", description = "Список товарів успішно отриманий")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductModel> products = productService.getAllProducts();

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(productDTOs);
    }

    @Operation(summary = "Отримання товару за ID",
            description = "Повертає інформацію про товар з вказаним ID")
    @ApiResponse(responseCode = "200", description = "Інформація про товар успішно отримана",
            content = @Content(schema = @Schema(implementation = ProductDTO.class)))
    @ApiResponse(responseCode = "404", description = "Товар не знайдено")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            ProductModel product = productService.getProductById(id);

            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());

            return ResponseEntity.ok(productDTO);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Оновлення товару",
            description = "Оновлює інформацію про товар з вказаним ID")
    @ApiResponse(responseCode = "200", description = "Товар успішно оновлений",
            content = @Content(schema = @Schema(implementation = ProductDTO.class)))
    @ApiResponse(responseCode = "404", description = "Товар не знайдено")
    @ApiResponse(responseCode = "400", description = "Некоректні дані товару")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        try {
            ProductModel product = new ProductModel();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());

            ProductModel updatedProduct = productService.updateProduct(id, product);

            ProductDTO responseDTO = new ProductDTO();
            responseDTO.setId(updatedProduct.getId());
            responseDTO.setName(updatedProduct.getName());
            responseDTO.setPrice(updatedProduct.getPrice());

            return ResponseEntity.ok(responseDTO);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Видалення товару",
            description = "Видаляє товар з вказаним ID")
    @ApiResponse(responseCode = "200", description = "Товар успішно видалений")
    @ApiResponse(responseCode = "404", description = "Товар не знайдено")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Товар успішно видалений");
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}