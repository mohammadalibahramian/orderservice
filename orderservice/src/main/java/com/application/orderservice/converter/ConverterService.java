package com.application.orderservice.converter;

import com.application.orderservice.dto.OrderDto;
import com.application.orderservice.model.order.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Converts Order Entity {@link Order} to Order Dto {@link OrderDto} and vice versa.
 */
@Component
@RequiredArgsConstructor
public class ConverterService {

    private final ModelMapper mapper;

    public OrderDto convertToDto(Order order) {
        return mapper.map(order, OrderDto.class);
    }

    public Order convertToEntity(OrderDto orderDto) {
        return mapper.map(orderDto, Order.class);
    }
}
