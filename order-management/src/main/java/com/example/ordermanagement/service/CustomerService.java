package com.example.ordermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.ordermanagement.dto.CustomerDTO;

@Service
public class CustomerService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String CLIENTE_SERVICE_URL = "http://localhost:8081/customers/";

    public CustomerDTO getCustomerById(Long id) {
        return restTemplate.getForObject(CLIENTE_SERVICE_URL + id, CustomerDTO.class);
    }
}
