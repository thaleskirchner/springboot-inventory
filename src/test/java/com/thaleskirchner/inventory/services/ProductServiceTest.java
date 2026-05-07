package com.thaleskirchner.inventory.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import com.thaleskirchner.inventory.dto.ProductDTO;
import com.thaleskirchner.inventory.entities.Product;
import com.thaleskirchner.inventory.repositories.ProductRepository;
import com.thaleskirchner.inventory.services.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Test
    @DisplayName("findAll deve retornar Page de ProductDTO")
    void findAll_shouldReturnPageOfProductDTO() {
        // Arrange
        Product p1 = new Product(1L, "Notebook", "Notebook Dell", 4500.00, "img.jpg", 50, 5, null);
        Product p2 = new Product(2L, "Mouse", "Mouse Logitech", 350.00, "img2.jpg", 100, 20, null);
        Page<Product> page = new PageImpl<>(List.of(p1, p2));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        // Act
        Page<ProductDTO> result = service.findAll(pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals("Notebook", result.getContent().get(0).name());
        assertEquals("Mouse", result.getContent().get(1).name());
    }

    @Test
    @DisplayName("findById deve retornar ProductDTO quando o produto existe")
    void findById_shouldReturnProductDTO_whenExists() {
        // Arrange
        Product product = new Product(1L, "Notebook", "Notebook Dell", 4500.00, "img.jpg", 50, 5, null);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        ProductDTO result = service.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Notebook", result.name());
        assertEquals(4500.00, result.price());
    }

    @Test
    @DisplayName("findById deve lançar ResourceNotFoundException quando o produto não existe")
    void findById_shouldThrow_whenNotFound() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(999L);
        });
    }

    @Test
    @DisplayName("insert deve salvar e retornar o produto")
    void insert_shouldSaveAndReturnProduct() {
        // Arrange
        Product product = new Product(null, "Teclado", "Teclado mecânico", 280.00, "img.jpg", 30, 10, null);
        Product saved = new Product(1L, "Teclado", "Teclado mecânico", 280.00, "img.jpg", 30, 10, null);

        when(repository.save(any(Product.class))).thenReturn(saved);

        // Act
        Product result = service.insert(product);

        // Assert
        assertNotNull(result.getId());
        assertEquals("Teclado", result.getName());
        verify(repository, times(1)).save(product);
    }

    @Test
    @DisplayName("delete deve chamar deleteById no repository")
    void delete_shouldCallDeleteById() {
        // Arrange
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        // Act
        service.delete(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }
}
