package com.application.orderservice.dto;

import com.application.orderservice.model.order.OrderStatus;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Data transfer object for {@link com.application.orderservice.model.order.Order}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDto {

    private Long id;

    @NotNull
    private OrderStatus status;

    @NotNull
    @Min(value = 0L, message = "total price can not be less than zero")
    private BigDecimal totalPrice;

    private Date createdDate;

    private Date updatedDate;

    private String userId;

    public OrderDto(Long id, OrderStatus status, BigDecimal totalPrice) {
        this.id = id;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
