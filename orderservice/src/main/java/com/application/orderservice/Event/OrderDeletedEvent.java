package com.application.orderservice.Event;

import com.application.orderservice.dto.OrderDto;

public class OrderDeletedEvent extends OrderEvent{

    public OrderDeletedEvent(OrderDto order) {
        super(order);
    }

    @Override
    public String toString() {
        return "OrderDeletedEvent{" +
                "order=" + order +
                '}';
    }
}
