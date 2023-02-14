package com.demo.querydsl;

import com.querydsl.core.util.StringUtils;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerFilterCriteria extends PageCriteria {
    private String firstName;
    private String lastName;
    private String email;

    @Builder(builderMethodName = "customerBuilder")
    public CustomerFilterCriteria(
            Integer page,
            Integer limit,
            List<String> sort,
            String firstName,
            String lastName,
            String email
    ) {
        super(page, limit, sort);
        this.setPage(page != null ? page : 1);
        this.setLimit(limit != null ? limit : 250);
        this.firstName = concatLike(firstName);
        this.lastName = concatLike(lastName);
        this.email = concatLike(email);
    }

    public static String concatLike(String val) {
        if (StringUtils.isNullOrEmpty(val)) {
            return "%%";
        }
        return "%" + val + "%";
    }
}
