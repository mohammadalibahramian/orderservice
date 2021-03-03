package com.application.orderservice.Event;

import com.application.orderservice.dto.OrderDto;

public class OrderUpdatedEvent extends OrderEvent {

    public OrderUpdatedEvent(OrderDto order) {
        super(order);
    }

    @Override
    public String toString() {
        return "OrderUpdatedEvent{" +
                "order=" + order +
                '}';
    }
}
