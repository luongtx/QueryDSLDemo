package com.demo.querydsl;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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
        var types = Arrays.asList('A', 'B', 'C', null);
        for (int i = 0; i < 7; i++) {
            var customer = Customer.builder()
                    .firstName(firstNames.get(i))
                    .lastName(lastNames.get(i))
                    .email(firstNames.get(i) + lastNames.get(i) + "@gmail.com")
                    .dob(LocalDate.of(1999 + i, i + 1, i + 1))
                    .dod(LocalDate.of(2050 + i, i + 1, i + 1))
                    .type(types.get(i % types.size()))
                    .build();
            customers.add(customer);
        }
        customers.add(Customer.builder()
                .firstName("Luong")
                .lastName("Tran")
                .build()
        );
        customers.add(Customer.builder().build());
        repository.saveAll(customers);
    }

    @Test
    void findAll() {
        var criteria = CustomerFilterCriteria
                .builder()
                .build();
        var pageCriteria = PageCriteria.builder().build();
        String keyword = null;
        var expectedCustomers = repository.findAll();
        var customers = service.findAll(criteria, pageCriteria, keyword).getContent();
        assertEquals(expectedCustomers.size(), customers.size());
        log.info(customers);
    }

    @Test
    void findWithKeyWord() {
        var criteria = CustomerFilterCriteria
                .builder()
                .build();
        var pageCriteria = PageCriteria.builder().build();
        String keyword = "neves";
        var customers = service.findAll(criteria, pageCriteria, keyword).getContent();
        assertEquals(1, customers.size());
        log.info(customers);
    }

    @Test
    void findWithDateRange() {
        var criteria = CustomerFilterCriteria
                .builder()
                .dob(LocalDate.of(2000, 1, 1))
                .dod(LocalDate.of(2055, 1, 1))
                .build();
        var pageCriteria = PageCriteria.builder().build();
        String keyword = "";
        var customers = service.findAll(criteria, pageCriteria, keyword).getContent();
        assertEquals(4, customers.size());
        log.info(customers);
    }

    @Test
    void findWithDateRangeNullDod() {
        var criteria = CustomerFilterCriteria
                .builder()
                .dob(LocalDate.of(2000, 1, 1))
                .build();
        var pageCriteria = PageCriteria.builder().build();
        String keyword = "";
        var customers = service.findAll(criteria, pageCriteria, keyword).getContent();
        assertEquals(6, customers.size());
        log.info(customers);
    }

    @Test
    void findWithDateRangeNullDob() {
        var criteria = CustomerFilterCriteria
                .builder()
                .dod(LocalDate.of(2055, 1, 1))
                .build();
        var pageCriteria = PageCriteria.builder().build();
        String keyword = null;
        var customers = service.findAll(criteria, pageCriteria, keyword).getContent();
        assertTrue(customers.size() > 0);
        assertEquals(5, customers.size());
        log.info(customers);
    }

    @Test
    void findWithDateRangeNullDobAndDod() {
        var criteria = CustomerFilterCriteria
                .builder()
                .build();
        var pageCriteria = PageCriteria.builder().build();
        String keyword = null;
        var expectedCustomers = repository.findAll();
        var customers = service.findAll(criteria, pageCriteria, keyword).getContent();
        assertTrue(customers.size() > 0);
        assertEquals(expectedCustomers.size(), customers.size());
        log.info(customers);
    }


    @Test
    void findWithTypeC() {
        var criteria = CustomerFilterCriteria
                .builder()
                .type('C')
                .build();
        var pageCriteria = PageCriteria.builder().build();
        String keyword = null;
        var customers = service.findAll(criteria, pageCriteria, keyword).getContent();
        assertTrue(customers.size() > 0);
        assertEquals(2, customers.size());
        log.info(customers);
    }

    @Test
    void testCustomerWithPageable() {
        var criteria = CustomerFilterCriteria
                .builder()
                .dob(LocalDate.of(1999,1,1))
                .dod(LocalDate.of(2060,1,1))
                .build();
        var pageCriteria = PageCriteria.builder()
                .page(2)
                .limit(5)
                .build();
        String keyword = null;
        var page = service.findAll(criteria, pageCriteria, keyword);
        log.info(page);
        assertEquals(7, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
        assertEquals(1, page.getNumber());
        var rows = page.getContent();
        assertEquals(2, rows.size());
        log.info(rows);
    }

//    @Test
//    void testCustomerWithPageableFirstPage() {
//        var criteria = CustomerFilterCriteria
//                .builder()
//                .dob(LocalDate.of(1999,1,1))
//                .dod(LocalDate.of(2060,1,1))
//                .build();
//        var pageCriteria = PageCriteria.builder()
//                .page(1)
//                .limit(5)
//                .build();
//        String keyword = null;
//        var page = service.findAll(criteria, pageCriteria, keyword);
//        log.info(page);
//        assertEquals(5, page.getTotalElements());
//        assertEquals(1, page.getTotalPages());
//        var rows = page.getContent();
//        assertEquals(5, rows.size());
//        log.info(rows);
//    }

}