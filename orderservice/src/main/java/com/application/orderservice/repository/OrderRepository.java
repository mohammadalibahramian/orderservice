package com.application.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.orderservice.model.order.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
