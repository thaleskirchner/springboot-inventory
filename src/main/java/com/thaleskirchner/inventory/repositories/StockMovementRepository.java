package com.thaleskirchner.inventory.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thaleskirchner.inventory.entities.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

	Page<StockMovement> findByProductId(Long productId, Pageable pageable);
}
