package com.demo.querydsl;

import org.springframework.data.domain.Page;

public interface CustomerService {

    Page<Customer> findAll(CustomerFilterCriteria criteria, String keyword);

}
