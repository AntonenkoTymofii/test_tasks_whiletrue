package org.example.test_tasks_whiletrue.converter;

import org.example.test_tasks_whiletrue.dto.ProductDTO;
import org.example.test_tasks_whiletrue.model.ProductModel;

public class ProductConverter {

    public static ProductDTO toDTO(ProductModel product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        return dto;
    }

    public static ProductModel toEntity(ProductDTO dto) {
        ProductModel product = new ProductModel();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return product;
    }
}
