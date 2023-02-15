package com.demo.querydsl;

import com.querydsl.core.util.StringUtils;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerFilterCriteria extends PageCriteria {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private LocalDate dod;
    private Character type;

    @Builder(builderMethodName = "customerBuilder")
    public CustomerFilterCriteria(
            Integer page,
            Integer limit,
            List<String> sort,
            String firstName,
            String lastName,
            String email,
            LocalDate dob,
            LocalDate dod,
            Character type
    ) {
        super(page, limit, sort);
        this.setPage(page != null ? page : 1);
        this.setLimit(limit != null ? limit : 250);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.type = type;
        this.dob = dob;
        this.dod = dod;
    }
}
