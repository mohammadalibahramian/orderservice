package com.application.orderservice.service.impl;

import com.application.orderservice.Event.OrderCreatedEvent;
import com.application.orderservice.Event.OrderDeletedEvent;
import com.application.orderservice.Event.OrderUpdatedEvent;
import com.application.orderservice.converter.ConverterService;
import com.application.orderservice.dto.OrderDto;
import com.application.orderservice.exception.OrderNotFoundException;
import com.application.orderservice.jwt.JwtService;
import com.application.orderservice.kafka.Producer;
import com.application.orderservice.model.order.Order;
import com.application.orderservice.model.order.OrderStatus;
import com.application.orderservice.repository.OrderRepository;
import com.application.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final Producer producer;
    private final ConverterService converter;
    private final JwtService jwtService;

    @Override
    public Page<OrderDto> findAll(Pageable pageable) {
        Page<Order> orderPage = repository.findAll(pageable);
        return orderPage.map(converter::convertToDto);
    }

    @Override
    public OrderDto save(OrderDto orderDto) {
        String userId = getUserId();

        Order order = convertToEntity(orderDto);
        order.setUserId(userId);
        order.setStatus(OrderStatus.CREATED);

        Order createdOrder = repository.save(order);

        OrderDto dto = convertToDto(createdOrder);
        producer.sendEvent(new OrderCreatedEvent(dto));
        return dto;
    }

    @Override
    public OrderDto update(OrderDto orderDto, Long orderId) {
        return repository.findById(orderId)
                .map(foundOrder -> {
                    Order order = convertToEntity(orderDto);
                    foundOrder.setStatus(order.getStatus());
                    foundOrder.setTotalPrice(order.getTotalPrice());
                    Order updatedOrder = repository.save(foundOrder);

                    OrderDto dto = converter.convertToDto(updatedOrder);
                    producer.sendEvent(new OrderUpdatedEvent(dto));
                    return dto;
                }).orElseThrow(() -> new OrderNotFoundException("Order not found with id " + orderId));
    }

    @Override
    public void delete(Long orderId) {
        repository.findById(orderId).ifPresent(tobeDeleted -> {
            repository.delete(tobeDeleted);
            producer.sendEvent(new OrderDeletedEvent(convertToDto(tobeDeleted)));
        });
    }

    private String getUserId() {
        return jwtService.getUserId();
    }

    private OrderDto convertToDto(Order createdOrder) {
        return converter.convertToDto(createdOrder);
    }

    private Order convertToEntity(OrderDto orderDto) {
        return converter.convertToEntity(orderDto);
    }
}
