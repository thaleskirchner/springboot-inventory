package com.thaleskirchner.inventory.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thaleskirchner.inventory.entities.Product;
import com.thaleskirchner.inventory.entities.StockMovement;
import com.thaleskirchner.inventory.entities.User;
import com.thaleskirchner.inventory.entities.enums.MovementType;
import com.thaleskirchner.inventory.repositories.ProductRepository;
import com.thaleskirchner.inventory.repositories.StockMovementRepository;
import com.thaleskirchner.inventory.services.exceptions.DataBaseException;

@ExtendWith(MockitoExtension.class)
public class StockMovementServiceTest {

    @InjectMocks
    private StockMovementService service;

    @Mock
    private StockMovementRepository repository;

    @Mock
    private ProductRepository productRepository;

    private Product product;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Carlos", "carlos@test.com", "11999990001", "123456");
        product = new Product(1L, "Notebook", "Notebook Dell", 4500.00, "img.jpg", 50, 5, null);
    }

    @Test
    @DisplayName("Entrada deve aumentar o estoque do produto")
    void insert_entry_shouldIncreaseStock() {
        // Arrange
        StockMovement movement = new StockMovement(null, 20, MovementType.ENTRY, Instant.now(),
                "Compra fornecedor", product, user);

        when(repository.save(any(StockMovement.class))).thenReturn(movement);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        service.insert(movement);

        // Assert
        assertEquals(70, product.getStockQuantity()); // 50 + 20
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Saída deve diminuir o estoque do produto")
    void insert_exit_shouldDecreaseStock() {
        // Arrange
        StockMovement movement = new StockMovement(null, 10, MovementType.EXIT, Instant.now(),
                "Venda para cliente", product, user);

        when(repository.save(any(StockMovement.class))).thenReturn(movement);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        service.insert(movement);

        // Assert
        assertEquals(40, product.getStockQuantity()); // 50 - 10
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Saída maior que estoque deve lançar DataBaseException")
    void insert_exit_shouldThrowWhenInsufficientStock() {
        // Arrange
        StockMovement movement = new StockMovement(null, 999, MovementType.EXIT, Instant.now(),
                "Venda impossível", product, user);

        when(repository.save(any(StockMovement.class))).thenReturn(movement);

        // Act & Assert
        DataBaseException exception = assertThrows(DataBaseException.class, () -> {
            service.insert(movement);
        });

        assertEquals("Estoque insuficiente", exception.getMessage());
        verify(productRepository, never()).save(any()); // NÃO deve salvar o produto
    }

    @Test
    @DisplayName("Devolução deve aumentar o estoque do produto")
    void insert_return_shouldIncreaseStock() {
        // Arrange
        StockMovement movement = new StockMovement(null, 5, MovementType.RETURN, Instant.now(),
                "Devolução - produto com defeito", product, user);

        when(repository.save(any(StockMovement.class))).thenReturn(movement);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        service.insert(movement);

        // Assert
        assertEquals(55, product.getStockQuantity()); // 50 + 5
    }

    @Test
    @DisplayName("Ajuste deve substituir o estoque atual pelo valor informado")
    void insert_adjustment_shouldSetExactStock() {
        // Arrange
        StockMovement movement = new StockMovement(null, 100, MovementType.ADJUSTMENT, Instant.now(),
                "Inventário físico", product, user);

        when(repository.save(any(StockMovement.class))).thenReturn(movement);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        service.insert(movement);

        // Assert
        assertEquals(100, product.getStockQuantity()); // Substitui para 100
    }

    @Test
    @DisplayName("Saída exata igual ao estoque deve funcionar (zerar estoque)")
    void insert_exit_shouldAllowExactStock() {
        // Arrange
        StockMovement movement = new StockMovement(null, 50, MovementType.EXIT, Instant.now(),
                "Venda total do estoque", product, user);

        when(repository.save(any(StockMovement.class))).thenReturn(movement);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        service.insert(movement);

        // Assert
        assertEquals(0, product.getStockQuantity()); // 50 - 50 = 0
    }
}
