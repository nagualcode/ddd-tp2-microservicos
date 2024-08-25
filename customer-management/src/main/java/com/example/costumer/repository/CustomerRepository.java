package com.example.costumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.costumer.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
