package com.example.selenium.ta.demo.pageobject.webtablespage;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class WebTableRow {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final int age;
    private final int salary;
    private final String department;
}
