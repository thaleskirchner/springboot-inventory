package com.thaleskirchner.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thaleskirchner.inventory.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
