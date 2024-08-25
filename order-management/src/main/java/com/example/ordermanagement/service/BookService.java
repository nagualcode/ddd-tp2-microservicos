package com.example.ordermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.ordermanagement.dto.BookDTO;

@Service
public class BookService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String LIVRO_SERVICE_URL = "http://localhost:8082/books/";

    public BookDTO getBookById(Long id) {
        return restTemplate.getForObject(LIVRO_SERVICE_URL + id, BookDTO.class);
    }
}

