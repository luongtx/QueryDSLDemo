package com.demo.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanOperation;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.util.StringUtils;
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
        var customerPredicate = customer.isNotNull();
        if (!StringUtils.isNullOrEmpty(keyword)) {
            customerPredicate = customerPredicate.and(likeExp(customer.firstName, keyword)
                    .or(likeExp(customer.lastName, keyword))
                    .or(likeExp(customer.email, keyword))
            );
        }
        if (!StringUtils.isNullOrEmpty(criteria.getFirstName())) {
            customerPredicate = customerPredicate.and(likeExp(customer.firstName, criteria.getFirstName()));
        }
        if (!StringUtils.isNullOrEmpty(criteria.getLastName())) {
            customerPredicate = customerPredicate.and(likeExp(customer.lastName, criteria.getLastName()));
        }
        if (!StringUtils.isNullOrEmpty(criteria.getEmail())) {
            customerPredicate = customerPredicate.and(likeExp(customer.email, criteria.getEmail()));
        }
        if (criteria.getDob() != null) {
            customerPredicate = customerPredicate.and(
                    customer.dob.isNotNull().and(customer.dob.before(criteria.getDob()).not())
            );
        }
        if (criteria.getDod() != null) {
            customerPredicate = customerPredicate.and(
                    customer.dod.isNotNull().and(customer.dod.after(criteria.getDod()).not())
            );
        }
        if (criteria.getType() != null) {
            customerPredicate = customerPredicate.and(
                    customer.type.isNotNull().and(customer.type.eq(criteria.getType()))
            );
        }
        Pageable pageable = PageRequest.of(
                criteria.getPage() - 1,
                criteria.getLimit()
        );
        return repository.findAll(customerPredicate, pageable);
    }

    BooleanExpression likeExp(StringPath stringPath, String criteriaVal) {
        return stringPath.isNotNull().and(stringPath.likeIgnoreCase(concatLike(criteriaVal)));
    }

    public static String concatLike(String val) {
        if (StringUtils.isNullOrEmpty(val)) {
            return "%%";
        }
        return "%" + val + "%";
    }
}
