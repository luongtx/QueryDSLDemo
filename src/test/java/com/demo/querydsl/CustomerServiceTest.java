package com.demo.querydsl;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceTest {
    @Autowired
    CustomerService service;

    @Autowired
    CustomerRepository repository;

    @BeforeAll
    public void setUp() {
        log.info("Setting up data test");
        var customers = new ArrayList<Customer>();
        var firstNames = Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven");
        var lastNames = Arrays.asList("Eno", "Owt", "Eerht", "Rouf", "Evif", "Xis", "Neves");
        for (int i = 0; i < 7; i++) {
            var customer = Customer.builder()
                    .firstName(firstNames.get(i))
                    .lastName(lastNames.get(i))
                    .email(firstNames.get(i) + lastNames.get(i) + "@gmail.com")
                    .build();
            customers.add(customer);
        }
        repository.saveAll(customers);
    }

    @Test
    void findAll() {
        var criteria = CustomerFilterCriteria
                .customerBuilder()
                .build();
        String keyword = null;
        var customers = service.findAll(criteria, keyword);
        assertEquals(7, customers.getContent().size());
        log.info(customers);
    }

    @Test
    void findWithKeyWord() {
        var criteria = CustomerFilterCriteria
                .customerBuilder()
                .build();
        String keyword = "neves";
        var customers = service.findAll(criteria, keyword);
        assertEquals(1, customers.getContent().size());
        log.info(customers);
    }
}