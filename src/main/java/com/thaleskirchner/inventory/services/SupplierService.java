package com.thaleskirchner.inventory.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thaleskirchner.inventory.dto.SupplierDTO;
import com.thaleskirchner.inventory.entities.Supplier;
import com.thaleskirchner.inventory.repositories.SupplierRepository;
import com.thaleskirchner.inventory.services.exceptions.DataBaseException;
import com.thaleskirchner.inventory.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SupplierService {

	@Autowired
	private SupplierRepository repository;

	@Transactional(readOnly = true)
	public Page<SupplierDTO> findAll(Pageable pageable) {
		Page<Supplier> result = repository.findAll(pageable);
		return result.map(SupplierDTO::new);
	}

	@Transactional(readOnly = true)
	public SupplierDTO findById(Long id) {
		Supplier entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
		return new SupplierDTO(entity);
	}

	@Transactional
	public Supplier insert(Supplier obj) {
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
	public Supplier update(Long id, Supplier obj) {
		try {
			Supplier entity = repository.getReferenceById(id);
			updateData(entity, obj);
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Supplier entity, Supplier obj) {
		entity.setName(obj.getName());
		entity.setCnpj(obj.getCnpj());
		entity.setEmail(obj.getEmail());
		entity.setPhone(obj.getPhone());
	}
}
