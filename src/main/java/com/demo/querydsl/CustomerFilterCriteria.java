package com.demo.querydsl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerFilterCriteria {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private LocalDate dod;
    private Character type;
}
