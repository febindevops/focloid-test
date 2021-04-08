package com.paywallet.borrowerservice.models.employers;

import lombok.Data;

@Data
public class EmploymentModel {
    private String jobTitle;
    private String employer;
    private String status;
    private String type;
    private String hiredOn;
}
