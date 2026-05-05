package com.thaleskirchner.inventory.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import com.thaleskirchner.inventory.entities.StockMovement;
import com.thaleskirchner.inventory.services.StockMovementService;

@RestController
@RequestMapping(value = "/stock-movements")
public class StockMovementController {

	@Autowired
	private StockMovementService service;

	@GetMapping
	public ResponseEntity<List<StockMovement>> findAll() {
		List<StockMovement> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<StockMovement> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findById(id));
	}

	@GetMapping(value = "/product/{productId}")
	public ResponseEntity<List<StockMovement>> findByProductId(@PathVariable Long productId) {
		List<StockMovement> list = service.findByProductId(productId);
		return ResponseEntity.ok().body(list);
	}

	@PostMapping
	public ResponseEntity<StockMovement> insert(@Valid @RequestBody StockMovement obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
}
