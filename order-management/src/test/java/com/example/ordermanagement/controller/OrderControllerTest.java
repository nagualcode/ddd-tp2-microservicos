package com.example.ordermanagement.controller;

import com.example.ordermanagement.controller.OrderController;
import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService bookOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderDTO sampleBookOrder;

    @BeforeEach
    void setUp() {
        sampleBookOrder = new OrderDTO();
        sampleBookOrder.setId(1L);
        sampleBookOrder.setBookId(1L);
        sampleBookOrder.setCustomerId(1L);
        sampleBookOrder.setQuantity(2);
        sampleBookOrder.setTotalPrice(29.98);
    }

    @Test
    void testGetAllOrders() throws Exception {
        when(bookOrderService.getAllBookOrders()).thenReturn(Arrays.asList(sampleBookOrder));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].bookId").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].totalPrice").value(29.98));

        verify(bookOrderService, times(1)).getAllBookOrders();
    }

    @Test
    void testGetBookOrderById() throws Exception {
        when(bookOrderService.getBookOrderById(1L)).thenReturn(Optional.of(sampleBookOrder));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.totalPrice").value(29.98));

        verify(bookOrderService, times(1)).getBookOrderById(1L);
    }

    @Test
    void testGetBookOrderById_NotFound() throws Exception {
        when(bookOrderService.getBookOrderById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isNotFound());

        verify(bookOrderService, times(1)).getBookOrderById(1L);
    }

    @Test
    void testCreateBookOrder() throws Exception {
        when(bookOrderService.saveOrder(any(OrderDTO.class))).thenReturn(sampleBookOrder);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBookOrder)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.totalPrice").value(29.98));

        verify(bookOrderService, times(1)).saveOrder(any(OrderDTO.class));
    }

    @Test
    void testDeleteBookOrder() throws Exception {
        doNothing().when(bookOrderService).deleteBookOrder(1L);

        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());

        verify(bookOrderService, times(1)).deleteBookOrder(1L);
    }
}
