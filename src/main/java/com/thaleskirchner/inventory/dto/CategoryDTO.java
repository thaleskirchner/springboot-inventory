package com.thaleskirchner.inventory.dto;

import com.thaleskirchner.inventory.entities.Category;

public record CategoryDTO(Long id, String name) {

    public CategoryDTO(Category entity) {
        this(entity.getId(), entity.getName());
    }
}
