package com.application.orderservice.service;

import com.application.orderservice.converter.ConverterService;
import com.application.orderservice.dto.OrderDto;
import com.application.orderservice.exception.OrderNotFoundException;
import com.application.orderservice.jwt.JwtService;
import com.application.orderservice.kafka.Producer;
import com.application.orderservice.model.order.Order;
import com.application.orderservice.model.order.OrderStatus;
import com.application.orderservice.repository.OrderRepository;
import com.application.orderservice.service.impl.OderServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OderServiceImpl orderService;

    @Mock
    private JwtService jwtService;

    @Mock
    private ConverterService converterService;

    @Mock
    private Producer producer;

    @Test
    public void createOrderTest() {
        // Given
        Order order = getOrder();
        OrderDto orderDto = getOrderDto();
        // When
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(jwtService.getUserId()).thenReturn("testUser");
        when(converterService.convertToEntity(orderDto)).thenReturn(order);
        when(converterService.convertToDto(order)).thenReturn(orderDto);
        // Then
        OrderDto dto = orderService.save(orderDto);
        Assert.assertEquals(1L, (long) dto.getId());
        Assert.assertEquals(0, dto.getTotalPrice().compareTo(new BigDecimal(12)));
        Assert.assertEquals(dto.getStatus(), OrderStatus.CREATED);
        assertThat(dto.getUserId()).isEqualTo("testUser");
    }

    @Test
    public void getAllOrdersTest() {
        // Given
        List<Order> orders = Arrays.asList(getOrder(), getOrder());
        Pageable pageable = PageRequest.of(1, 20);
        Page<Order> orderPage = new PageImpl<>(orders);
        // When
        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(jwtService.getUserId()).thenReturn("testUser");
        when(converterService.convertToEntity(getOrderDto())).thenReturn(getOrder());
        when(converterService.convertToDto(getOrder())).thenReturn(getOrderDto());
        // Then
        Assert.assertEquals(2, orderService.findAll(pageable).getSize());

    }

    @Test(expected = OrderNotFoundException.class)
    public void updateOrderShouldReturnNotFoundIfProvidedIdIsIncorrect() {
        // Given
        Order order = getOrder();
        OrderDto orderDto = getOrderDto();
        // When
        when(this.orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(jwtService.getUserId()).thenReturn("testUser");
        when(converterService.convertToEntity(orderDto)).thenReturn(order);
        when(converterService.convertToDto(order)).thenReturn(orderDto);
        // Then
        this.orderService.update(orderDto, 2L);
    }


    private Order getOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(new BigDecimal("12"));
        order.setUserId("testUser");
        return order;
    }

    private OrderDto getOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setStatus(OrderStatus.CREATED);
        orderDto.setTotalPrice(new BigDecimal("12"));
        orderDto.setUserId("testUser");
        return orderDto;
    }
}
