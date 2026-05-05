package com.thaleskirchner.inventory.config;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.thaleskirchner.inventory.entities.Category;
import com.thaleskirchner.inventory.entities.Product;
import com.thaleskirchner.inventory.entities.StockMovement;
import com.thaleskirchner.inventory.entities.Supplier;
import com.thaleskirchner.inventory.entities.User;
import com.thaleskirchner.inventory.entities.enums.MovementType;
import com.thaleskirchner.inventory.repositories.CategoryRepository;
import com.thaleskirchner.inventory.repositories.ProductRepository;
import com.thaleskirchner.inventory.repositories.StockMovementRepository;
import com.thaleskirchner.inventory.repositories.SupplierRepository;
import com.thaleskirchner.inventory.repositories.UserRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StockMovementRepository stockMovementRepository;

	@Override
	public void run(String... args) throws Exception {

		// Users (operadores do estoque)
		User u1 = new User(null, "Carlos Silva", "carlos@empresa.com", "11999990001", "123456");
		User u2 = new User(null, "Ana Souza", "ana@empresa.com", "11999990002", "123456");
		userRepository.saveAll(Arrays.asList(u1, u2));

		// Categories
		Category cat1 = new Category(null, "Eletrônicos");
		Category cat2 = new Category(null, "Roupas");
		Category cat3 = new Category(null, "Alimentos");
		categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3));

		// Suppliers
		Supplier sup1 = new Supplier(null, "TechDistribuidora LTDA", "12.345.678/0001-90", "contato@techdist.com",
				"1140001234");
		Supplier sup2 = new Supplier(null, "Moda & Cia", "98.765.432/0001-10", "vendas@modacia.com", "1140005678");
		supplierRepository.saveAll(Arrays.asList(sup1, sup2));

		// Products
		Product p1 = new Product(null, "Notebook Dell Inspiron", "Notebook 15.6\", 16GB RAM, 512GB SSD", 4500.00,
				"https://example.com/notebook.jpg", 25, 5, sup1);
		Product p2 = new Product(null, "Mouse Logitech MX Master", "Mouse sem fio ergonômico", 350.00,
				"https://example.com/mouse.jpg", 100, 20, sup1);
		Product p3 = new Product(null, "Camiseta Polo", "Camiseta polo algodão, tamanho M", 89.90,
				"https://example.com/polo.jpg", 3, 10, sup2);
		Product p4 = new Product(null, "Teclado Mecânico", "Teclado mecânico RGB, switches blue", 280.00,
				"https://example.com/teclado.jpg", 50, 10, sup1);
		Product p5 = new Product(null, "Café Premium 500g", "Café torrado e moído, 500g", 32.00,
				"https://example.com/cafe.jpg", 8, 15, sup2);

		p1.getCategories().add(cat1);
		p2.getCategories().add(cat1);
		p3.getCategories().add(cat2);
		p4.getCategories().add(cat1);
		p5.getCategories().add(cat3);

		productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

		// Stock Movements
		StockMovement sm1 = new StockMovement(null, 50, MovementType.ENTRY, Instant.parse("2025-04-01T10:00:00Z"),
				"Compra inicial - fornecedor TechDist", p1, u1);
		StockMovement sm2 = new StockMovement(null, 200, MovementType.ENTRY, Instant.parse("2025-04-01T10:30:00Z"),
				"Compra inicial - fornecedor TechDist", p2, u1);
		StockMovement sm3 = new StockMovement(null, 25, MovementType.EXIT, Instant.parse("2025-04-15T14:00:00Z"),
				"Venda para cliente final", p1, u2);
		StockMovement sm4 = new StockMovement(null, 100, MovementType.EXIT, Instant.parse("2025-04-20T09:00:00Z"),
				"Venda para loja parceira", p2, u2);
		StockMovement sm5 = new StockMovement(null, 30, MovementType.ENTRY, Instant.parse("2025-04-25T11:00:00Z"),
				"Reposição de estoque", p3, u1);
		StockMovement sm6 = new StockMovement(null, 27, MovementType.EXIT, Instant.parse("2025-05-01T16:00:00Z"),
				"Venda em promoção", p3, u2);
		StockMovement sm7 = new StockMovement(null, 5, MovementType.RETURN, Instant.parse("2025-05-02T08:30:00Z"),
				"Devolução - produto com defeito", p4, u1);

		stockMovementRepository.saveAll(Arrays.asList(sm1, sm2, sm3, sm4, sm5, sm6, sm7));
	}
}
