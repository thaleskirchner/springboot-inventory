package com.thaleskirchner.inventory.dto;

import com.thaleskirchner.inventory.entities.User;

public record UserDTO(Long id, String name, String email, String phone) {

    public UserDTO(User entity) {
        this(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone());
    }
}
