package com.xq.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {

    @NotEmpty(message = "Name can not be null or empty")
    @Size(min=5, max=30,message = "Customer name length must be greater or equal to 5 and less than or equal to 30")
    private String name;

    @NotEmpty(message = "Email address can not be null or empty")
    @Email(message = "Email address must be a valid email")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;
    private AccountsDto account;
}
