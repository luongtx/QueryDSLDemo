package com.demo.querydsl;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Customer> findAll(CustomerFilterCriteria criteria, String keyword) {
        return find(criteria, keyword);
    }

    private Page<Customer> find(CustomerFilterCriteria criteria, String keyword) {
        QCustomer customer = QCustomer.customer;
        keyword = CustomerFilterCriteria.concatLike(keyword);
        var customerHasKeyword = customer.firstName.likeIgnoreCase(keyword)
                .or(customer.lastName.likeIgnoreCase(keyword))
                .or(customer.email.likeIgnoreCase(keyword));
        var customerPropertiesMatch = customer.firstName.likeIgnoreCase(criteria.getFirstName())
                .and(customer.lastName.likeIgnoreCase(criteria.getLastName()))
                .and(customer.email.likeIgnoreCase(criteria.getEmail()));
        var finalFilterPredicate = customerHasKeyword.and(customerPropertiesMatch);
        Pageable pageable = PageRequest.of(
                criteria.getPage() - 1,
                criteria.getLimit()
        );
        return repository.findAll(finalFilterPredicate, pageable);
    }
}
