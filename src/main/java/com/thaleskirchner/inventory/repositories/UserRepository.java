package com.thaleskirchner.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thaleskirchner.inventory.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
