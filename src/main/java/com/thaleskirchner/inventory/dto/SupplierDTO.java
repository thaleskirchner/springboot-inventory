package com.thaleskirchner.inventory.dto;

import com.thaleskirchner.inventory.entities.Supplier;

public record SupplierDTO(Long id, String name, String cnpj, String email, String phone) {

    public SupplierDTO(Supplier entity) {
        this(entity.getId(), entity.getName(), entity.getCnpj(), entity.getEmail(), entity.getPhone());
    }
}
