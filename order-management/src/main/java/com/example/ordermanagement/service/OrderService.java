package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.BookDTO;
import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.dto.CustomerDTO;
import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository bookOrderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BookService bookService;

    public List<OrderDTO> getAllBookOrders() {
        return bookOrderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderDTO> getBookOrderById(Long id) {
        return bookOrderRepository.findById(id)
                .map(this::convertToDTO);
    }

    public OrderDTO saveOrder(OrderDTO bookOrderDTO) {
        // Verifica se o cliente existe
        CustomerDTO customer = customerService.getCustomerById(bookOrderDTO.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Cliente não encontrado");
        }

        // Verifica se o livro existe e obtém o preço
        BookDTO book = bookService.getBookById(bookOrderDTO.getBookId());
        if (book == null) {
            throw new RuntimeException("Livro não encontrado");
        }

        // Calcula o preço total
        bookOrderDTO.setTotalPrice(book.getPrice() * bookOrderDTO.getQuantity());

        // Converte DTO para entidade
        Order bookOrder = convertToEntity(bookOrderDTO);

        // Salva o pedido
        Order savedOrder = bookOrderRepository.save(bookOrder);

        // Converte a entidade salva de volta para DTO
        return convertToDTO(savedOrder);
    }

    public void deleteBookOrder(Long id) {
        bookOrderRepository.deleteById(id);
    }

    private Order convertToEntity(OrderDTO dto) {
        Order entity = new Order();
        entity.setId(dto.getId());
        entity.setBookId(dto.getBookId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setBookTitle(dto.getBookTitle());
        entity.setQuantity(dto.getQuantity());
        entity.setTotalPrice(dto.getTotalPrice());
        return entity;
    }

    private OrderDTO convertToDTO(Order entity) {
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setBookId(entity.getBookId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setBookTitle(entity.getBookTitle());
        dto.setQuantity(entity.getQuantity());
        dto.setTotalPrice(entity.getTotalPrice());
        return dto;
    }
}
