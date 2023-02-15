package com.demo.querydsl;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepositoryCustom repositoryCustom;

    public CustomerServiceImpl(CustomerRepositoryCustom repositoryCustom) {
        this.repositoryCustom = repositoryCustom;
    }

    @Override
    public Page<Customer> findAll(CustomerFilterCriteria criteria, PageCriteria pageCriteria, String keyword) {
        return repositoryCustom.findAll(criteria, pageCriteria, keyword);
    }
}
