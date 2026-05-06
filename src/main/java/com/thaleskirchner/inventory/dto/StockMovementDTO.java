package com.thaleskirchner.inventory.dto;

import java.time.Instant;

import com.thaleskirchner.inventory.entities.StockMovement;

public record StockMovementDTO(
        Long id,
        Integer quantity,
        String movementType,
        Instant moment,
        String reason,
        String productName,
        String userName) {

    public StockMovementDTO(StockMovement entity) {
        this(
                entity.getId(),
                entity.getQuantity(),
                entity.getMovementType().name(),
                entity.getMoment(),
                entity.getReason(),
                entity.getProduct() != null ? entity.getProduct().getName() : null,
                entity.getUser() != null ? entity.getUser().getName() : null);
    }
}
