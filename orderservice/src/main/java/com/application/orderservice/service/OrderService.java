package com.application.orderservice.service;

import com.application.orderservice.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> findAll(Pageable pageable);
    OrderDto save(OrderDto orderDto);
    OrderDto update(OrderDto orderDto, Long orderId);
    void delete(Long orderId);
}
