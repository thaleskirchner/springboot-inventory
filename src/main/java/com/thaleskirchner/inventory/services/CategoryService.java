package com.thaleskirchner.inventory.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thaleskirchner.inventory.dto.CategoryDTO;
import com.thaleskirchner.inventory.entities.Category;
import com.thaleskirchner.inventory.repositories.CategoryRepository;
import com.thaleskirchner.inventory.services.exceptions.DataBaseException;
import com.thaleskirchner.inventory.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAll(Pageable pageable) {
		Page<Category> result = repository.findAll(pageable);
		return result.map(CategoryDTO::new);
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Category entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
		return new CategoryDTO(entity);
	}

	@Transactional
	public Category insert(Category obj) {
		return repository.save(obj);
	}

	@Transactional
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
	}

	@Transactional
	public Category update(Long id, Category obj) {
		try {
			Category entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Category entity, Category obj) {
		entity.setName(obj.getName());
	}
}

