package com.thaleskirchner.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thaleskirchner.inventory.entities.Product;
import com.thaleskirchner.inventory.entities.StockMovement;
import com.thaleskirchner.inventory.repositories.ProductRepository;
import com.thaleskirchner.inventory.repositories.StockMovementRepository;
import com.thaleskirchner.inventory.services.exceptions.DataBaseException;
import com.thaleskirchner.inventory.services.exceptions.ResourceNotFoundException;

@Service
public class StockMovementService {

	@Autowired
	private StockMovementRepository repository;

	@Autowired
	private ProductRepository productRepository;

	public List<StockMovement> findAll() {
		return repository.findAll();
	}

	public StockMovement findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public List<StockMovement> findByProductId(Long productId) {
		return repository.findByProductId(productId);
	}

	public StockMovement insert(StockMovement obj) {
		StockMovement movement = repository.save(obj);
		updateProductStock(movement);
		return movement;
	}

	private void updateProductStock(StockMovement movement) {
		Product product = movement.getProduct();
		Integer currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;

		switch (movement.getMovementType()) {
			case ENTRY:
			case RETURN:
				product.setStockQuantity(currentStock + movement.getQuantity());
				break;
			case EXIT:
				if (currentStock - movement.getQuantity() < 0) {
					throw new DataBaseException("Estoque insuficiente");
				}
				product.setStockQuantity(currentStock - movement.getQuantity());
				break;
			case ADJUSTMENT:
				product.setStockQuantity(movement.getQuantity());
				break;
		}

		productRepository.save(product);
	}
}
