package com.demo.querydsl;

import org.springframework.data.domain.Page;

public interface CustomerRepositoryCustom {
    Page<Customer> findAll(CustomerFilterCriteria filterCriteria, PageCriteria pageCriteria, String keyword);
}
