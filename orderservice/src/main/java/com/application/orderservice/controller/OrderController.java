package com.application.orderservice.controller;

import com.application.orderservice.dto.OrderDto;
import com.application.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @GetMapping("/orders")
    public Page<OrderDto> getOrders(Pageable pageable) {
        return service.findAll(pageable);
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@Valid @RequestBody OrderDto orderDto) {
        return service.save(orderDto);
    }

    @PutMapping("/orders/{orderId}")
    public OrderDto updateOrder(@PathVariable Long orderId, @Valid @RequestBody OrderDto orderDto) {
        return service.update(orderDto, orderId);
    }

    @DeleteMapping("/orders/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        service.delete(orderId);
    }
}
