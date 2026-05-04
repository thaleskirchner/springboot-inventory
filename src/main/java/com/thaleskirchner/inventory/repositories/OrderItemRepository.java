package com.thaleskirchner.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thaleskirchner.inventory.entities.OrderItem;
import com.thaleskirchner.inventory.entities.pk.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
