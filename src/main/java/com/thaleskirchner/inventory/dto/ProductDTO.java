package com.thaleskirchner.inventory.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.thaleskirchner.inventory.entities.Product;

public record ProductDTO(
        Long id,
        String name,
        String description,
        Double price,
        String imgUrl,
        Integer stockQuantity,
        Integer minimumStock,
        String supplierName,
        Set<CategoryDTO> categories) {

    public ProductDTO(Product entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImgUrl(),
                entity.getStockQuantity(),
                entity.getMinimumStock(),
                entity.getSupplier() != null ? entity.getSupplier().getName() : null,
                entity.getCategories().stream().map(CategoryDTO::new).collect(Collectors.toSet()));
    }
}
