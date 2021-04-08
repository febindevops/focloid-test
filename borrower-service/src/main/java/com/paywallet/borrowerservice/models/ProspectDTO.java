package com.paywallet.borrowerservice.models;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ProspectDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    // @NotBlank(message = "Date of birth is required")
    // @Pattern(regexp = "^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[-
    // /.](19|20)\\d\\d$",message = "Please use mm/dd/yyyy format for date of
    // birth.")
    // private Date dateOfBirth;

}
