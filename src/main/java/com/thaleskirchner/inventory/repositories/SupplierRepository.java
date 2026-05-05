package com.thaleskirchner.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thaleskirchner.inventory.entities.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}
