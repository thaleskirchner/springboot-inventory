package com.thaleskirchner.inventory.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thaleskirchner.inventory.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.minimumStock")
	List<Product> findLowStockProducts();
}
