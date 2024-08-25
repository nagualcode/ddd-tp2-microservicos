package com.example.ordermanagement.controller;

import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService bookOrderService;

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return bookOrderService.getAllBookOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getBookOrderById(@PathVariable Long id) {
        return bookOrderService.getBookOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public OrderDTO createBookOrder(@RequestBody OrderDTO bookOrderDTO) {
        return bookOrderService.saveOrder(bookOrderDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookOrder(@PathVariable Long id) {
        bookOrderService.deleteBookOrder(id);
        return ResponseEntity.noContent().build();
    }
}
