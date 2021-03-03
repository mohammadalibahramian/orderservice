package com.application.orderservice.Event;

import com.application.orderservice.dto.OrderDto;

public class OrderEvent {

    protected OrderDto order;

    public OrderEvent(OrderDto order) {
        this.order = order;
    }

    public OrderDto getOrder() {
        return order;
    }
}
