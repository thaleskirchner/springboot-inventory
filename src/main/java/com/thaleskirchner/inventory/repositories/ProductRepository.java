package com.thaleskirchner.inventory.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thaleskirchner.inventory.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.minimumStock")
	Page<Product> findLowStockProducts(Pageable pageable);
}
