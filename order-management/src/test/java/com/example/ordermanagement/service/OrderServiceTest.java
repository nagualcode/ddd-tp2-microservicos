package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.BookDTO;
import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.dto.CustomerDTO;
import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.service.OrderService;
import com.example.ordermanagement.service.BookService;
import com.example.ordermanagement.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository bookOrderRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private OrderService bookOrderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBookOrder_Success() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBookId(1L);
        orderDTO.setCustomerId(1L);
        orderDTO.setQuantity(2);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setPrice(10.0);

        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);
        when(bookService.getBookById(1L)).thenReturn(bookDTO);
        when(bookOrderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderDTO result = bookOrderService.saveOrder(orderDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals(1L, result.getCustomerId());
        assertEquals(2, result.getQuantity());
        assertEquals(20.0, result.getTotalPrice());

        verify(customerService, times(1)).getCustomerById(1L);
        verify(bookService, times(1)).getBookById(1L);
        verify(bookOrderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testCreateBookOrder_CustomerNotFound() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBookId(1L);
        orderDTO.setCustomerId(1L);
        orderDTO.setQuantity(2);

        when(customerService.getCustomerById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bookOrderService.saveOrder(orderDTO));

        verify(customerService, times(1)).getCustomerById(1L);
        verify(bookService, never()).getBookById(anyLong());
        verify(bookOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testCreateBookOrder_BookNotFound() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBookId(1L);
        orderDTO.setCustomerId(1L);
        orderDTO.setQuantity(2);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");

        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);
        when(bookService.getBookById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bookOrderService.saveOrder(orderDTO));

        verify(customerService, times(1)).getCustomerById(1L);
        verify(bookService, times(1)).getBookById(1L);
        verify(bookOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testGetBookOrderById_Success() {
        // Arrange
        Long orderId = 1L;
        Order bookOrder = new Order();
        bookOrder.setId(orderId);
        bookOrder.setBookId(1L);
        bookOrder.setCustomerId(1L);
        bookOrder.setQuantity(2);
        bookOrder.setTotalPrice(20.0);

        when(bookOrderRepository.findById(orderId)).thenReturn(Optional.of(bookOrder));

        // Act
        Optional<OrderDTO> result = bookOrderService.getBookOrderById(orderId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getId());
        assertEquals(1L, result.get().getBookId());
        assertEquals(1L, result.get().getCustomerId());
        assertEquals(2, result.get().getQuantity());
        assertEquals(20.0, result.get().getTotalPrice());

        verify(bookOrderRepository, times(1)).findById(orderId);
    }

    @Test
    public void testGetBookOrderById_NotFound() {
        // Arrange
        Long orderId = 1L;
        when(bookOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        Optional<OrderDTO> result = bookOrderService.getBookOrderById(orderId);

        // Assert
        assertFalse(result.isPresent());
        verify(bookOrderRepository, times(1)).findById(orderId);
    }
}
