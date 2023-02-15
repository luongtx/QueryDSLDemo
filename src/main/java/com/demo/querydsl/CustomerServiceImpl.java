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
        var keywordMatch = customer.isNotNull();
        if (!StringUtils.isNullOrEmpty(keyword)) {
            keywordMatch = likeExp(customer.firstName, keyword)
                    .or(likeExp(customer.lastName, keyword))
                    .or(likeExp(customer.email, keyword));
        }
        var propertiesMatch = customer.isNotNull();
        if (!StringUtils.isNullOrEmpty(criteria.getFirstName())) {
            propertiesMatch = propertiesMatch.and(likeExp(customer.firstName, criteria.getFirstName()));
        }
        if (!StringUtils.isNullOrEmpty(criteria.getLastName())) {
            propertiesMatch = propertiesMatch.and(likeExp(customer.lastName, criteria.getLastName()));
        }
        if (!StringUtils.isNullOrEmpty(criteria.getEmail())) {
            propertiesMatch = propertiesMatch.and(likeExp(customer.email, criteria.getEmail()));
        }
        if (criteria.getDob() != null) {
            propertiesMatch = propertiesMatch.and(
                    customer.dob.isNotNull().and(customer.dob.before(criteria.getDob()).not())
            );
        }
        if (criteria.getDod() != null) {
            propertiesMatch = propertiesMatch.and(
                    customer.dod.isNotNull().and(customer.dod.after(criteria.getDod()).not())
            );
        }
        if (criteria.getType() != null) {
            propertiesMatch = propertiesMatch.and(
                    customer.type.isNotNull().and(customer.type.eq(criteria.getType()))
            );
        }
        var finalFilterPredicate = keywordMatch.and(propertiesMatch);
        Pageable pageable = PageRequest.of(
                criteria.getPage() - 1,
                criteria.getLimit()
        );
        return repository.findAll(finalFilterPredicate, pageable);
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
