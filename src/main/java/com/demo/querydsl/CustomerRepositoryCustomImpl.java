package com.demo.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Customer> findAll(CustomerFilterCriteria filterCriteria, PageCriteria pageCriteria, String keyword) {
        final JPAQuery<Customer> query = new JPAQuery<>(em);
        final QCustomer customer = QCustomer.customer;
        var predicate = customer.isNotNull();
        if (!StringUtils.isNullOrEmpty(keyword)) {
            predicate = predicate.and(likeExp(customer.firstName, keyword)
                    .or(likeExp(customer.lastName, keyword))
                    .or(likeExp(customer.email, keyword))
            );
        }
        if (!StringUtils.isNullOrEmpty(filterCriteria.getFirstName())) {
            predicate = predicate.and(likeExp(customer.firstName, filterCriteria.getFirstName()));
        }
        if (!StringUtils.isNullOrEmpty(filterCriteria.getLastName())) {
            predicate = predicate.and(likeExp(customer.lastName, filterCriteria.getLastName()));
        }
        if (!StringUtils.isNullOrEmpty(filterCriteria.getEmail())) {
            predicate = predicate.and(likeExp(customer.email, filterCriteria.getEmail()));
        }
        if (filterCriteria.getDob() != null) {
            predicate = predicate.and(
                    customer.dob.isNotNull().and(customer.dob.before(filterCriteria.getDob()).not())
            );
        }
        if (filterCriteria.getDod() != null) {
            predicate = predicate.and(
                    customer.dod.isNotNull().and(customer.dod.after(filterCriteria.getDod()).not())
            );
        }
        if (filterCriteria.getType() != null) {
            predicate = predicate.and(
                    customer.type.isNotNull().and(customer.type.eq(filterCriteria.getType()))
            );
        }
        Pageable pageable = toPageable(pageCriteria);
        List<Customer> customers = query.from(customer)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(customers, pageable, customers.size());
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

    Pageable toPageable(PageCriteria criteria) {
        if (criteria.getPage() == null) {
            criteria.setPage(1);
        }
        if (criteria.getLimit() == null) {
            criteria.setLimit(250);
        }
        return PageRequest.of(criteria.getPage() - 1, criteria.getLimit());
    }

}
