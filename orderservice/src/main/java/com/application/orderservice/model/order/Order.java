package com.application.orderservice.model.order;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "total_price", nullable = false)
    @NotNull
    private BigDecimal totalPrice;

    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedDate;

    @Column(name = "user_id")
    private String userId;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", userId='" + userId + '\'' +
                '}';
    }
}
