package com.paywallet.borrowerservice.models.argyle;

import java.util.ArrayList;
import com.paywallet.borrowerservice.models.employers.EmploymentModel;
import lombok.Data;


@Data
public class ArgyleProspectProfileModel {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String maritalStatus;
    private String gender;
    private String ssn;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String country;
    private String postalCode;
    private ArrayList<EmploymentModel> employment;
}
