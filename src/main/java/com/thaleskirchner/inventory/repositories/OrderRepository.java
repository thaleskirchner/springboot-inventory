package com.thaleskirchner.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thaleskirchner.inventory.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
