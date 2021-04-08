package com.paywallet.borrowerservice.models.employers;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EmployersListDTO {
    @NotEmpty(message = "employers missing !")
    @Size(min = 0,message = "At least one employer id is required !")
    private List<Long> employers;

}