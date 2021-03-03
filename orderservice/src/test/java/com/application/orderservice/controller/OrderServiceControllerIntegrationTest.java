package com.application.orderservice.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;

import com.application.orderservice.dto.OrderDto;
import com.application.orderservice.model.order.OrderStatus;
import com.application.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@WebAppConfiguration
class OrderServiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(username = "user")
    public void createOrderTest() throws Exception {
        OrderDto dto = new OrderDto(123L, OrderStatus.CREATED, new BigDecimal("12.80"));
        when(this.orderService.save(any(OrderDto.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/orders")
                .content(convertToJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "user")
    public void createOrderShouldReturnBadRequestIfTotalPriceIsNull() throws Exception {
        OrderDto dto = new OrderDto(123L, OrderStatus.CREATED, null);
        when(this.orderService.save(any(OrderDto.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/orders")
                .content(convertToJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    public void createOrderShouldReturnBadRequestIfTotalPriceIsLessThanZero() throws Exception {
        OrderDto dto = new OrderDto(123L, OrderStatus.CREATED, new BigDecimal("-12.80"));
        when(this.orderService.save(any(OrderDto.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/orders")
                .content(convertToJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    public void updateOrderTest() throws Exception {
        OrderDto dto = new OrderDto(1L, OrderStatus.CREATED, new BigDecimal("12.80"));
        when(orderService.update(any(OrderDto.class), eq(1L))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/orders/{orderId}", 1)
                .content(convertToJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(12.80));
    }

    @Test
    @WithMockUser(username = "user")
    public void getOrdersTest() throws Exception {
        List<OrderDto> orders = Arrays.asList(
                new OrderDto(1L, OrderStatus.CREATED, new BigDecimal("12.80")),
                new OrderDto(2L, OrderStatus.CREATED, new BigDecimal("20"))
        );
        Page<OrderDto> page = new PageImpl<>(orders);

        when(orderService.findAll(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(orders.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(orders.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status")
                        .value(orders.get(0).getStatus().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].status")
                        .value(orders.get(1).getStatus().name()));
    }

    @Test
    @WithMockUser(username = "user")
    public void deleteOrderTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    public static String convertToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
