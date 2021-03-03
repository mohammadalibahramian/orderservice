package com.application.orderservice.Event;

import com.application.orderservice.dto.OrderDto;

public class OrderCreatedEvent extends OrderEvent {

    public OrderCreatedEvent(OrderDto order) {
        super(order);
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "order=" + order +
                '}';
    }
}
