package com.example.ch7_8_test.repository;

import com.example.ch7_8_test.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}